package com.uit.petrescueapi.presentation.controller;

import com.uit.petrescueapi.application.dto.auth.AuthTokenResponseDto;
import com.uit.petrescueapi.application.dto.auth.LoginRequestDto;
import com.uit.petrescueapi.application.dto.auth.RefreshTokenRequestDto;
import com.uit.petrescueapi.application.dto.auth.RegisterRequestDto;
import com.uit.petrescueapi.application.dto.user.UserResponseDto;
import com.uit.petrescueapi.application.port.command.AuthCommandPort;
import com.uit.petrescueapi.application.port.query.AuthQueryPort;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * REST controller for authentication & user registration.
 *
 * <p>Uses CQRS:
 * <ul>
 *   <li>Commands (write) go through {@link AuthCommandPort}</li>
 *   <li>Queries (read) go through {@link AuthQueryPort}</li>
 * </ul>
 *
 * <p>Public endpoints (no JWT required):
 * <ul>
 *   <li>POST /register</li>
 *   <li>POST /login</li>
 *   <li>POST /refresh</li>
 *   <li>GET  /verify-email</li>
 *   <li>POST /resend-verification</li>
 * </ul>
 *
 * <p>Protected endpoints (JWT required):
 * <ul>
 *   <li>POST /logout</li>
 *   <li>GET  /me</li>
 * </ul>
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Authentication & registration")
@RequiredArgsConstructor
public class AuthController {

    private final AuthCommandPort authCommandPort;
    private final AuthQueryPort authQueryPort;

    // ── Public endpoints ────────────────────────

    @PostMapping("/register")
    @Operation(summary = "Register a new account", description = "Creates the user and sends a verification email")
    public ResponseEntity<ApiResponse<AuthTokenResponseDto>> register(
            @Valid @RequestBody RegisterRequestDto req) {
        AuthTokenResponseDto result = authCommandPort.register(req);
        return ResponseEntity.status(201).body(ApiResponse.created(result));
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email & password")
    public ResponseEntity<ApiResponse<AuthTokenResponseDto>> login(
            @Valid @RequestBody LoginRequestDto req) {
        AuthTokenResponseDto result = authCommandPort.login(req);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token (silent refresh)")
    public ResponseEntity<ApiResponse<AuthTokenResponseDto>> refresh(
            @Valid @RequestBody RefreshTokenRequestDto req) {
        AuthTokenResponseDto result = authCommandPort.refreshToken(req);
        return ResponseEntity.ok(ApiResponse.ok(result));
    }

    @GetMapping("/verify-email")
    @Operation(summary = "Verify email via token", description = "Token is sent to the user's email on registration")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestParam String token) {
        authCommandPort.verifyEmail(token);
        return ResponseEntity.ok(ApiResponse.ok(null, "Email verified successfully"));
    }

    @PostMapping("/resend-verification")
    @Operation(summary = "Resend verification email")
    public ResponseEntity<ApiResponse<Void>> resendVerification(@RequestParam String email) {
        authCommandPort.resendVerificationEmail(email);
        return ResponseEntity.ok(ApiResponse.ok(null, "Verification email sent"));
    }

    // ── Protected endpoints ─────────────────────

    @PostMapping("/logout")
    @Operation(summary = "Logout — revoke all refresh tokens")
    public ResponseEntity<ApiResponse<Void>> logout(Authentication authentication) {
        authCommandPort.logout(authentication.getName());
        return ResponseEntity.ok(ApiResponse.ok(null, "Logged out successfully"));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<ApiResponse<UserResponseDto>> me(Authentication authentication) {
        UUID userId = UUID.fromString(authentication.getName());
        UserResponseDto dto = authQueryPort.findUserById(userId);
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }
}
