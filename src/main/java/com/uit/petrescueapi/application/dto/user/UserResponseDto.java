package com.uit.petrescueapi.application.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Response DTO for user information.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "User information")
public class UserResponseDto {

    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID userId;

    @Schema(example = "johndoe")
    private String username;

    @Schema(example = "john@example.com")
    private String email;

    @Schema(example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "BANNED"})
    private String status;

    private List<String> roles;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
