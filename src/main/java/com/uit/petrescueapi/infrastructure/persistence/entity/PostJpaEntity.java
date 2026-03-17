package com.uit.petrescueapi.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * JPA entity mapped to the {@code posts} table.
 */
@Entity
@Table(name = "posts", indexes = {
        @Index(name = "idx_posts_author", columnList = "author_id"),
        @Index(name = "idx_posts_rescue", columnList = "rescue_case_id"),
        @Index(name = "idx_posts_created_at", columnList = "created_at")
})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class PostJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "post_id", updatable = false, nullable = false)
    private UUID postId;

    @Column(name = "author_id")
    private UUID authorId;

    @Column(name = "rescue_case_id")
    private UUID rescueCaseId;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", insertable = false, updatable = false)
    private UserJpaEntity author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rescue_case_id", insertable = false, updatable = false)
    private RescueCaseJpaEntity rescueCase;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_media",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "media_id")
    )
    @Builder.Default
    private List<MediaFileJpaEntity> mediaFiles = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    @Builder.Default
    private List<TagJpaEntity> tags = new ArrayList<>();
}
