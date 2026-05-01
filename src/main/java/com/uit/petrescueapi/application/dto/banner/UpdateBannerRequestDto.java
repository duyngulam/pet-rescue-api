package com.uit.petrescueapi.application.dto.banner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateBannerRequestDto {

    private String title;

    private String subtitle;

    private String buttonText;

    private UUID mediaId;

    private String linkUrl;

    private String linkType;

    private Integer displayOrder;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Boolean active;

    private String targetPage;
}
