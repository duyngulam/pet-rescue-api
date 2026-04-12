package com.uit.petrescueapi.domain.repository;

import com.uit.petrescueapi.domain.entity.PostLike;

import java.util.UUID;

/**
 * Domain repository contract for PostLike writes and existence checks.
 */
public interface PostLikeRepository {

    PostLike save(PostLike postLike);

    boolean exists(UUID postId, UUID userId);

    void delete(UUID postId, UUID userId);
}
