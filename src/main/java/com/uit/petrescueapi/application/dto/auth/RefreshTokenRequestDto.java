package com.uit.petrescueapi.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Request DTO for refreshing an access token.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Refresh token request")
public class RefreshTokenRequestDto {

    @NotBlank(message = "Refresh token is required")
    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    private String refreshToken;
}
