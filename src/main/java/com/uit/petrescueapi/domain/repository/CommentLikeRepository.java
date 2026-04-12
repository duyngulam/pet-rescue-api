package com.uit.petrescueapi.domain.repository;

import com.uit.petrescueapi.domain.entity.CommentLike;

import java.util.UUID;

/**
 * Domain repository contract for CommentLike writes and existence checks.
 */
public interface CommentLikeRepository {

    CommentLike save(CommentLike commentLike);

    boolean exists(UUID commentId, UUID userId);

    void delete(UUID commentId, UUID userId);
}
