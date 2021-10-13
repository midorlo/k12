package com.midorlo.k12.repository;

import com.midorlo.k12.domain.webapp.MenuItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the MenuItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {}
