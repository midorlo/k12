package com.midorlo.k12.repository.webapp;

import com.midorlo.k12.domain.webapp.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Integer> {
}
