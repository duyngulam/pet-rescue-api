package com.uit.petrescueapi.application.usecase;

import com.uit.petrescueapi.application.dto.like.LikeStatusDto;
import com.uit.petrescueapi.application.port.command.LikeCommandPort;
import com.uit.petrescueapi.domain.event.CommentLikedEvent;
import com.uit.petrescueapi.domain.event.CommentUnlikedEvent;
import com.uit.petrescueapi.domain.event.PostLikedEvent;
import com.uit.petrescueapi.domain.event.PostUnlikedEvent;
import com.uit.petrescueapi.domain.service.LikeDomainService;
import com.uit.petrescueapi.infrastructure.messaging.EventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeCommandUseCase implements LikeCommandPort {

    private final LikeDomainService likeDomainService;
    private final EventPublisher eventPublisher;

    @Override
    public LikeStatusDto likePost(UUID postId, UUID userId) {
        boolean changed = likeDomainService.likePost(postId, userId);
        if (changed) {
            eventPublisher.publishPostLiked(PostLikedEvent.builder()
                    .postId(postId)
                    .userId(userId)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
        return LikeStatusDto.builder().targetId(postId).liked(true).build();
    }

    @Override
    public LikeStatusDto unlikePost(UUID postId, UUID userId) {
        boolean changed = likeDomainService.unlikePost(postId, userId);
        if (changed) {
            eventPublisher.publishPostUnliked(PostUnlikedEvent.builder()
                    .postId(postId)
                    .userId(userId)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
        return LikeStatusDto.builder().targetId(postId).liked(false).build();
    }

    @Override
    public LikeStatusDto likeComment(UUID commentId, UUID userId) {
        boolean changed = likeDomainService.likeComment(commentId, userId);
        if (changed) {
            eventPublisher.publishCommentLiked(CommentLikedEvent.builder()
                    .commentId(commentId)
                    .userId(userId)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
        return LikeStatusDto.builder().targetId(commentId).liked(true).build();
    }

    @Override
    public LikeStatusDto unlikeComment(UUID commentId, UUID userId) {
        boolean changed = likeDomainService.unlikeComment(commentId, userId);
        if (changed) {
            eventPublisher.publishCommentUnliked(CommentUnlikedEvent.builder()
                    .commentId(commentId)
                    .userId(userId)
                    .timestamp(LocalDateTime.now())
                    .build());
        }
        return LikeStatusDto.builder().targetId(commentId).liked(false).build();
    }
}
