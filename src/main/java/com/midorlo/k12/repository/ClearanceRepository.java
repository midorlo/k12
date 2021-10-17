package com.midorlo.k12.repository;

import com.midorlo.k12.domain.security.Clearance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.Optional;

/**
 * Spring Data JPA repository for the {@link Clearance} entity.
 */
public interface ClearanceRepository extends JpaRepository<Clearance, Long> {
    Optional<Clearance> findByName(String name);

    boolean existsByName(String name);

    Integer deleteByNameEqualsIgnoreCase(@NonNull String name);
}
