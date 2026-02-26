package com.uit.petrescueapi.application.dto.auth;

import lombok.*;

/**
 * Request DTO for user registration.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequestDto {

    private String username;
    private String email;
    private String password;
}
