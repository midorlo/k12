package com.midorlo.k12.repository;

import com.midorlo.k12.domain.Clearance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Clearance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClearanceRepository extends JpaRepository<Clearance, Long> {}
