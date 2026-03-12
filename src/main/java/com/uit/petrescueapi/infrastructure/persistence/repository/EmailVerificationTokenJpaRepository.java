package com.uit.petrescueapi.infrastructure.persistence.repository;

import com.uit.petrescueapi.infrastructure.persistence.entity.EmailVerificationTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmailVerificationTokenJpaRepository extends JpaRepository<EmailVerificationTokenJpaEntity, UUID> {

    Optional<EmailVerificationTokenJpaEntity> findByToken(String token);

    @Query("SELECT t FROM EmailVerificationTokenJpaEntity t WHERE t.userId = :userId ORDER BY t.createdAt DESC LIMIT 1")
    Optional<EmailVerificationTokenJpaEntity> findLatestByUserId(@Param("userId") UUID userId);
}
