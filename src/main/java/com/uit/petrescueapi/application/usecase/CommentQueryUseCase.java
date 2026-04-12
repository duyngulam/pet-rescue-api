package com.uit.petrescueapi.application.usecase;

import com.uit.petrescueapi.application.dto.comment.CommentResponseDto;
import com.uit.petrescueapi.application.dto.comment.CommentSummaryDto;
import com.uit.petrescueapi.application.dto.comment.CursorPageDto;
import com.uit.petrescueapi.application.port.out.CommentQueryDataPort;
import com.uit.petrescueapi.application.port.query.CommentQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentQueryUseCase implements CommentQueryPort {

    private final CommentQueryDataPort queryDataPort;

    @Override
    public CursorPageDto<CommentSummaryDto> findParentCommentsByPostId(UUID postId, LocalDateTime cursor, Pageable pageable) {
        Page<CommentSummaryDto> page = queryDataPort.findParentCommentsByPostId(postId, cursor, pageable);
        LocalDateTime nextCursor = page.hasContent()
                ? page.getContent().get(page.getContent().size() - 1).getCreatedAt()
                : null;
        return CursorPageDto.<CommentSummaryDto>builder()
                .items(page.getContent())
                .nextCursor(nextCursor)
                .hasMore(page.hasNext())
                .build();
    }

    @Override
    public Page<CommentSummaryDto> findRepliesByParentCommentId(UUID parentCommentId, Pageable pageable) {
        return queryDataPort.findRepliesByParentCommentId(parentCommentId, pageable);
    }

    @Override
    public CommentResponseDto findById(UUID commentId) {
        return queryDataPort.findById(commentId);
    }
}
