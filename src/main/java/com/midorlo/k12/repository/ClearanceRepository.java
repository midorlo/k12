package com.midorlo.k12.repository;

import com.midorlo.k12.domain.security.Clearance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Clearance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClearanceRepository extends JpaRepository<Clearance, Long> {}
