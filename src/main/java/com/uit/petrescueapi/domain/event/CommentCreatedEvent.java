package com.uit.petrescueapi.domain.event;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
@Jacksonized
public class CommentCreatedEvent {
    UUID commentId;
    UUID postId;
    UUID parentCommentId;
    UUID authorId;
    LocalDateTime timestamp;
}
