package com.uit.petrescueapi.presentation.controller;

import com.uit.petrescueapi.application.dto.media.MediaFileResponseDto;
import com.uit.petrescueapi.application.dto.media.MediaUploadResponseDto;
import com.uit.petrescueapi.application.port.command.MediaCommandPort;
import com.uit.petrescueapi.application.port.out.CloudStoragePort;
import com.uit.petrescueapi.application.port.query.MediaQueryPort;
import com.uit.petrescueapi.domain.entity.MediaFile;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
@Slf4j
@io.swagger.v3.oas.annotations.tags.Tag(name = "Media", description = "Media file management")
public class MediaController {

    private final MediaCommandPort commandPort;
    private final MediaQueryPort queryPort;
    private final CloudStoragePort cloudStoragePort;

    @PostMapping(value = "/upload/temp", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload a file to temporary storage",
               description = "Uploads a file to Cloudinary temp folder. Returns media ID to use when creating entities.")
    public ResponseEntity<ApiResponse<MediaUploadResponseDto>> uploadTemp(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "folder", required = false) String subFolder,
            Authentication authentication) {

        UUID userId = UUID.fromString(authentication.getName());
        log.info("Uploading temp file for user {}", userId);
        MediaFile mediaFile = commandPort.uploadTemp(file, userId, subFolder);

        MediaUploadResponseDto response = MediaUploadResponseDto.builder()
                .mediaId(mediaFile.getMediaId())
                .publicId(mediaFile.getPublicId())
                .url(cloudStoragePort.buildUrl(mediaFile.getPublicId()))
                .resourceType(mediaFile.getResourceType())
                .format(mediaFile.getFormat())
                .width(mediaFile.getWidth())
                .height(mediaFile.getHeight())
                .bytes(mediaFile.getBytes())
                .status(mediaFile.getStatus())
                .createdAt(mediaFile.getCreatedAt())
                .build();

        return ResponseEntity.ok(ApiResponse.created(response, "File uploaded to temporary storage"));
    }

    @PostMapping("/{id}/confirm")
    @Operation(summary = "Confirm a temp upload", 
               description = "Moves a temp upload to permanent storage. Called after entity is successfully created.")
    public ResponseEntity<ApiResponse<MediaUploadResponseDto>> confirmUpload(
            @PathVariable UUID id,
            @RequestParam(value = "folder", required = false) String targetFolder) {
        
        log.info("Confirming upload for media {}", id);
        MediaFile mediaFile = commandPort.confirmUpload(id, targetFolder);
        
        MediaUploadResponseDto response = MediaUploadResponseDto.builder()
                .mediaId(mediaFile.getMediaId())
                .publicId(mediaFile.getPublicId())
                .url(cloudStoragePort.buildUrl(mediaFile.getPublicId()))
                .resourceType(mediaFile.getResourceType())
                .format(mediaFile.getFormat())
                .width(mediaFile.getWidth())
                .height(mediaFile.getHeight())
                .bytes(mediaFile.getBytes())
                .status(mediaFile.getStatus())
                .createdAt(mediaFile.getCreatedAt())
                .build();
        
        return ResponseEntity.ok(ApiResponse.ok(response, "Upload confirmed"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a media file")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable UUID id) {
        commandPort.delete(id);
        return ResponseEntity.ok(ApiResponse.noContent());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get media file by ID")
    public ResponseEntity<ApiResponse<MediaFileResponseDto>> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(ApiResponse.ok(queryPort.findById(id)));
    }
}
