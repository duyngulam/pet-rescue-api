package com.uit.petrescueapi.application.usecase;

import com.uit.petrescueapi.application.port.in.query.PetQueryPort;
import com.uit.petrescueapi.domain.entity.Pet;
import com.uit.petrescueapi.domain.service.PetDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

/**
 * Query (read) use-case for Pet operations.
 * Delegates read-only calls to {@link PetDomainService}.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PetQueryUseCase implements PetQueryPort {

    private final PetDomainService domainService;

    @Override
    public Pet findById(UUID id) {
        log.debug("Query: find pet by id {}", id);
        return domainService.findById(id);
    }

    @Override
    public List<Pet> findAll() {
        log.debug("Query: find all pets");
        return domainService.findAll();
    }

    @Override
    public Page<Pet> findAll(Pageable pageable) {
        log.debug("Query: find all pets (paginated)");
        return domainService.findAll(pageable);
    }

    @Override
    public List<Pet> findAvailable() {
        log.debug("Query: find available pets");
        return domainService.findAvailable();
    }

    @Override
    public Page<Pet> findAvailable(Pageable pageable) {
        log.debug("Query: find available pets (paginated)");
        return domainService.findAvailable(pageable);
    }
}
