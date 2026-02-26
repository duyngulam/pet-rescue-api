package com.uit.petrescueapi.application.dto.tag;

import lombok.*;

/**
 * Request DTO for creating a new tag.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateTagRequestDto {

    private String code;
    private String name;
    private String description;
}
