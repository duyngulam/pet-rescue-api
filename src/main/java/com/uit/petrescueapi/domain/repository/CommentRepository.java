package com.uit.petrescueapi.domain.repository;

import com.uit.petrescueapi.domain.entity.Comment;

import java.util.Optional;
import java.util.UUID;

/**
 * Domain repository contract for the Comment aggregate.
 */
public interface CommentRepository {

    Comment save(Comment comment);

    Optional<Comment> findById(UUID commentId);
}
