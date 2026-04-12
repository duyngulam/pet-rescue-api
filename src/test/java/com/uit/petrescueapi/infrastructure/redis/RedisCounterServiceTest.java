package com.uit.petrescueapi.infrastructure.redis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RedisCounterServiceTest {

    private StringRedisTemplate redisTemplate;
    private ZSetOperations<String, String> zSetOps;
    private ValueOperations<String, String> valueOps;
    private RedisCounterService service;

    @BeforeEach
    void setUp() {
        redisTemplate = mock(StringRedisTemplate.class);
        zSetOps = mock(ZSetOperations.class);
        valueOps = mock(ValueOperations.class);
        when(redisTemplate.opsForZSet()).thenReturn(zSetOps);
        when(redisTemplate.opsForValue()).thenReturn(valueOps);

        service = new RedisCounterService(redisTemplate);
        ReflectionTestUtils.setField(service, "postLikesKeyPattern", "post:%s:likes");
        ReflectionTestUtils.setField(service, "commentLikesKeyPattern", "comment:%s:likes");
        ReflectionTestUtils.setField(service, "postCommentCountKeyPattern", "post:%s:comment_count");
        ReflectionTestUtils.setField(service, "commentReplyCountKeyPattern", "comment:%s:reply_count");
    }

    @Test
    void incrementPostLikesReturnsTrueWhenAdded() {
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(zSetOps.addIfAbsent(eq(String.format("post:%s:likes", postId)), eq(userId.toString()), org.mockito.ArgumentMatchers.anyDouble()))
                .thenReturn(true);

        boolean added = service.incrementPostLikes(postId, userId);

        assertThat(added).isTrue();
    }

    @Test
    void incrementPostLikesReturnsFalseWhenAlreadyExists() {
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(zSetOps.addIfAbsent(eq(String.format("post:%s:likes", postId)), eq(userId.toString()), org.mockito.ArgumentMatchers.anyDouble()))
                .thenReturn(false);

        boolean added = service.incrementPostLikes(postId, userId);

        assertThat(added).isFalse();
    }

    @Test
    void checkUserLikedPostUsesZSetScore() {
        UUID postId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        when(zSetOps.score(String.format("post:%s:likes", postId), userId.toString())).thenReturn(1.0);

        boolean liked = service.checkUserLikedPost(postId, userId);

        assertThat(liked).isTrue();
    }

    @Test
    void getPostCommentCountReturnsZeroWhenMissing() {
        UUID postId = UUID.randomUUID();
        when(valueOps.get(anyString())).thenReturn(null);

        long count = service.getPostCommentCount(postId);

        assertThat(count).isZero();
    }

    @Test
    void getPostCommentCountParsesStoredValue() {
        UUID postId = UUID.randomUUID();
        when(valueOps.get(String.format("post:%s:comment_count", postId))).thenReturn("7");

        long count = service.getPostCommentCount(postId);

        assertThat(count).isEqualTo(7L);
    }
}
