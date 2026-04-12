package com.uit.petrescueapi.application.usecase;

import com.uit.petrescueapi.domain.service.LikeDomainService;
import com.uit.petrescueapi.infrastructure.messaging.EventPublisher;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

class LikeCommandUseCaseTest {

    @Test
    void likePostPublishesEventWhenStateChanged() {
        LikeDomainService domain = mock(LikeDomainService.class);
        EventPublisher publisher = mock(EventPublisher.class);
        LikeCommandUseCase useCase = new LikeCommandUseCase(domain, publisher);

        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(domain.likePost(postId, userId)).thenReturn(true);

        useCase.likePost(postId, userId);

        verify(publisher).publishPostLiked(any());
    }

    @Test
    void likePostDoesNotPublishWhenDuplicate() {
        LikeDomainService domain = mock(LikeDomainService.class);
        EventPublisher publisher = mock(EventPublisher.class);
        LikeCommandUseCase useCase = new LikeCommandUseCase(domain, publisher);

        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(domain.likePost(postId, userId)).thenReturn(false);

        useCase.likePost(postId, userId);

        verify(publisher, never()).publishPostLiked(any());
    }
}
