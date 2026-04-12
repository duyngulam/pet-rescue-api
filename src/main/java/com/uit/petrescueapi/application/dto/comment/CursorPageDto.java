package com.uit.petrescueapi.application.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CursorPageDto<T> {
    private List<T> items;
    private LocalDateTime nextCursor;
    private boolean hasMore;
}
