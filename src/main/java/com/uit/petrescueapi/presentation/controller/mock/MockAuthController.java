package com.uit.petrescueapi.presentation.controller.mock;

import com.uit.petrescueapi.application.dto.auth.AuthTokenResponseDto;
import com.uit.petrescueapi.application.dto.auth.LoginRequestDto;
import com.uit.petrescueapi.application.dto.auth.RefreshTokenRequestDto;
import com.uit.petrescueapi.application.dto.auth.RegisterRequestDto;
import com.uit.petrescueapi.application.dto.user.UserResponseDto;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Mock authentication controller — returns hardcoded tokens.
 * Replace with real implementation later.
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth", description = "Authentication & registration")
public class MockAuthController {

    private static final UUID SAMPLE_USER_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

    // ── Endpoints ────────────────────────────────

    @PostMapping("/register")
    @Operation(summary = "Register a new account")
    public ResponseEntity<ApiResponse<AuthTokenResponseDto>> register(@RequestBody RegisterRequestDto req) {
        return ResponseEntity.status(201).body(ApiResponse.created(sampleToken()));
    }

    @PostMapping("/login")
    @Operation(summary = "Login with email & password")
    public ResponseEntity<ApiResponse<AuthTokenResponseDto>> login(@RequestBody LoginRequestDto req) {
        return ResponseEntity.ok(ApiResponse.ok(sampleToken()));
    }

    @PostMapping("/refresh")
    @Operation(summary = "Refresh access token")
    public ResponseEntity<ApiResponse<AuthTokenResponseDto>> refresh(@RequestBody RefreshTokenRequestDto req) {
        return ResponseEntity.ok(ApiResponse.ok(sampleToken()));
    }

    @PostMapping("/logout")
    @Operation(summary = "Invalidate the current token")
    public ResponseEntity<ApiResponse<Void>> logout() {
        return ResponseEntity.ok(ApiResponse.ok(null, "Logged out"));
    }

    @GetMapping("/me")
    @Operation(summary = "Get current user profile")
    public ResponseEntity<ApiResponse<UserResponseDto>> me() {
        return ResponseEntity.ok(ApiResponse.ok(sampleUser()));
    }

    // ── Sample data ──────────────────────────────

    private AuthTokenResponseDto sampleToken() {
        return AuthTokenResponseDto.builder()
                .accessToken("eyJhbGciOiJIUzI1NiJ9.mock-access-token")
                .refreshToken("eyJhbGciOiJIUzI1NiJ9.mock-refresh-token")
                .tokenType("Bearer")
                .expiresIn(3600L)
                .user(sampleUser())
                .build();
    }

    private UserResponseDto sampleUser() {
        return UserResponseDto.builder()
                .userId(SAMPLE_USER_ID)
                .username("johndoe")
                .email("john@example.com")
                .status("ACTIVE")
                .roles(List.of("USER"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
