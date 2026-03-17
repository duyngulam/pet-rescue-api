package com.uit.petrescueapi.domain.repository;

import com.uit.petrescueapi.domain.entity.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

/**
 * Domain repository contract for the Tag entity.
 */
public interface TagRepository {

    Tag save(Tag tag);

    Optional<Tag> findById(UUID tagId);

    Optional<Tag> findByCode(String code);

    Page<Tag> findAll(Pageable pageable);

    void delete(UUID tagId);
}
