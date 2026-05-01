package com.uit.petrescueapi.application.dto.banner;

import jakarta.validation.constraints.NotBlank;
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
public class CreateBannerRequestDto {

    @NotBlank(message = "Title is required")
    private String title;

    private String subtitle;

    private String buttonText;

    private UUID mediaId;

    private String linkUrl;

    private String linkType;  // INTERNAL, EXTERNAL, NONE

    private Integer displayOrder;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Builder.Default
    private boolean active = true;

    private String targetPage;  // HOME, ADOPTION, RESCUE, etc.
}
