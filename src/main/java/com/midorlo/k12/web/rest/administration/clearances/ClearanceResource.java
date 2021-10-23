package com.midorlo.k12.web.rest.administration.clearances;

import com.midorlo.k12.configuration.web.problem.BadRequestAlertProblem;
import com.midorlo.k12.domain.security.Clearance;
import com.midorlo.k12.repository.ClearanceRepository;
import com.midorlo.k12.web.RestUtilities;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * REST controller for managing {@link Clearance}.
 */
@Slf4j
@RestController
@RequestMapping("/api")
@Transactional
public class ClearanceResource {

    private static final String              ENTITY_NAME = "clearance";
    private final        ClearanceRepository clearanceRepository;

    @Value("${application.meta.name}")
    private String applicationName;

    public ClearanceResource(ClearanceRepository clearanceRepository) {
        this.clearanceRepository = clearanceRepository;
    }

    /**
     * {@code POST  /clearances} : Create a new clearance.
     *
     * @param clearance the clearance to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clearance, or with
     * status {@code 400 (Bad Request)} if the clearance has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clearances")
    public ResponseEntity<Clearance> createClearance(@Valid @RequestBody Clearance clearance) throws
                                                                                              URISyntaxException {
        log.debug("REST request to save Clearance : {}", clearance);

        Optional<Clearance> byId = clearanceRepository.findByName(clearance.getName());
        if (byId.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }
        Clearance result = clearanceRepository.save(clearance);
        return ResponseEntity
            .created(new URI("/api/clearances/" + result.getName()))
            .headers(RestUtilities.createEntityCreationAlert(applicationName, ENTITY_NAME, result.getName()))
            .body(result);
    }

    /**
     * {@code PUT  /clearances/:id} : Updates an existing clearance.
     *
     * @param id        the id of the clearance to save.
     * @param clearance the clearance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clearance,
     * or with status {@code 400 (Bad Request)} if the clearance is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clearance couldn't be updated.
     */
    @PutMapping("/clearances/{id}")
    public ResponseEntity<Clearance> updateClearance(@PathVariable(value = "id", required = false) final Long id,
                                                     @Valid @RequestBody Clearance clearance) {

        log.debug("REST request to update Clearance : {}, {}", id, clearance);

        if (clearance.getName() == null) {
            throw new BadRequestAlertProblem("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clearance.getId())) {
            throw new BadRequestAlertProblem("Invalid ID", ENTITY_NAME, "idinvalid");
        }
        if (!clearanceRepository.existsById(id)) {
            throw new BadRequestAlertProblem("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Clearance result = clearanceRepository.save(clearance);
        return ResponseEntity
            .ok()
            .headers(RestUtilities.createEntityUpdateAlert(applicationName, ENTITY_NAME, clearance.getName()))
            .body(result);
    }

    /**
     * {@code PATCH  /clearances/:id} : Partial updates given fields of an existing clearance, field will ignore if
     * it is null
     *
     * @param name      the id of the clearance to save.
     * @param clearance the clearance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clearance,
     * or with status {@code 400 (Bad Request)} if the clearance is not valid,
     * or with status {@code 404 (Not Found)} if the clearance is not found,
     * or with status {@code 500 (Internal Server Error)} if the clearance couldn't be updated.
     */
    @PatchMapping(value = "/clearances/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Clearance> partialUpdateClearance(
        @PathVariable(value = "id", required = false) final String name,
        @NotNull @RequestBody Clearance clearance
    ) {
        log.debug("REST request to partial update Clearance partially : {}, {}", name, clearance);
        if (clearance.getName() == null) {
            throw new BadRequestAlertProblem("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(name, clearance.getName())) {
            throw new BadRequestAlertProblem("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clearanceRepository.existsByName(name)) {
            throw new BadRequestAlertProblem("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Clearance> result = clearanceRepository
            .findByName(clearance.getName())
            .map(
                existingClearance -> {
                    if (clearance.getName() != null) {
                        existingClearance.setName(clearance.getName());
                    }

                    return existingClearance;
                }
            )
            .map(clearanceRepository::save);

        return RestUtilities.wrapOrNotFound(
            result.orElseThrow(EntityNotFoundException::new),
            RestUtilities.createEntityUpdateAlert(applicationName, ENTITY_NAME, clearance.getName())
        );
    }

    /**
     * {@code GET  /clearances} : get all the clearances.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clearances in body.
     */
    @GetMapping("/clearances")
    public List<Clearance> getAllAuthorities() {
        log.debug("REST request to get all Authorities");
        return clearanceRepository.findAll();
    }

    /**
     * {@code GET  /clearances/:id} : get the "id" clearance.
     *
     * @param name the id of the clearance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clearance, or with status
     * {@code 404 (Not Found)}.
     */
    @GetMapping("/clearances/{name}")
    public ResponseEntity<Clearance> getClearance(@PathVariable String name) {
        log.debug("REST request to get Clearance : {}", name);
        Optional<Clearance> clearance = clearanceRepository.findByName(name);
        return ResponseEntity.ok().body(clearance.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));
    }

    /**
     * {@code DELETE  /clearances/:id} : delete the "id" clearance.
     *
     * @param name the id of the clearance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clearances/{name}")
    public ResponseEntity<Void> deleteClearance(@PathVariable String name) {
        log.debug("REST request to delete Clearance : {}", name);
        log.debug("Deleted clearance with result {}", clearanceRepository.deleteByNameEqualsIgnoreCase(name));
        return ResponseEntity.noContent()
                             .headers(RestUtilities.createEntityDeletionAlert(applicationName, ENTITY_NAME, name))
                             .build();
    }
}
