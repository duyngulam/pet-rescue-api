package com.uit.petrescueapi.application.dto.auth;

import com.uit.petrescueapi.application.dto.user.UserResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * Response DTO for authentication tokens.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Auth tokens response")
public class AuthTokenResponseDto {

    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String accessToken;

    @Schema(example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

    @Schema(example = "Bearer")
    private String tokenType;

    @Schema(example = "3600", description = "Token expiry in seconds")
    private Long expiresIn;

    private UserResponseDto user;
}
