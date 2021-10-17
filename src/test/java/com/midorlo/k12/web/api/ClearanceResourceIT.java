package com.midorlo.k12.web.api;

import static com.midorlo.k12.configuration.ApplicationConstants.SecurityConstants.ROLE_ADMIN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.midorlo.k12.IntegrationTest;
import com.midorlo.k12.domain.security.Clearance;
import com.midorlo.k12.repository.ClearanceRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link com.midorlo.k12.web.api.administration.clearances.ClearanceResource} REST
 * controller.
 */
@SuppressWarnings("OptionalGetWithoutIsPresent")
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser(authorities = ROLE_ADMIN)
class ClearanceResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL      = "/api/clearances";
    private static final String ENTITY_API_URL_NAME = ENTITY_API_URL + "/{name}";

    private static final Random random = new Random();
    private static final AtomicLong count = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ClearanceRepository clearanceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restClearanceMockMvc;

    private Clearance clearance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    @SuppressWarnings("unused")
    public static Clearance createEntity(EntityManager em) {
        return new Clearance().setName(DEFAULT_NAME);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    @SuppressWarnings("unused")
    public static Clearance createUpdatedEntity(EntityManager em) {
        return new Clearance().setName(UPDATED_NAME);
    }

    @BeforeEach
    public void initTest() {
        clearance = createEntity(em);
    }

    @Test
    @Transactional
    void createClearance() throws Exception {
        restClearanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clearance)))
            .andExpect(status().isCreated());
        // Validate the Clearance in the database
        assertThat(clearanceRepository.findAll().stream().map(Clearance::getName).collect(Collectors.toList())).contains(DEFAULT_NAME);
    }

    @Test
    @Transactional
    void createClearanceWithExistingId() throws Exception {
        // Create the Clearance with an existing ID
        clearance.setName(DEFAULT_NAME);
        restClearanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clearance)))
            .andExpect(status().isCreated());
        // remember the total count
        int databaseSizeBeforeCreate = clearanceRepository.findAll().size();

        //try to create the same clearance
        restClearanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clearance)))
            .andExpect(status().isBadRequest());
        List<Clearance> clearanceList = clearanceRepository.findAll();
        assertThat(clearanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checki18nIsRequired() throws Exception {
        int databaseSizeBeforeTest = clearanceRepository.findAll().size();
        // set the field null
        clearance.setName(null);

        // Create the Clearance, which fails.

        restClearanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clearance)))
            .andExpect(status().isBadRequest());

        List<Clearance> clearanceList = clearanceRepository.findAll();
        assertThat(clearanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAuthorities() throws Exception {
        // Initialize the database
        clearanceRepository.saveAndFlush(clearance);

        // Get all the clearanceList
        restClearanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].name").value(hasItem(clearance.getName())));
    }

    @Test
    @Transactional
    void getClearance() throws Exception {
        // Initialize the database
        clearanceRepository.saveAndFlush(clearance);

        // Get the clearance
        restClearanceMockMvc
            .perform(get(ENTITY_API_URL_NAME, clearance.getName()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.name").value(clearance.getName()));
    }

    @Test
    @Transactional
    void getNonExistingClearance() throws Exception {
        // Get the clearance
        restClearanceMockMvc.perform(get(ENTITY_API_URL_NAME, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewClearance() throws Exception {
        // Initialize the database
        clearanceRepository.saveAndFlush(clearance);

        // Update the clearance
        Clearance updatedClearance = clearanceRepository.findByName(clearance.getName()).get();
        // Disconnect from session so that the updates on updatedClearance are not directly saved in db
        em.detach(updatedClearance);
        updatedClearance.setName(UPDATED_NAME);

        restClearanceMockMvc
            .perform(
                put(ENTITY_API_URL_NAME, updatedClearance.getName())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClearance))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void putNonExistingClearance() throws Exception {
        int databaseSizeBeforeUpdate = clearanceRepository.findAll().size();
        clearance.setName("count.incrementAndGet()");

        // If the entity doesn't have an ID, it will throw BadRequestAlertProblem
        restClearanceMockMvc
            .perform(
                put(ENTITY_API_URL_NAME, clearance.getName())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clearance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clearance in the database
        List<Clearance> clearanceList = clearanceRepository.findAll();
        assertThat(clearanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchClearance() throws Exception {
        int databaseSizeBeforeUpdate = clearanceRepository.findAll().size();
        clearance.setName("count.incrementAndGet()");

        // If url ID doesn't match entity ID, it will throw BadRequestAlertProblem
        restClearanceMockMvc
            .perform(
                put(ENTITY_API_URL_NAME, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(clearance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clearance in the database
        List<Clearance> clearanceList = clearanceRepository.findAll();
        assertThat(clearanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamClearance() throws Exception {
        int databaseSizeBeforeUpdate = clearanceRepository.findAll().size();
        clearance.setName("count.incrementAndGet()");

        // If url ID doesn't match entity ID, it will throw BadRequestAlertProblem
        restClearanceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clearance)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Clearance in the database
        List<Clearance> clearanceList = clearanceRepository.findAll();
        assertThat(clearanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateClearanceWithPatch() throws Exception {
        // Initialize the database
        clearanceRepository.saveAndFlush(clearance);

        // Update the clearance using partial update
        Clearance partialUpdatedClearance = new Clearance();
        partialUpdatedClearance.setName(clearance.getName());

        partialUpdatedClearance.setName(UPDATED_NAME);

        restClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_NAME, partialUpdatedClearance.getName())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClearance))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void fullUpdateClearanceWithPatch() throws Exception {
        // Initialize the database
        clearanceRepository.saveAndFlush(clearance);

        // Update the clearance using partial update
        Clearance partialUpdatedClearance = new Clearance();
        partialUpdatedClearance.setName(clearance.getName());

        partialUpdatedClearance.setName(UPDATED_NAME);

        restClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_NAME, partialUpdatedClearance.getName())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClearance))
            )
            .andExpect(status().isBadRequest());
    }

    @Test
    @Transactional
    void patchNonExistingClearance() throws Exception {
        int databaseSizeBeforeUpdate = clearanceRepository.findAll().size();
        clearance.setName("count.incrementAndGet()");

        // If the entity doesn't have an ID, it will throw BadRequestAlertProblem
        restClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_NAME, clearance.getName())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clearance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clearance in the database
        List<Clearance> clearanceList = clearanceRepository.findAll();
        assertThat(clearanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchClearance() throws Exception {
        int databaseSizeBeforeUpdate = clearanceRepository.findAll().size();
        clearance.setName("count.incrementAndGet()");

        // If url ID doesn't match entity ID, it will throw BadRequestAlertProblem
        restClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_NAME, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(clearance))
            )
            .andExpect(status().isBadRequest());

        // Validate the Clearance in the database
        List<Clearance> clearanceList = clearanceRepository.findAll();
        assertThat(clearanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamClearance() throws Exception {
        int databaseSizeBeforeUpdate = clearanceRepository.findAll().size();
        clearance.setName("count.incrementAndGet()");

        // If url ID doesn't match entity ID, it will throw BadRequestAlertProblem
        restClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(clearance))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Clearance in the database
        List<Clearance> clearanceList = clearanceRepository.findAll();
        assertThat(clearanceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteClearance() throws Exception {
        // Initialize the database
        clearanceRepository.saveAndFlush(clearance);

        int databaseSizeBeforeDelete = clearanceRepository.findAll().size();

        // Delete the clearance
        restClearanceMockMvc
            .perform(delete(ENTITY_API_URL_NAME, clearance.getName()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Clearance> clearanceList = clearanceRepository.findAll();
        assertThat(clearanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
