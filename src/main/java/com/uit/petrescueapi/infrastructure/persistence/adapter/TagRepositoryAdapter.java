package com.uit.petrescueapi.infrastructure.persistence.adapter;

import com.uit.petrescueapi.domain.entity.Tag;
import com.uit.petrescueapi.domain.repository.TagRepository;
import com.uit.petrescueapi.infrastructure.persistence.mapper.TagEntityMapper;
import com.uit.petrescueapi.infrastructure.persistence.repository.TagJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class TagRepositoryAdapter implements TagRepository {

    private final TagJpaRepository jpa;
    private final TagEntityMapper mapper;

    @Override
    public Tag save(Tag tag) {
        return mapper.toDomain(jpa.save(mapper.toEntity(tag)));
    }

    @Override
    public Optional<Tag> findById(UUID tagId) {
        return jpa.findById(tagId)
                .filter(e -> !e.isDeleted())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<Tag> findByCode(String code) {
        return jpa.findByCodeAndDeletedFalse(code)
                .map(mapper::toDomain);
    }

    @Override
    public Page<Tag> findAll(Pageable pageable) {
        return jpa.findAll(pageable).map(mapper::toDomain);
    }

    @Override
    public void delete(UUID tagId) {
        jpa.findById(tagId).ifPresent(entity -> {
            entity.setDeleted(true);
            entity.setDeletedAt(LocalDateTime.now());
            jpa.save(entity);
        });
    }
}
