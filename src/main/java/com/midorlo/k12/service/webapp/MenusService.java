package com.midorlo.k12.service.webapp;

import com.midorlo.k12.domain.webapp.Menu;
import com.midorlo.k12.repository.webapp.MenuRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class MenusService {

    private final MenuRepository menuRepository;

    public MenusService(MenuRepository menuRepository) {this.menuRepository = menuRepository;}

    public List<Menu> all() {
        List<Menu> all = menuRepository.findAll();
        log.info("all: {}", all);
        return all;
    }
}
