package com.uit.petrescueapi.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;

/**
 * JPA entity mapped to the {@code media_files} table.
 */
@Entity
@Table(name = "media_files", indexes = {
        @Index(name = "idx_media_uploader", columnList = "uploader_id"),
        @Index(name = "idx_media_public_id", columnList = "public_id")
})
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class MediaFileJpaEntity extends BaseJpaEntity {

    @Id
    @Column(name = "media_id", updatable = false, nullable = false)
    private UUID mediaId;

    @Column(name = "uploader_id")
    private UUID uploaderId;

    @Column(name = "public_id", unique = true, nullable = false, length = 255)
    private String publicId;

    @Column(name = "resource_type", length = 20)
    private String resourceType;

    @Column(name = "format", length = 20)
    private String format;

    @Column(name = "width")
    private Integer width;

    @Column(name = "height")
    private Integer height;

    @Column(name = "bytes")
    private Integer bytes;

    @Column(name = "folder", length = 255)
    private String folder;

    @Column(name = "status", length = 20)
    @Builder.Default
    private String status = "PERMANENT";

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uploader_id", insertable = false, updatable = false)
    private UserJpaEntity uploader;
}
