package com.uit.petrescueapi.presentation.controller.mock;

import com.uit.petrescueapi.application.dto.tag.CreateTagRequestDto;
import com.uit.petrescueapi.application.dto.tag.TagResponseDto;
import com.uit.petrescueapi.application.dto.tag.TagSummaryResponseDto;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import com.uit.petrescueapi.presentation.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Mock tag controller for post categorization.
 */
@RestController
@RequestMapping("/api/v1/tags")
@Tag(name = "Tags", description = "Post tags / categories")
public class MockTagController {

    @GetMapping
    @Operation(summary = "List all tags (paginated)")
    public ResponseEntity<ApiResponse<PageResponse<TagSummaryResponseDto>>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        List<TagSummaryResponseDto> tags = List.of(
                TagSummaryResponseDto.builder().tagId(UUID.randomUUID()).code("rescue-story").name("Rescue Story").build(),
                TagSummaryResponseDto.builder().tagId(UUID.randomUUID()).code("adoption").name("Adoption").build(),
                TagSummaryResponseDto.builder().tagId(UUID.randomUUID()).code("urgent").name("Urgent").build(),
                TagSummaryResponseDto.builder().tagId(UUID.randomUUID()).code("lost-pet").name("Lost Pet").build(),
                TagSummaryResponseDto.builder().tagId(UUID.randomUUID()).code("found-pet").name("Found Pet").build(),
                TagSummaryResponseDto.builder().tagId(UUID.randomUUID()).code("volunteer").name("Volunteer").build());
        PageResponse<TagSummaryResponseDto> pr = PageResponse.<TagSummaryResponseDto>builder()
                .content(tags).page(page).size(size)
                .totalElements(6).totalPages(1).last(true)
                .build();
        return ResponseEntity.ok(ApiResponse.ok(pr));
    }

    @PostMapping
    @Operation(summary = "Create a new tag")
    public ResponseEntity<ApiResponse<TagResponseDto>> create(@RequestBody CreateTagRequestDto req) {
        return ResponseEntity.status(201).body(
                ApiResponse.created(sampleTag(req.getCode(), req.getName())));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a tag")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(null, "Tag deleted"));
    }

    private TagResponseDto sampleTag(String code, String name) {
        return TagResponseDto.builder()
                .tagId(UUID.randomUUID()).code(code).name(name)
                .description("Posts about " + name.toLowerCase())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
