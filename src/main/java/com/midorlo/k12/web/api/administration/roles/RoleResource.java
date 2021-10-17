package com.midorlo.k12.web.api.administration.roles;

import com.midorlo.k12.configuration.web.problem.BadRequestAlertProblem;
import com.midorlo.k12.domain.security.Role;
import com.midorlo.k12.repository.RoleRepository;
import com.midorlo.k12.web.RestUtilities;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.persistence.EntityNotFoundException;
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
 * REST controller for managing {@link Role}.
 */
@Slf4j
@RestController
@RequestMapping("/api")
@Transactional
public class RoleResource {

    private static final String ENTITY_NAME = "role";
    private final RoleRepository roleRepository;

    @Value("${application.clientApp.name}")
    private String applicationName;

    public RoleResource(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    /**
     * {@code POST  /roles} : Create a new role.
     *
     * @param role the role to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new role, or with
     * status {@code 400 (Bad Request)} if the role has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/roles")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws URISyntaxException {
        log.debug("REST request to save Role : {}", role);
        if (role.getId() != null) {
            throw new BadRequestAlertProblem("A new role cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Role result = roleRepository.save(role);
        return ResponseEntity
            .created(new URI("/api/roles/" + result.getId()))
            .headers(RestUtilities.createEntityCreationAlert(applicationName, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /roles/:id} : Updates an existing role.
     *
     * @param id   the id of the role to save.
     * @param role the role to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated role,
     * or with status {@code 400 (Bad Request)} if the role is not valid,
     * or with status {@code 500 (Internal Server Error)} if the role couldn't be updated.
     */
    @PutMapping("/roles/{id}")
    public ResponseEntity<Role> updateRole(@PathVariable(value = "id", required = false) final Long id, @Valid @RequestBody Role role) {
        log.debug("REST request to update Role : {}, {}", id, role);
        if (role.getId() == null) {
            throw new BadRequestAlertProblem("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, role.getId())) {
            throw new BadRequestAlertProblem("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleRepository.existsById(id)) {
            throw new BadRequestAlertProblem("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Role result = roleRepository.save(role);
        return ResponseEntity
            .ok()
            .headers(RestUtilities.createEntityUpdateAlert(applicationName, ENTITY_NAME, role.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /roles/:id} : Partial updates given fields of an existing role, field will ignore if it is null
     *
     * @param id   the id of the role to save.
     * @param role the role to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated role,
     * or with status {@code 400 (Bad Request)} if the role is not valid,
     * or with status {@code 404 (Not Found)} if the role is not found,
     * or with status {@code 500 (Internal Server Error)} if the role couldn't be updated.
     */
    @PatchMapping(value = "/roles/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Role> partialUpdateRole(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Role role
    ) {
        log.debug("REST request to partial update Role partially : {}, {}", id, role);
        if (role.getId() == null) {
            throw new BadRequestAlertProblem("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, role.getId())) {
            throw new BadRequestAlertProblem("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!roleRepository.existsById(id)) {
            throw new BadRequestAlertProblem("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Role> result = roleRepository
            .findById(role.getId())
            .map(
                existingRole -> {
                    if (role.getName() != null) {
                        existingRole.setName(role.getName());
                    }

                    return existingRole;
                }
            )
            .map(roleRepository::save);

        return RestUtilities.wrapOrNotFound(
            result.orElseThrow(EntityNotFoundException::new),
            RestUtilities.createEntityUpdateAlert(applicationName, ENTITY_NAME, role.getId().toString())
        );
    }

    /**
     * {@code GET  /roles} : get all the roles.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of roles in body.
     */
    @GetMapping("/roles")
    public List<Role> getAllRoles(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Roles");
        return roleRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /roles/:id} : get the "id" role.
     *
     * @param id the id of the role to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the role, or with status {@code
     * 404 (Not Found)}.
     */
    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> getRole(@PathVariable Long id) {
        log.debug("REST request to get Role : {}", id);
        Optional<Role> role = roleRepository.findOneWithEagerRelationships(id);
        return ResponseEntity.ok().body(role.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    /**
     * {@code DELETE  /roles/:id} : delete the "id" role.
     *
     * @param id the id of the role to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/roles/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        log.debug("REST request to delete Role : {}", id);
        roleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(RestUtilities.createEntityDeletionAlert(applicationName, ENTITY_NAME, id.toString()))
            .build();
    }
}
