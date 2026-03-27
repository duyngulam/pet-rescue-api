package com.uit.petrescueapi.application.dto.media;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Response DTO for temp media upload.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response for media upload operation")
public class MediaUploadResponseDto {

    @Schema(description = "Media file ID", example = "550e8400-e29b-41d4-a716-446655440000")
    private UUID mediaId;

    @Schema(description = "Cloudinary public ID", example = "temp/pets/abc123")
    private String publicId;

    @Schema(description = "Full URL to the uploaded file")
    private String url;

    @Schema(description = "Resource type", example = "image")
    private String resourceType;

    @Schema(description = "File format", example = "jpg")
    private String format;

    @Schema(description = "Image/video width in pixels")
    private Integer width;

    @Schema(description = "Image/video height in pixels")
    private Integer height;

    @Schema(description = "File size in bytes")
    private Integer bytes;

    @Schema(description = "Upload status", example = "TEMP", allowableValues = {"TEMP", "PERMANENT"})
    private String status;

    @Schema(description = "Upload timestamp")
    private LocalDateTime createdAt;
}
