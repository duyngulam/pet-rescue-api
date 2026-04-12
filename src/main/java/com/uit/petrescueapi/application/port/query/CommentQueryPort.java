package com.uit.petrescueapi.application.port.query;

import com.uit.petrescueapi.application.dto.comment.CommentResponseDto;
import com.uit.petrescueapi.application.dto.comment.CommentSummaryDto;
import com.uit.petrescueapi.application.dto.comment.CursorPageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.UUID;

public interface CommentQueryPort {
    CursorPageDto<CommentSummaryDto> findParentCommentsByPostId(UUID postId, LocalDateTime cursor, Pageable pageable);
    Page<CommentSummaryDto> findRepliesByParentCommentId(UUID parentCommentId, Pageable pageable);
    CommentResponseDto findById(UUID commentId);
}
