package com.uit.petrescueapi.application.dto.auth;

import lombok.*;

/**
 * Request DTO for refreshing an access token.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequestDto {

    private String refreshToken;
}
