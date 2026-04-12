package com.uit.petrescueapi.infrastructure.persistence.repository;

import com.uit.petrescueapi.infrastructure.persistence.entity.CommentLikeId;
import com.uit.petrescueapi.infrastructure.persistence.entity.CommentLikeJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentLikeJpaRepository extends JpaRepository<CommentLikeJpaEntity, CommentLikeId> {

    boolean existsByCommentIdAndUserId(UUID commentId, UUID userId);

    void deleteByCommentIdAndUserId(UUID commentId, UUID userId);
}
