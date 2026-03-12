package com.uit.petrescueapi.application.port.command;

import com.uit.petrescueapi.application.dto.auth.AuthTokenResponseDto;
import com.uit.petrescueapi.application.dto.auth.LoginRequestDto;
import com.uit.petrescueapi.application.dto.auth.RefreshTokenRequestDto;
import com.uit.petrescueapi.application.dto.auth.RegisterRequestDto;

/**
 * Command (write) port for Authentication operations.
 * Handles registration, login, token refresh, email verification and logout.
 */
public interface AuthCommandPort {

    /** Register a new user account and send a verification email. */
    AuthTokenResponseDto register(RegisterRequestDto cmd);

    /** Authenticate with email + password. */
    AuthTokenResponseDto login(LoginRequestDto cmd);

    /** Rotate refresh token (silent refresh). */
    AuthTokenResponseDto refreshToken(RefreshTokenRequestDto cmd);

    /** Verify email via one-time token. */
    void verifyEmail(String token);

    /** Re-send verification email. */
    void resendVerificationEmail(String email);

    /** Revoke all refresh tokens for the current user (logout). */
    void logout(String userId);
}
