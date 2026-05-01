package com.uit.petrescueapi.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * JPA entity mapped to the {@code banners} table.
 */
@Entity
@Table(name = "banners", indexes = {
        @Index(name = "idx_banners_active", columnList = "is_active"),
        @Index(name = "idx_banners_target_page", columnList = "target_page"),
        @Index(name = "idx_banners_display_order", columnList = "display_order")
})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class BannerJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "banner_id", updatable = false, nullable = false)
    private UUID bannerId;

    @Column(name = "title", length = 255)
    private String title;

    @Column(name = "subtitle", columnDefinition = "TEXT")
    private String subtitle;

    @Column(name = "button_text", length = 255)
    private String buttonText;

    @Column(name = "media_id")
    private UUID mediaId;

    @Column(name = "link_url", columnDefinition = "TEXT")
    private String linkUrl;

    @Column(name = "link_type", length = 50)
    @Builder.Default
    private String linkType = "NONE";

    @Column(name = "display_order", nullable = false)
    @Builder.Default
    private Integer displayOrder = 0;

    @Column(name = "start_date")
    private LocalDateTime startDate;

    @Column(name = "end_date")
    private LocalDateTime endDate;

    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "target_page", length = 100)
    @Builder.Default
    private String targetPage = "HOME";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "media_id", insertable = false, updatable = false)
    private MediaFileJpaEntity media;
}
