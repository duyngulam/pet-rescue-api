package com.uit.petrescueapi.application.dto.auth;

import lombok.*;

/**
 * Request DTO for login.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    private String email;
    private String password;
}
