package com.uit.petrescueapi.application.usecase;

import com.uit.petrescueapi.application.dto.auth.AuthTokenResponseDto;
import com.uit.petrescueapi.application.dto.auth.LoginRequestDto;
import com.uit.petrescueapi.application.dto.auth.RefreshTokenRequestDto;
import com.uit.petrescueapi.application.dto.auth.RegisterRequestDto;
import com.uit.petrescueapi.application.dto.user.UserResponseDto;
import com.uit.petrescueapi.application.port.command.AuthCommandPort;
import com.uit.petrescueapi.domain.entity.EmailVerificationToken;
import com.uit.petrescueapi.domain.entity.RefreshToken;
import com.uit.petrescueapi.domain.entity.User;
import com.uit.petrescueapi.domain.exception.BusinessException;
import com.uit.petrescueapi.domain.exception.UnauthorizedException;
import com.uit.petrescueapi.domain.service.AuthDomainService;
import com.uit.petrescueapi.domain.valueobject.UserStatus;
import com.uit.petrescueapi.domain.entity.Organization;
import com.uit.petrescueapi.domain.repository.OrganizationRepository;
import com.uit.petrescueapi.infrastructure.email.EmailService;
import com.uit.petrescueapi.infrastructure.security.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Application-layer use case that orchestrates authentication workflows.
 *
 * <p>Combines domain logic ({@link AuthDomainService}) with infrastructure
 * concerns (password hashing, JWT creation, email sending) without leaking
 * those details into the domain layer.</p>
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class AuthCommandUseCase implements AuthCommandPort {

    private final AuthDomainService authDomainService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailService emailService;
    private final com.uit.petrescueapi.domain.repository.OrganizationMemberRepository organizationMemberRepository;
    private final OrganizationRepository organizationRepository;

    @Value("${app.security.refresh-token.expiry-days:7}")
    private long refreshTokenExpiryDays;

    @Value("${app.email.verification-token-expiry-minutes:1440}")
    private long verificationTokenExpiryMinutes;

    // ── Register ────────────────────────────────

    @Override
    public AuthTokenResponseDto register(RegisterRequestDto cmd) {
        String hashedPassword = passwordEncoder.encode(cmd.getPassword());

        User user = authDomainService.registerUser(
                cmd.getUsername(),
                cmd.getEmail(),
                hashedPassword,
                cmd.getFullName(),
                cmd.getPhone(),
                cmd.getGender(),
                cmd.getStreetAddress(),
                cmd.getWardCode(),
                cmd.getWardName(),
                cmd.getProvinceCode(),
                cmd.getProvinceName());

        // Create and send verification token
        EmailVerificationToken verificationToken =
                authDomainService.createVerificationToken(user.getId(), verificationTokenExpiryMinutes);
        emailService.sendVerificationEmail(user.getEmail(), user.getUsername(), verificationToken.getToken());

        // Generate tokens so the user is "logged in" immediately
        return buildTokenResponse(user);
    }

    // ── Login ───────────────────────────────────

    @Override
    public AuthTokenResponseDto login(LoginRequestDto cmd) {
        User user = authDomainService.findByEmailOrUsername(cmd.getEmailOrUsername());

        if (!passwordEncoder.matches(cmd.getPassword(), user.getPasswordHash())) {
            throw new UnauthorizedException("Invalid credentials");
        }

        if (user.getStatus() == UserStatus.BANNED) {
            throw new BusinessException("Your account has been banned");
        }

        if (!user.isEmailVerified()) {
            throw new BusinessException("Please verify your email before logging in");
        }

        return buildTokenResponse(user);
    }

    // ── Refresh Token ───────────────────────────

    @Override
    public AuthTokenResponseDto refreshToken(RefreshTokenRequestDto cmd) {
        RefreshToken newToken = authDomainService.rotateRefreshToken(
                cmd.getRefreshToken(), refreshTokenExpiryDays);

        User user = authDomainService.findById(newToken.getUserId());

        List<String> roleCodes = user.getRoles().stream()
                .map(r -> r.getCode())
                .toList();

        // If user has MEMBER role, lookup their org context
        UUID organizationId = null;
        String organizationName = null;
        String organizationRole = null;
        if (roleCodes.contains("MEMBER")) {
            organizationId = organizationMemberRepository.findOrganizationIdByUserId(user.getId());
            organizationRole = organizationMemberRepository.findOrgRoleByUserId(user.getId());
            if (organizationId != null) {
                organizationName = organizationRepository.findById(organizationId)
                        .map(Organization::getName)
                        .orElse(null);
            }
        }

        String accessToken = jwtService.generateAccessToken(
                user.getId(), user.getEmail(), roleCodes, organizationId);

        return AuthTokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(newToken.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessTokenExpirationSeconds())
                .user(toUserResponse(user, organizationId, organizationName, organizationRole))
                .build();
    }

    // ── Email Verification ──────────────────────

    @Override
    public void verifyEmail(String token) {
        authDomainService.verifyEmail(token);
        log.info("Email verified via token");
    }

    @Override
    public void resendVerificationEmail(String email) {
        EmailVerificationToken token =
                authDomainService.resendVerificationToken(email, verificationTokenExpiryMinutes);

        User user = authDomainService.findByEmail(email);
        emailService.sendVerificationEmail(user.getEmail(), user.getUsername(), token.getToken());
        log.info("Resent verification email to {}", email);
    }

    // ── Logout ──────────────────────────────────

    @Override
    public void logout(String userId) {
        authDomainService.revokeAllTokens(UUID.fromString(userId));
        log.info("User {} logged out — all refresh tokens revoked", userId);
    }

    // ── Helpers ─────────────────────────────────

    private AuthTokenResponseDto buildTokenResponse(User user) {
        List<String> roleCodes = user.getRoles().stream()
                .map(r -> r.getCode())
                .toList();

        // If user has MEMBER role, lookup their org context
        UUID organizationId = null;
        String organizationName = null;
        String organizationRole = null;
        if (roleCodes.contains("MEMBER")) {
            organizationId = organizationMemberRepository.findOrganizationIdByUserId(user.getId());
            organizationRole = organizationMemberRepository.findOrgRoleByUserId(user.getId());
            if (organizationId != null) {
                organizationName = organizationRepository.findById(organizationId)
                        .map(Organization::getName)
                        .orElse(null);
            }
            log.debug("User {} org context: orgId={}, role={}", user.getId(), organizationId, organizationRole);
        }

        String accessToken = jwtService.generateAccessToken(
                user.getId(), user.getEmail(), roleCodes, organizationId);

        RefreshToken refreshToken = authDomainService.createRefreshToken(
                user.getId(), refreshTokenExpiryDays);

        return AuthTokenResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken.getToken())
                .tokenType("Bearer")
                .expiresIn(jwtService.getAccessTokenExpirationSeconds())
                .user(toUserResponse(user, organizationId, organizationName, organizationRole))
                .build();
    }

    private UserResponseDto toUserResponse(User user, UUID organizationId, String organizationName, String organizationRole) {
        return UserResponseDto.builder()
                .userId(user.getId())
                .organizationId(organizationId)
                .organizationName(organizationName)
                .organizationRole(organizationRole)
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .phone(user.getPhone())
                .gender(user.getGender())
                .streetAddress(user.getStreetAddress())
                .wardCode(user.getWardCode())
                .wardName(user.getWardName())
                .provinceCode(user.getProvinceCode())
                .provinceName(user.getProvinceName())
                .status(user.getStatus().name())
                .emailVerified(user.isEmailVerified())
                .roles(user.getRoles().stream().map(r -> r.getCode()).toList())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
