package com.uit.petrescueapi.presentation.controller.mock;

import com.uit.petrescueapi.application.dto.user.UserResponseDto;
import com.uit.petrescueapi.application.dto.user.UserReputationResponseDto;
import com.uit.petrescueapi.application.dto.user.UserSummaryResponseDto;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import com.uit.petrescueapi.presentation.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Mock user management controller.
 */
@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "Users", description = "User management")
public class MockUserController {

    private static final UUID ID1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");
    private static final UUID ID2 = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");

    @GetMapping
    @Operation(summary = "List users (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<UserSummaryResponseDto>>> list(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "20") int size) {

        List<UserSummaryResponseDto> users = List.of(
                UserSummaryResponseDto.builder()
                        .userId(ID1).username("johndoe").email("johndoe@example.com").status("ACTIVE").build(),
                UserSummaryResponseDto.builder()
                        .userId(ID2).username("janedoe").email("janedoe@example.com").status("ACTIVE").build());
        PageResponse<UserSummaryResponseDto> pr = PageResponse.<UserSummaryResponseDto>builder()
                .content(users).page(page).size(size)
                .totalElements(2).totalPages(1).last(true)
                .build();
        return ResponseEntity.ok(ApiResponse.ok(pr));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public ResponseEntity<ApiResponse<UserResponseDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(sampleUser(id, "johndoe")));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update user profile")
    public ResponseEntity<ApiResponse<UserResponseDto>> update(@PathVariable UUID id,
                                                        @RequestBody UserResponseDto dto) {
        return ResponseEntity.ok(ApiResponse.ok(sampleUser(id, dto.getUsername())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deactivate user")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(null, "User deactivated"));
    }

    @GetMapping("/{id}/reputation")
    @Operation(summary = "Get user reputation / score")
    public ResponseEntity<ApiResponse<UserReputationResponseDto>> reputation(@PathVariable UUID id) {
        UserReputationResponseDto rep = UserReputationResponseDto.builder()
                .userId(id).score(150).level("GOLD")
                .updatedAt(LocalDateTime.now())
                .build();
        return ResponseEntity.ok(ApiResponse.ok(rep));
    }

    private UserResponseDto sampleUser(UUID id, String username) {
        return UserResponseDto.builder()
                .userId(id).username(username)
                .email(username + "@example.com")
                .status("ACTIVE").roles(List.of("USER"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
