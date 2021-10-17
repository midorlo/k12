package com.midorlo.k12.repository;

import com.midorlo.k12.domain.security.Clearance;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Clearance} entity.
 */
public interface ClearanceRepository extends JpaRepository<Clearance, String> {}
