package com.uit.petrescueapi.application.port.in.command;

import com.uit.petrescueapi.application.dto.auth.LoginRequestDto;
import com.uit.petrescueapi.application.dto.auth.RefreshTokenRequestDto;
import com.uit.petrescueapi.application.dto.auth.RegisterRequestDto;
import com.uit.petrescueapi.application.dto.auth.AuthTokenResponseDto;

/**
 * Command (write) port for Authentication operations.
 * Handles registration, login and token refresh.
 */
public interface AuthCommandPort {

    AuthTokenResponseDto register(RegisterRequestDto cmd);

    AuthTokenResponseDto login(LoginRequestDto cmd);

    AuthTokenResponseDto refreshToken(RefreshTokenRequestDto cmd);
}
