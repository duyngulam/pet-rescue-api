package com.uit.petrescueapi.application.dto.rescue;

import lombok.*;

/**
 * Request DTO for updating rescue case status.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRescueCaseStatusRequestDto {

    private String status;  // REPORTED | IN_PROGRESS | RESCUED | CLOSED
}
