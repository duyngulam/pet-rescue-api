package com.uit.petrescueapi.presentation.controller;

import com.uit.petrescueapi.application.dto.media.MediaFileResponseDto;
import com.uit.petrescueapi.application.port.command.MediaCommandPort;
import com.uit.petrescueapi.application.port.query.MediaQueryPort;
import com.uit.petrescueapi.presentation.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/media")
@RequiredArgsConstructor
@Slf4j
@io.swagger.v3.oas.annotations.tags.Tag(name = "Media", description = "Media file management")
public class MediaController {

    private final MediaCommandPort commandPort;
    private final MediaQueryPort queryPort;

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
