package com.uit.petrescueapi.infrastructure.messaging;

import com.uit.petrescueapi.domain.event.CommentCreatedEvent;
import com.uit.petrescueapi.domain.event.CommentLikedEvent;
import com.uit.petrescueapi.domain.event.CommentUnlikedEvent;
import com.uit.petrescueapi.domain.event.PostLikedEvent;
import com.uit.petrescueapi.domain.event.PostUnlikedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventPublisher {

    private final RabbitTemplate rabbitTemplate;

    @Value("${app.rabbitmq.exchange.social}")
    private String socialExchange;

    @Value("${app.rabbitmq.routing-key.post-liked}")
    private String postLikedRoutingKey;

    @Value("${app.rabbitmq.routing-key.post-unliked}")
    private String postUnlikedRoutingKey;

    @Value("${app.rabbitmq.routing-key.comment-created}")
    private String commentCreatedRoutingKey;

    @Value("${app.rabbitmq.routing-key.comment-liked}")
    private String commentLikedRoutingKey;

    @Value("${app.rabbitmq.routing-key.comment-unliked}")
    private String commentUnlikedRoutingKey;

    public void publishPostLiked(PostLikedEvent event) {
        rabbitTemplate.convertAndSend(socialExchange, postLikedRoutingKey, event);
    }

    public void publishPostUnliked(PostUnlikedEvent event) {
        rabbitTemplate.convertAndSend(socialExchange, postUnlikedRoutingKey, event);
    }

    public void publishCommentCreated(CommentCreatedEvent event) {
        rabbitTemplate.convertAndSend(socialExchange, commentCreatedRoutingKey, event);
    }

    public void publishCommentLiked(CommentLikedEvent event) {
        rabbitTemplate.convertAndSend(socialExchange, commentLikedRoutingKey, event);
    }

    public void publishCommentUnliked(CommentUnlikedEvent event) {
        rabbitTemplate.convertAndSend(socialExchange, commentUnlikedRoutingKey, event);
    }
}
