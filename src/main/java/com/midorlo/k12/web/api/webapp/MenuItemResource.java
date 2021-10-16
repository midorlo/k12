package com.midorlo.k12.web.api.webapp;

import com.midorlo.k12.domain.webapp.MenuItem;
import com.midorlo.k12.repository.MenuItemRepository;
import com.midorlo.k12.web.RestUtilities;
import com.midorlo.k12.config.web.problem.BadRequestAlertProblem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link MenuItem}.
 */
@Slf4j
@RestController
@RequestMapping("/api/webapp")
@Transactional
public class MenuItemResource {

    private static final String             ENTITY_NAME = "menuItem";
    private final        MenuItemRepository menuItemRepository;
    @Value("${application.clientApp.name}")
    private              String             applicationName;

    public MenuItemResource(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    /**
     * {@code POST  /menu-items} : Create a new menuItem.
     *
     * @param menuItem the menuItem to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new menuItem, or with
     * status {@code 400 (Bad Request)} if the menuItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/menu-items")
    public ResponseEntity<MenuItem> createMenuItem(@Valid @RequestBody MenuItem menuItem) throws URISyntaxException {
        log.debug("REST request to save MenuItem : {}", menuItem);
        if (menuItem.getId() != null) {
            throw new BadRequestAlertProblem("A new menuItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MenuItem result = menuItemRepository.save(menuItem);
        return ResponseEntity
            .created(new URI("/api/webapp/menu-items/" + result.getId()))
            .headers(RestUtilities.createEntityCreationAlert(applicationName, ENTITY_NAME, result.getId()
                                                                                                 .toString()))
            .body(result);
    }

    /**
     * {@code PUT  /menu-items/:id} : Updates an existing menuItem.
     *
     * @param id       the id of the menuItem to save.
     * @param menuItem the menuItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuItem,
     * or with status {@code 400 (Bad Request)} if the menuItem is not valid,
     * or with status {@code 500 (Internal Server Error)} if the menuItem couldn't be updated.
     */
    @PutMapping("/menu-items/{id}")
    public ResponseEntity<MenuItem> updateMenuItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MenuItem menuItem
    ) {
        log.debug("REST request to update MenuItem : {}, {}", id, menuItem);
        if (menuItem.getId() == null) {
            throw new BadRequestAlertProblem("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuItem.getId())) {
            throw new BadRequestAlertProblem("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuItemRepository.existsById(id)) {
            throw new BadRequestAlertProblem("Entity not found", ENTITY_NAME, "idnotfound");
        }

        MenuItem result = menuItemRepository.save(menuItem);
        return ResponseEntity
            .ok()
            .headers(RestUtilities.createEntityUpdateAlert(applicationName, ENTITY_NAME, menuItem.getId()
                                                                                                 .toString()))
            .body(result);
    }

    private void verify(final Long id, @NotNull MenuItem menuItem) {
        if (menuItem.getId() == null) {
            throw new BadRequestAlertProblem("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, menuItem.getId())) {
            throw new BadRequestAlertProblem("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!menuItemRepository.existsById(id)) {
            throw new BadRequestAlertProblem("Entity not found", ENTITY_NAME, "idnotfound");
        }
    }

    /**
     * {@code PATCH  /menu-items/:id} : Partial updates given fields of an existing menuItem, field will ignore if it
     * is null
     *
     * @param id       the id of the menuItem to save.
     * @param menuItem the menuItem to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated menuItem,
     * or with status {@code 400 (Bad Request)} if the menuItem is not valid,
     * or with status {@code 404 (Not Found)} if the menuItem is not found,
     * or with status {@code 500 (Internal Server Error)} if the menuItem couldn't be updated.
     */
    @PatchMapping(value = "/menu-items/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<MenuItem> partialUpdateMenuItem(@PathVariable(value = "id", required = false) final Long id,
                                                          @NotNull @RequestBody MenuItem menuItem) {
        log.debug("REST request to partial update MenuItem partially : {}, {}", id, menuItem);
        verify(id, menuItem);
        Optional<MenuItem> result = menuItemRepository.findById(menuItem.getId())
                                                      .map(
                                                          existingMenuItem -> {
                                                              if (menuItem.getI18n() != null) {
                                                                  existingMenuItem.setI18n(menuItem.getI18n());
                                                              }
                                                              if (menuItem.getIcon() != null) {
                                                                  existingMenuItem.setIcon(menuItem.getIcon());
                                                              }
                                                              if (menuItem.getTarget() != null) {
                                                                  existingMenuItem.setTarget(menuItem.getTarget());
                                                              }
                                                              if (menuItem.getEnabled() != null) {
                                                                  existingMenuItem.setEnabled(menuItem.getEnabled());
                                                              }

                                                              return existingMenuItem;
                                                          }
                                                      )
                                                      .map(menuItemRepository::save);
        return RestUtilities.wrapOrNotFound(result.orElseThrow(EntityNotFoundException::new),
                                            RestUtilities.createEntityUpdateAlert(applicationName,
                                                                                  ENTITY_NAME,
                                                                                  menuItem.getId()
                                                                                          .toString()));
    }

    /**
     * {@code GET  /menu-items} : get all the menuItems.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of menuItems in body.
     */
    @GetMapping("/menu-items")
    public List<MenuItem> getAllMenuItems() {
        log.debug("REST request to get all MenuItems");
        return menuItemRepository.findAll();
    }

    /**
     * {@code GET  /menu-items/:id} : get the "id" menuItem.
     *
     * @param id the id of the menuItem to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the menuItem, or with status
     * {@code 404 (Not Found)}.
     */
    @GetMapping("/menu-items/{id}")
    public ResponseEntity<MenuItem> getMenuItem(@PathVariable Long id) {
        log.debug("REST request to get MenuItem : {}", id);
        Optional<MenuItem> menuItem = menuItemRepository.findById(id);
        return RestUtilities.wrapOrNotFound(menuItem.orElseThrow(EntityNotFoundException::new));
    }

    /**
     * {@code DELETE  /menu-items/:id} : delete the "id" menuItem.
     *
     * @param id the id of the menuItem to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/menu-items/{id}")
    public ResponseEntity<Void> deleteMenuItem(@PathVariable Long id) {
        log.debug("REST request to delete MenuItem : {}", id);
        menuItemRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(RestUtilities.createEntityDeletionAlert(applicationName, ENTITY_NAME, id.toString()))
            .build();
    }
}
