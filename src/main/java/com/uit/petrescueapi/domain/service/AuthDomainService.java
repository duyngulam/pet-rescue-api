package com.uit.petrescueapi.domain.service;

import com.uit.petrescueapi.domain.entity.EmailVerificationToken;
import com.uit.petrescueapi.domain.entity.RefreshToken;
import com.uit.petrescueapi.domain.entity.Role;
import com.uit.petrescueapi.domain.entity.User;
import com.uit.petrescueapi.domain.exception.BusinessException;
import com.uit.petrescueapi.domain.exception.ResourceAlreadyExistsException;
import com.uit.petrescueapi.domain.exception.ResourceNotFoundException;
import com.uit.petrescueapi.domain.exception.UnauthorizedException;
import com.uit.petrescueapi.domain.repository.EmailVerificationTokenRepository;
import com.uit.petrescueapi.domain.repository.RefreshTokenRepository;
import com.uit.petrescueapi.domain.repository.RoleRepository;
import com.uit.petrescueapi.domain.repository.UserRepository;
import com.uit.petrescueapi.domain.valueobject.SystemRole;
import com.uit.petrescueapi.domain.valueobject.UserStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Domain service for authentication & user-registration business rules.
 *
 * Responsibilities:
 * <ul>
 *   <li>Validate uniqueness of email / username</li>
 *   <li>Assign default role on registration</li>
 *   <li>Email-verification flow (create token → verify)</li>
 *   <li>Refresh-token lifecycle (create / rotate / revoke)</li>
 * </ul>
 *
 * Password hashing and JWT creation are <strong>NOT</strong> handled here — those
 * are infrastructure concerns injected via the application layer.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthDomainService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final EmailVerificationTokenRepository verificationTokenRepository;

    // ── Registration ────────────────────────────

    /**
     * Create a new user entity with the default USER role.
     * The password is expected to be pre-hashed by the caller.
     */
    public User registerUser(String username, String email, String hashedPassword,
                              String fullName, String phone, String gender,
                              String streetAddress, String wardCode, String wardName,
                              String provinceCode, String provinceName) {
        if (userRepository.existsByEmail(email)) {
            throw new ResourceAlreadyExistsException("User", "email", email);
        }
        if (userRepository.existsByUsername(username)) {
            throw new ResourceAlreadyExistsException("User", "username", username);
        }

        Role defaultRole = roleRepository.findByCode(SystemRole.USER.name())
                .orElseThrow(() -> new BusinessException("Default role USER not found. Run DB migration."));

        User user = User.builder()
                .id(UUID.randomUUID())
                .username(username)
                .email(email)
                .passwordHash(hashedPassword)
                .fullName(fullName)
                .phone(phone)
                .gender(gender)
                .streetAddress(streetAddress)
                .wardCode(wardCode)
                .wardName(wardName)
                .provinceCode(provinceCode)
                .provinceName(provinceName)
                .status(UserStatus.PENDING_VERIFICATION)
                .emailVerified(false)
                .roles(Set.of(defaultRole))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        User saved = userRepository.save(user);
        log.info("Registered user {} ({})", saved.getUsername(), saved.getId());
        return saved;
    }

    // ── Email verification ──────────────────────

    /**
     * Create a one-time verification token for the given user.
     */
    public EmailVerificationToken createVerificationToken(UUID userId, long expiryMinutes) {
        EmailVerificationToken token = EmailVerificationToken.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusMinutes(expiryMinutes))
                .used(false)
                .createdAt(LocalDateTime.now())
                .build();
        return verificationTokenRepository.save(token);
    }

    /**
     * Verify the email of a user given a raw token string.
     */
    public User verifyEmail(String rawToken) {
        EmailVerificationToken token = verificationTokenRepository.findByToken(rawToken)
                .orElseThrow(() -> new BusinessException("Invalid verification token"));

        if (!token.isUsable()) {
            throw new BusinessException("Verification token is expired or already used");
        }

        token.markUsed();
        verificationTokenRepository.save(token);

        User user = userRepository.findById(token.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", token.getUserId()));
        user.verifyEmail();
        return userRepository.save(user);
    }

    /**
     * Re-send: create a fresh verification token for a user who has not verified yet.
     */
    public EmailVerificationToken resendVerificationToken(String email, long expiryMinutes) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));

        if (user.isEmailVerified()) {
            throw new BusinessException("Email is already verified");
        }
        return createVerificationToken(user.getId(), expiryMinutes);
    }

    // ── Refresh tokens ──────────────────────────

    public RefreshToken createRefreshToken(UUID userId, long expiryDays) {
        RefreshToken token = RefreshToken.builder()
                .id(UUID.randomUUID())
                .userId(userId)
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusDays(expiryDays))
                .revoked(false)
                .createdAt(LocalDateTime.now())
                .build();
        return refreshTokenRepository.save(token);
    }

    /**
     * Rotate: validate old refresh token, revoke it, issue a new one.
     */
    public RefreshToken rotateRefreshToken(String rawToken, long expiryDays) {
        RefreshToken existing = refreshTokenRepository.findByToken(rawToken)
                .orElseThrow(() -> new UnauthorizedException("Invalid refresh token"));

        if (!existing.isUsable()) {
            // Possible token reuse attack — revoke all user tokens
            refreshTokenRepository.revokeAllByUserId(existing.getUserId());
            throw new UnauthorizedException("Refresh token expired or revoked. All sessions invalidated.");
        }

        existing.revoke();
        refreshTokenRepository.save(existing);

        return createRefreshToken(existing.getUserId(), expiryDays);
    }

    public void revokeAllTokens(UUID userId) {
        refreshTokenRepository.revokeAllByUserId(userId);
        log.info("Revoked all refresh tokens for user {}", userId);
    }

    // ── Lookups ─────────────────────────────────

    @Transactional(readOnly = true)
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }

    @Transactional(readOnly = true)
    public User findById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Transactional(readOnly = true)
    public User findByEmailOrUsername(String emailOrUsername) {
        // Try email first
        Optional<User> byEmail = userRepository.findByEmail(emailOrUsername);
        if (byEmail.isPresent()) {
            return byEmail.get();
        }
        // Try username
        return userRepository.findByUsername(emailOrUsername)
                .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));
    }
}
