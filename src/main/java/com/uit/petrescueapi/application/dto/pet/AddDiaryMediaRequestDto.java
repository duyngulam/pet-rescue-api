package com.uit.petrescueapi.application.dto.pet;

import lombok.*;

import java.util.UUID;

/**
 * Request DTO for adding a media entry to pet diary.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddDiaryMediaRequestDto {

    private UUID mediaId;
}
