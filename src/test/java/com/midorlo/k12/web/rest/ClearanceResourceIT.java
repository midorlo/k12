package com.midorlo.k12.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.midorlo.k12.IntegrationTest;
import com.midorlo.k12.domain.Clearance;
import com.midorlo.k12.repository.ClearanceRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
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
 * Integration tests for the {@link ClearanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ClearanceResourceIT {

    private static final String DEFAULT_I_18_N = "AAAAAAAAAA";
    private static final String UPDATED_I_18_N = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/clearances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static final Random random = new Random();
    private static final AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

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
    public static Clearance createEntity(EntityManager em) {
        return new Clearance().i18n(DEFAULT_I_18_N);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Clearance createUpdatedEntity(EntityManager em) {
        return new Clearance().i18n(UPDATED_I_18_N);
    }

    @BeforeEach
    public void initTest() {
        clearance = createEntity(em);
    }

    @Test
    @Transactional
    void createClearance() throws Exception {
        int databaseSizeBeforeCreate = clearanceRepository.findAll().size();
        // Create the Clearance
        restClearanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clearance)))
            .andExpect(status().isCreated());

        // Validate the Clearance in the database
        List<Clearance> clearanceList = clearanceRepository.findAll();
        assertThat(clearanceList).hasSize(databaseSizeBeforeCreate + 1);
        Clearance testClearance = clearanceList.get(clearanceList.size() - 1);
        assertThat(testClearance.getI18n()).isEqualTo(DEFAULT_I_18_N);
    }

    @Test
    @Transactional
    void createClearanceWithExistingId() throws Exception {
        // Create the Clearance with an existing ID
        clearance.setId(1L);

        int databaseSizeBeforeCreate = clearanceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restClearanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clearance)))
            .andExpect(status().isBadRequest());

        // Validate the Clearance in the database
        List<Clearance> clearanceList = clearanceRepository.findAll();
        assertThat(clearanceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checki18nIsRequired() throws Exception {
        int databaseSizeBeforeTest = clearanceRepository.findAll().size();
        // set the field null
        clearance.seti18n(null);

        // Create the Clearance, which fails.

        restClearanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(clearance)))
            .andExpect(status().isBadRequest());

        List<Clearance> clearanceList = clearanceRepository.findAll();
        assertThat(clearanceList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllClearances() throws Exception {
        // Initialize the database
        clearanceRepository.saveAndFlush(clearance);

        // Get all the clearanceList
        restClearanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(clearance.getId().intValue())))
            .andExpect(jsonPath("$.[*].i18n").value(hasItem(DEFAULT_I_18_N)));
    }

    @Test
    @Transactional
    void getClearance() throws Exception {
        // Initialize the database
        clearanceRepository.saveAndFlush(clearance);

        // Get the clearance
        restClearanceMockMvc
            .perform(get(ENTITY_API_URL_ID, clearance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(clearance.getId().intValue()))
            .andExpect(jsonPath("$.i18n").value(DEFAULT_I_18_N));
    }

    //    @Test
    //    @Transactional
    //    void getNonExistingClearance() throws Exception {
    //        // Get the clearance
    //        restClearanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    //    }

    @Test
    @Transactional
    void putNewClearance() throws Exception {
        // Initialize the database
        clearanceRepository.saveAndFlush(clearance);

        int databaseSizeBeforeUpdate = clearanceRepository.findAll().size();

        // Update the clearance
        Clearance updatedClearance = clearanceRepository.findById(clearance.getId()).get();
        // Disconnect from session so that the updates on updatedClearance are not directly saved in db
        em.detach(updatedClearance);
        updatedClearance.i18n(UPDATED_I_18_N);

        restClearanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedClearance.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedClearance))
            )
            .andExpect(status().isOk());

        // Validate the Clearance in the database
        List<Clearance> clearanceList = clearanceRepository.findAll();
        assertThat(clearanceList).hasSize(databaseSizeBeforeUpdate);
        Clearance testClearance = clearanceList.get(clearanceList.size() - 1);
        assertThat(testClearance.getI18n()).isEqualTo(UPDATED_I_18_N);
    }

    @Test
    @Transactional
    void putNonExistingClearance() throws Exception {
        int databaseSizeBeforeUpdate = clearanceRepository.findAll().size();
        clearance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClearanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, clearance.getId())
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
        clearance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClearanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
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
        clearance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
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

        int databaseSizeBeforeUpdate = clearanceRepository.findAll().size();

        // Update the clearance using partial update
        Clearance partialUpdatedClearance = new Clearance();
        partialUpdatedClearance.setId(clearance.getId());

        partialUpdatedClearance.i18n(UPDATED_I_18_N);

        restClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClearance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClearance))
            )
            .andExpect(status().isOk());

        // Validate the Clearance in the database
        List<Clearance> clearanceList = clearanceRepository.findAll();
        assertThat(clearanceList).hasSize(databaseSizeBeforeUpdate);
        Clearance testClearance = clearanceList.get(clearanceList.size() - 1);
        assertThat(testClearance.getI18n()).isEqualTo(UPDATED_I_18_N);
    }

    @Test
    @Transactional
    void fullUpdateClearanceWithPatch() throws Exception {
        // Initialize the database
        clearanceRepository.saveAndFlush(clearance);

        int databaseSizeBeforeUpdate = clearanceRepository.findAll().size();

        // Update the clearance using partial update
        Clearance partialUpdatedClearance = new Clearance();
        partialUpdatedClearance.setId(clearance.getId());

        partialUpdatedClearance.i18n(UPDATED_I_18_N);

        restClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedClearance.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedClearance))
            )
            .andExpect(status().isOk());

        // Validate the Clearance in the database
        List<Clearance> clearanceList = clearanceRepository.findAll();
        assertThat(clearanceList).hasSize(databaseSizeBeforeUpdate);
        Clearance testClearance = clearanceList.get(clearanceList.size() - 1);
        assertThat(testClearance.getI18n()).isEqualTo(UPDATED_I_18_N);
    }

    @Test
    @Transactional
    void patchNonExistingClearance() throws Exception {
        int databaseSizeBeforeUpdate = clearanceRepository.findAll().size();
        clearance.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, clearance.getId())
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
        clearance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restClearanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
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
        clearance.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
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
            .perform(delete(ENTITY_API_URL_ID, clearance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Clearance> clearanceList = clearanceRepository.findAll();
        assertThat(clearanceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
