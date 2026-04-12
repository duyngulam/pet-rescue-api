package com.uit.petrescueapi.application.port.out;

import com.uit.petrescueapi.application.dto.comment.CommentResponseDto;
import com.uit.petrescueapi.application.dto.comment.CommentSummaryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.UUID;

public interface CommentQueryDataPort {
    Page<CommentSummaryDto> findParentCommentsByPostId(UUID postId, LocalDateTime cursor, Pageable pageable);
    Page<CommentSummaryDto> findRepliesByParentCommentId(UUID parentCommentId, Pageable pageable);
    CommentResponseDto findById(UUID commentId);
}
