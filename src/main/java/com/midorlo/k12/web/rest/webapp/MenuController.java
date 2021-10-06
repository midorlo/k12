package com.midorlo.k12.web.rest.webapp;

import com.midorlo.k12.domain.webapp.Menu;
import com.midorlo.k12.repository.webapp.MenuItemRepository;
import com.midorlo.k12.repository.webapp.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/webapp")
public class MenuController {

    private final MenuRepository     menuRepository;
    private final MenuItemRepository menuItemRepository;

    public MenuController(MenuRepository menuRepository,
                          MenuItemRepository menuItemRepository) {
        this.menuRepository     = menuRepository;
        this.menuItemRepository = menuItemRepository;
    }

    @GetMapping("/menus")
    public ResponseEntity<List<Menu>> getMenu() {
        List<Menu> all = menuRepository.findAll();
        log.info("all: {}", all);
        return ResponseEntity.ok(all);
    }
}
