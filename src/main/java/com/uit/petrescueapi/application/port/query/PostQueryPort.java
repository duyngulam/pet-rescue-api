package com.uit.petrescueapi.application.port.query;

import com.uit.petrescueapi.application.dto.post.PostResponseDto;
import com.uit.petrescueapi.application.dto.post.PostSummaryResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PostQueryPort {
    PostResponseDto findById(UUID postId);
    Page<PostSummaryResponseDto> findAll(Pageable pageable);
}
