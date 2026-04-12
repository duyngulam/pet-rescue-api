package com.uit.petrescueapi.infrastructure.messaging;

import com.uit.petrescueapi.domain.event.CommentCreatedEvent;
import com.uit.petrescueapi.domain.event.CommentLikedEvent;
import com.uit.petrescueapi.domain.event.CommentUnlikedEvent;
import com.uit.petrescueapi.domain.event.PostLikedEvent;
import com.uit.petrescueapi.domain.event.PostUnlikedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class EventPublisherTest {

    private RabbitTemplate rabbitTemplate;
    private EventPublisher publisher;

    @BeforeEach
    void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        publisher = new EventPublisher(rabbitTemplate);
        ReflectionTestUtils.setField(publisher, "socialExchange", "social.exchange");
        ReflectionTestUtils.setField(publisher, "postLikedRoutingKey", "post.liked");
        ReflectionTestUtils.setField(publisher, "postUnlikedRoutingKey", "post.unliked");
        ReflectionTestUtils.setField(publisher, "commentCreatedRoutingKey", "comment.created");
        ReflectionTestUtils.setField(publisher, "commentLikedRoutingKey", "comment.liked");
        ReflectionTestUtils.setField(publisher, "commentUnlikedRoutingKey", "comment.unliked");
    }

    @Test
    void publishPostLikedSendsToExpectedRoutingKey() {
        PostLikedEvent event = PostLikedEvent.builder()
                .postId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .build();
        publisher.publishPostLiked(event);
        verify(rabbitTemplate).convertAndSend("social.exchange", "post.liked", event);
    }

    @Test
    void publishPostUnlikedSendsToExpectedRoutingKey() {
        PostUnlikedEvent event = PostUnlikedEvent.builder()
                .postId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .build();
        publisher.publishPostUnliked(event);
        verify(rabbitTemplate).convertAndSend("social.exchange", "post.unliked", event);
    }

    @Test
    void publishCommentCreatedSendsToExpectedRoutingKey() {
        CommentCreatedEvent event = CommentCreatedEvent.builder()
                .commentId(UUID.randomUUID())
                .postId(UUID.randomUUID())
                .parentCommentId(null)
                .authorId(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .build();
        publisher.publishCommentCreated(event);
        verify(rabbitTemplate).convertAndSend("social.exchange", "comment.created", event);
    }

    @Test
    void publishCommentLikedSendsToExpectedRoutingKey() {
        CommentLikedEvent event = CommentLikedEvent.builder()
                .commentId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .build();
        publisher.publishCommentLiked(event);
        verify(rabbitTemplate).convertAndSend("social.exchange", "comment.liked", event);
    }

    @Test
    void publishCommentUnlikedSendsToExpectedRoutingKey() {
        CommentUnlikedEvent event = CommentUnlikedEvent.builder()
                .commentId(UUID.randomUUID())
                .userId(UUID.randomUUID())
                .timestamp(LocalDateTime.now())
                .build();
        publisher.publishCommentUnliked(event);
        verify(rabbitTemplate).convertAndSend("social.exchange", "comment.unliked", event);
    }
}
