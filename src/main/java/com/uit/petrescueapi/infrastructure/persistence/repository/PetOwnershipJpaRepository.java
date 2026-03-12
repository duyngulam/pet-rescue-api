package com.uit.petrescueapi.infrastructure.persistence.repository;

import com.uit.petrescueapi.infrastructure.persistence.entity.PetOwnershipJpaEntity;
import com.uit.petrescueapi.infrastructure.persistence.entity.PetOwnershipId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PetOwnershipJpaRepository extends JpaRepository<PetOwnershipJpaEntity, PetOwnershipId> {

    Optional<PetOwnershipJpaEntity> findByPetIdAndToTimeIsNull(UUID petId);

    List<PetOwnershipJpaEntity> findAllByPetIdOrderByFromTimeDesc(UUID petId);

    @Modifying
    @Query("UPDATE PetOwnershipJpaEntity o SET o.toTime = :endTime WHERE o.petId = :petId AND o.toTime IS NULL")
    void endCurrentOwnership(@Param("petId") UUID petId, @Param("endTime") LocalDateTime endTime);
}
