package com.uit.petrescueapi.presentation.controller.mock;

import com.uit.petrescueapi.application.dto.media.MediaFileResponseDto;
import com.uit.petrescueapi.application.dto.post.CreatePostRequestDto;
import com.uit.petrescueapi.application.dto.post.PostResponseDto;
import com.uit.petrescueapi.application.dto.post.PostSummaryResponseDto;
import com.uit.petrescueapi.application.dto.post.UpdatePostRequestDto;
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
 * Mock community post controller.
 */
@RestController
@RequestMapping("/api/v1/posts")
@Tag(name = "Posts", description = "Community posts & social feed")
public class MockPostController {

    private static final UUID POST1 = UUID.fromString("e50e8400-e29b-41d4-a716-446655440000");
    private static final UUID USER1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

    @GetMapping
    @Operation(summary = "List posts (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<PostSummaryResponseDto>>> list(
            @Parameter(description = "Filter by tag code") @RequestParam(required = false) String tag,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        List<PostSummaryResponseDto> posts = List.of(
                PostSummaryResponseDto.builder()
                        .postId(POST1).authorUsername("johndoe")
                        .content("Found this little guy near the park, he needs help!")
                        .tags(List.of("rescue-story", "urgent"))
                        .createdAt(LocalDateTime.now()).build());
        PageResponse<PostSummaryResponseDto> pr = PageResponse.<PostSummaryResponseDto>builder()
                .content(posts).page(page).size(size)
                .totalElements(1).totalPages(1).last(true)
                .build();
        return ResponseEntity.ok(ApiResponse.ok(pr));
    }

    @PostMapping
    @Operation(summary = "Create a new post")
    public ResponseEntity<ApiResponse<PostResponseDto>> create(@RequestBody CreatePostRequestDto req) {
        PostResponseDto dto = samplePost(UUID.randomUUID());
        dto.setContent(req.getContent());
        dto.setRescueCaseId(req.getRescueCaseId());
        return ResponseEntity.status(201).body(ApiResponse.created(dto));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get post by ID")
    public ResponseEntity<ApiResponse<PostResponseDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(samplePost(id)));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a post")
    public ResponseEntity<ApiResponse<PostResponseDto>> update(@PathVariable UUID id,
                                                        @RequestBody UpdatePostRequestDto req) {
        PostResponseDto dto = samplePost(id);
        dto.setContent(req.getContent());
        return ResponseEntity.ok(ApiResponse.ok(dto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a post")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(null, "Post deleted"));
    }

    private PostResponseDto samplePost(UUID id) {
        MediaFileResponseDto media = MediaFileResponseDto.builder()
                .mediaId(UUID.randomUUID()).uploaderId(USER1)
                .url("https://storage.example.com/posts/rescue-01.jpg")
                .type("IMAGE").createdAt(LocalDateTime.now())
                .build();

        return PostResponseDto.builder()
                .postId(id).authorId(USER1).authorUsername("johndoe")
                .content("Found this little guy near the park, he needs help!")
                .media(List.of(media))
                .tags(List.of("rescue-story", "urgent"))
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
