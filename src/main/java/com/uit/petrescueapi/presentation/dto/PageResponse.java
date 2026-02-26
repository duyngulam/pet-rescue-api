package com.uit.petrescueapi.presentation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Paginated wrapper carried inside {@link ApiResponse#getData()}.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Paginated result set")
public class PageResponse<T> {

    @Schema(description = "Items on this page")
    private List<T> content;

    @Schema(description = "Current page number (0-based)", example = "0")
    private int page;

    @Schema(description = "Page size", example = "20")
    private int size;

    @Schema(description = "Total elements across all pages", example = "42")
    private long totalElements;

    @Schema(description = "Total pages", example = "3")
    private int totalPages;

    @Schema(description = "Is this the last page?", example = "false")
    private boolean last;
}
