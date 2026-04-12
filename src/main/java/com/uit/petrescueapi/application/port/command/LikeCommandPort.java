package com.uit.petrescueapi.application.port.command;

import com.uit.petrescueapi.application.dto.like.LikeStatusDto;

import java.util.UUID;

public interface LikeCommandPort {
    LikeStatusDto likePost(UUID postId, UUID userId);
    LikeStatusDto unlikePost(UUID postId, UUID userId);
    LikeStatusDto likeComment(UUID commentId, UUID userId);
    LikeStatusDto unlikeComment(UUID commentId, UUID userId);
}
