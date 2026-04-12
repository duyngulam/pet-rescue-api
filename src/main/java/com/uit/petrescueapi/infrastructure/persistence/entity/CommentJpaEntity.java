package com.uit.petrescueapi.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * JPA entity mapped to the {@code comments} table.
 */
@Entity
@Table(name = "comments", indexes = {
        @Index(name = "idx_comments_post_id", columnList = "post_id, created_at"),
        @Index(name = "idx_comments_parent_id", columnList = "parent_comment_id, created_at"),
        @Index(name = "idx_comments_author", columnList = "author_id")
})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class CommentJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "comment_id", updatable = false, nullable = false)
    private UUID commentId;

    @Column(name = "post_id", nullable = false)
    private UUID postId;

    @Column(name = "parent_comment_id")
    private UUID parentCommentId;

    @Column(name = "author_id", nullable = false)
    private UUID authorId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "like_count", nullable = false)
    private Integer likeCount;

    @Column(name = "reply_count", nullable = false)
    private Integer replyCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", insertable = false, updatable = false)
    private PostJpaEntity post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id", insertable = false, updatable = false)
    private CommentJpaEntity parentComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private UserJpaEntity author;
}
