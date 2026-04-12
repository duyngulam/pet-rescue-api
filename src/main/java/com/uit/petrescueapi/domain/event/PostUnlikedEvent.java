package com.uit.petrescueapi.domain.event;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
@Jacksonized
public class PostUnlikedEvent {
    UUID postId;
    UUID userId;
    LocalDateTime timestamp;
}
