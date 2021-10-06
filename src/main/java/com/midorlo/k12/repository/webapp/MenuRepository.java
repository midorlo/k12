package com.midorlo.k12.repository.webapp;

import com.midorlo.k12.domain.webapp.Menu;
import com.midorlo.k12.repository.webapp.projection.MenuInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.lang.NonNull;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    List<Menu> findByDefaultTitleEqualsIgnoreCase(@NonNull String defaultTitle);
}
