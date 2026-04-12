package com.uit.petrescueapi.infrastructure.messaging;

import com.uit.petrescueapi.domain.event.CommentCreatedEvent;
import com.uit.petrescueapi.domain.event.CommentLikedEvent;
import com.uit.petrescueapi.domain.event.CommentUnlikedEvent;
import com.uit.petrescueapi.infrastructure.persistence.repository.CommentJpaRepository;
import com.uit.petrescueapi.infrastructure.persistence.repository.PostJpaRepository;
import com.uit.petrescueapi.infrastructure.redis.RedisCounterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommentEventListener {

    private final RedisCounterService redisCounterService;
    private final CommentJpaRepository commentJpaRepository;
    private final PostJpaRepository postJpaRepository;

    @RabbitListener(queues = "${app.rabbitmq.queue.comment-created}")
    @Transactional
    public void handleCommentCreated(CommentCreatedEvent event) {
        redisCounterService.incrementPostCommentCount(event.getPostId());
        postJpaRepository.incrementCommentCount(event.getPostId());

        if (event.getParentCommentId() != null) {
            redisCounterService.incrementCommentReplyCount(event.getParentCommentId());
            commentJpaRepository.incrementReplyCount(event.getParentCommentId());
        }

        log.debug("Handled CommentCreatedEvent: commentId={}, postId={}, parentCommentId={}",
                event.getCommentId(), event.getPostId(), event.getParentCommentId());
    }

    @RabbitListener(queues = "${app.rabbitmq.queue.comment-liked}")
    @Transactional
    public void handleCommentLiked(CommentLikedEvent event) {
        boolean added = redisCounterService.incrementCommentLikes(event.getCommentId(), event.getUserId());
        if (added) {
            commentJpaRepository.incrementLikeCount(event.getCommentId());
        }

        log.debug("Handled CommentLikedEvent: commentId={}, userId={}, redisAdded={}",
                event.getCommentId(), event.getUserId(), added);
    }

    @RabbitListener(queues = "${app.rabbitmq.queue.comment-unliked}")
    @Transactional
    public void handleCommentUnliked(CommentUnlikedEvent event) {
        boolean removed = redisCounterService.decrementCommentLikes(event.getCommentId(), event.getUserId());
        if (removed) {
            commentJpaRepository.decrementLikeCount(event.getCommentId());
        }

        log.debug("Handled CommentUnlikedEvent: commentId={}, userId={}, redisRemoved={}",
                event.getCommentId(), event.getUserId(), removed);
    }
}
