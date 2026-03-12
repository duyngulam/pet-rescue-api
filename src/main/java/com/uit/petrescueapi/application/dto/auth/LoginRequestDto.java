package com.uit.petrescueapi.application.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Request DTO for login.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Login request")
public class LoginRequestDto {

    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    @Schema(example = "john@example.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(example = "P@ssw0rd123")
    private String password;
}
