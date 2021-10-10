package com.midorlo.k12.web.rest;

import com.midorlo.k12.domain.Clearance;
import com.midorlo.k12.repository.ClearanceRepository;
import com.midorlo.k12.web.rest.errors.BadRequestAlertException;
import com.midorlo.k12.web.util.HeaderUtil;
import com.midorlo.k12.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing {@link com.midorlo.k12.domain.Clearance}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ClearanceResource {

    private final Logger log = LoggerFactory.getLogger(ClearanceResource.class);

    private static final String ENTITY_NAME = "clearance";

    @Value("${application.clientApp.name}")
    private String applicationName;

    private final ClearanceRepository clearanceRepository;

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
        if (clearance.getId() != null) {
            throw new BadRequestAlertException("A new clearance cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Clearance result = clearanceRepository.save(clearance);
        return ResponseEntity
            .created(new URI("/api/clearances/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId()
                                                                                                     .toString()))
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
    public ResponseEntity<Clearance> updateClearance(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Clearance clearance
    ) {
        log.debug("REST request to update Clearance : {}, {}", id, clearance);
        if (clearance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clearance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clearanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Clearance result = clearanceRepository.save(clearance);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clearance.getId()
                                                                                                      .toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /clearances/:id} : Partial updates given fields of an existing clearance, field will ignore if
     * it is null
     *
     * @param id        the id of the clearance to save.
     * @param clearance the clearance to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clearance,
     * or with status {@code 400 (Bad Request)} if the clearance is not valid,
     * or with status {@code 404 (Not Found)} if the clearance is not found,
     * or with status {@code 500 (Internal Server Error)} if the clearance couldn't be updated.
     */
    @PatchMapping(value = "/clearances/{id}", consumes = "application/merge-patch+json")
    public ResponseEntity<Clearance> partialUpdateClearance(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Clearance clearance
    ) {
        log.debug("REST request to partial update Clearance partially : {}, {}", id, clearance);
        if (clearance.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, clearance.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!clearanceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Clearance> result = clearanceRepository
            .findById(clearance.getId())
            .map(
                existingClearance -> {
                    if (clearance.geti18n() != null) {
                        existingClearance.seti18n(clearance.geti18n());
                    }

                    return existingClearance;
                }
            )
            .map(clearanceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result.orElseThrow(EntityNotFoundException::new),
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clearance.getId().toString())
        );
    }

    /**
     * {@code GET  /clearances} : get all the clearances.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clearances in body.
     */
    @GetMapping("/clearances")
    public List<Clearance> getAllClearances() {
        log.debug("REST request to get all Clearances");
        return clearanceRepository.findAll();
    }

    /**
     * {@code GET  /clearances/:id} : get the "id" clearance.
     *
     * @param id the id of the clearance to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clearance, or with status
     * {@code 404 (Not Found)}.
     */
    @GetMapping("/clearances/{id}")
    public ResponseEntity<Clearance> getClearance(@PathVariable Long id) {
        log.debug("REST request to get Clearance : {}", id);
        Optional<Clearance> clearance = clearanceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(clearance.orElseThrow(EntityNotFoundException::new));
    }

    /**
     * {@code DELETE  /clearances/:id} : delete the "id" clearance.
     *
     * @param id the id of the clearance to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clearances/{id}")
    public ResponseEntity<Void> deleteClearance(@PathVariable Long id) {
        log.debug("REST request to delete Clearance : {}", id);
        clearanceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
