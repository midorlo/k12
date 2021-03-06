package com.midorlo.k12.web.rest.webapp;

import com.midorlo.k12.configuration.web.problem.BadRequestAlertProblem;
import com.midorlo.k12.domain.webapp.Menu;
import com.midorlo.k12.repository.MenuRepository;
import com.midorlo.k12.web.RestUtilities;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * REST controller for managing {@link Menu}.
 */
@Slf4j
@RestController
@RequestMapping("/api/webapp")
@Transactional
public class MenuResource {

    private static final String ENTITY_NAME = "menu";
    private final MenuRepository menuRepository;

    @Value("${application.meta.name}")
    private String applicationName;

    public MenuResource(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    /**
     * {@code POST  /menus} : Create a new menu.
     *
     * @param menu the menu to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new menu, or with
     * status {@code 400 (Bad Request)} if the menu has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/menus")
    public ResponseEntity<Menu> createMenu(@Valid @RequestBody Menu menu) throws URISyntaxException {
        log.debug("REST request to save Menu : {}", menu);
        if (menu.getId() != null) {
            throw new BadRequestAlertProblem("A new menu cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Menu result = menuRepository.save(menu);
        return ResponseEntity
            .created(new URI("/api/webapp/menus/" + result.getId()))
            .headers(RestUtilities.createEntityCreationAlert(applicationName, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /menus/:id} : Updates an existing menu.
     *
     * @param id   the id of the menu to save.
     * @param menu the menu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menu,
     * or with status {@code 400 (Bad Request)} if the menu is not valid,
     * or with status {@code 500 (Internal Server Error)} if the menu couldn't be updated.
     */
    @PutMapping("/menus/{id}")
    public ResponseEntity<Menu> updateMenu(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Menu menu) {
        log.debug("REST request to update Menu : {}, {}", id, menu);
        if (menu.getId() == null) {
            throw new BadRequestAlertProblem("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menu.getId())) {
            throw new BadRequestAlertProblem("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuRepository.existsById(id)) {
            throw new BadRequestAlertProblem("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Menu result = menuRepository.save(menu);
        return ResponseEntity
            .ok()
            .headers(RestUtilities.createEntityUpdateAlert(applicationName, ENTITY_NAME, menu.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /menus/:id} : Partial updates given fields of an existing menu, field will ignore if it is null
     *
     * @param id   the id of the menu to save.
     * @param menu the menu to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menu,
     * or with status {@code 400 (Bad Request)} if the menu is not valid,
     * or with status {@code 404 (Not Found)} if the menu is not found,
     * or with status {@code 500 (Internal Server Error)} if the menu couldn't be updated.
     */
    @PatchMapping(value = "/menus/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Menu> partialUpdateMenu(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Menu menu
    ) {
        log.debug("REST request to partial update Menu partially : {}, {}", id, menu);
        if (menu.getId() == null) {
            throw new BadRequestAlertProblem("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menu.getId())) {
            throw new BadRequestAlertProblem("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuRepository.existsById(id)) {
            throw new BadRequestAlertProblem("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Menu> result = menuRepository
            .findById(menu.getId())
            .map(
                existingMenu -> {
                    if (menu.getI18n() != null) {
                        existingMenu.setI18n(menu.getI18n());
                    }
                    if (menu.getIcon() != null) {
                        existingMenu.setIcon(menu.getIcon());
                    }
                    if (menu.getEnabled() != null) {
                        existingMenu.setEnabled(menu.getEnabled());
                    }

                    return existingMenu;
                }
            )
            .map(menuRepository::save);
        return ResponseEntity.ok().body(result.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    /**
     * {@code GET  /menus} : get all the menus.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of menus in body.
     */
    @GetMapping("/menus")
    public List<Menu> getAllMenus() {
        log.debug("REST request to get all Menus");
        return menuRepository.findAll();
    }

    /**
     * {@code GET  /menus/:id} : get the "id" menu.
     *
     * @param id the id of the menu to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the menu, or with status {@code
     * 404 (Not Found)}.
     */
    @GetMapping("/menus/{id}")
    public ResponseEntity<Menu> getMenu(@PathVariable Long id) {
        log.debug("REST request to get Menu : {}", id);
        Optional<Menu> menu = menuRepository.findById(id);
        return ResponseEntity.ok().body(menu.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    /**
     * {@code DELETE  /menus/:id} : delete the "id" menu.
     *
     * @param id the id of the menu to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/menus/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        log.debug("REST request to delete Menu : {}", id);
        menuRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(RestUtilities.createEntityDeletionAlert(applicationName, ENTITY_NAME, id.toString()))
            .build();
    }
}
