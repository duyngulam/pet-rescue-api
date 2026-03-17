package com.uit.petrescueapi.infrastructure.persistence.adapter;

import com.uit.petrescueapi.domain.entity.Post;
import com.uit.petrescueapi.domain.repository.PostRepository;
import com.uit.petrescueapi.infrastructure.persistence.entity.PostJpaEntity;
import com.uit.petrescueapi.infrastructure.persistence.mapper.PostEntityMapper;
import com.uit.petrescueapi.infrastructure.persistence.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PostRepositoryAdapter implements PostRepository {

    private final PostJpaRepository jpa;
    private final PostEntityMapper mapper;

    @Override
    public Post save(Post post) {
        return mapper.toDomain(jpa.save(mapper.toEntity(post)));
    }

    @Override
    public Optional<Post> findById(UUID postId) {
        return jpa.findById(postId)
                .filter(e -> !e.isDeleted())
                .map(mapper::toDomain);
    }

    @Override
    public Page<Post> findAll(Pageable pageable) {
        return jpa.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public void delete(UUID postId) {
        jpa.findById(postId).ifPresent(entity -> {
            entity.setDeleted(true);
            entity.setDeletedAt(LocalDateTime.now());
            jpa.save(entity);
        });
    }
}
