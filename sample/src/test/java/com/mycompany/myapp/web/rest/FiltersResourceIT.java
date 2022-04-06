package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Crawler;
import com.mycompany.myapp.domain.Filters;
import com.mycompany.myapp.repository.FiltersRepository;
import com.mycompany.myapp.service.criteria.FiltersCriteria;
import com.mycompany.myapp.service.dto.FiltersDTO;
import com.mycompany.myapp.service.mapper.FiltersMapper;
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
 * Integration tests for the {@link FiltersResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FiltersResourceIT {

    private static final String ENTITY_API_URL = "/api/filters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FiltersRepository filtersRepository;

    @Autowired
    private FiltersMapper filtersMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFiltersMockMvc;

    private Filters filters;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filters createEntity(EntityManager em) {
        Filters filters = new Filters();
        return filters;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filters createUpdatedEntity(EntityManager em) {
        Filters filters = new Filters();
        return filters;
    }

    @BeforeEach
    public void initTest() {
        filters = createEntity(em);
    }

    @Test
    @Transactional
    void createFilters() throws Exception {
        int databaseSizeBeforeCreate = filtersRepository.findAll().size();
        // Create the Filters
        FiltersDTO filtersDTO = filtersMapper.toDto(filters);
        restFiltersMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filtersDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Filters in the database
        List<Filters> filtersList = filtersRepository.findAll();
        assertThat(filtersList).hasSize(databaseSizeBeforeCreate + 1);
        Filters testFilters = filtersList.get(filtersList.size() - 1);
    }

    @Test
    @Transactional
    void createFiltersWithExistingId() throws Exception {
        // Create the Filters with an existing ID
        filters.setId(1L);
        FiltersDTO filtersDTO = filtersMapper.toDto(filters);

        int databaseSizeBeforeCreate = filtersRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFiltersMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filtersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filters in the database
        List<Filters> filtersList = filtersRepository.findAll();
        assertThat(filtersList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFilters() throws Exception {
        // Initialize the database
        filtersRepository.saveAndFlush(filters);

        // Get all the filtersList
        restFiltersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filters.getId().intValue())));
    }

    @Test
    @Transactional
    void getFilters() throws Exception {
        // Initialize the database
        filtersRepository.saveAndFlush(filters);

        // Get the filters
        restFiltersMockMvc
            .perform(get(ENTITY_API_URL_ID, filters.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(filters.getId().intValue()));
    }

    @Test
    @Transactional
    void getFiltersByIdFiltering() throws Exception {
        // Initialize the database
        filtersRepository.saveAndFlush(filters);

        Long id = filters.getId();

        defaultFiltersShouldBeFound("id.equals=" + id);
        defaultFiltersShouldNotBeFound("id.notEquals=" + id);

        defaultFiltersShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultFiltersShouldNotBeFound("id.greaterThan=" + id);

        defaultFiltersShouldBeFound("id.lessThanOrEqual=" + id);
        defaultFiltersShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFiltersByCrawlerIsEqualToSomething() throws Exception {
        // Initialize the database
        filtersRepository.saveAndFlush(filters);
        Crawler crawler;
        if (TestUtil.findAll(em, Crawler.class).isEmpty()) {
            crawler = CrawlerResourceIT.createEntity(em);
            em.persist(crawler);
            em.flush();
        } else {
            crawler = TestUtil.findAll(em, Crawler.class).get(0);
        }
        em.persist(crawler);
        em.flush();
        filters.setCrawler(crawler);
        filtersRepository.saveAndFlush(filters);
        Long crawlerId = crawler.getId();

        // Get all the filtersList where crawler equals to crawlerId
        defaultFiltersShouldBeFound("crawlerId.equals=" + crawlerId);

        // Get all the filtersList where crawler equals to (crawlerId + 1)
        defaultFiltersShouldNotBeFound("crawlerId.equals=" + (crawlerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFiltersShouldBeFound(String filter) throws Exception {
        restFiltersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filters.getId().intValue())));

        // Check, that the count call also returns 1
        restFiltersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFiltersShouldNotBeFound(String filter) throws Exception {
        restFiltersMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFiltersMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFilters() throws Exception {
        // Get the filters
        restFiltersMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFilters() throws Exception {
        // Initialize the database
        filtersRepository.saveAndFlush(filters);

        int databaseSizeBeforeUpdate = filtersRepository.findAll().size();

        // Update the filters
        Filters updatedFilters = filtersRepository.findById(filters.getId()).get();
        // Disconnect from session so that the updates on updatedFilters are not directly saved in db
        em.detach(updatedFilters);
        FiltersDTO filtersDTO = filtersMapper.toDto(updatedFilters);

        restFiltersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filtersDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filtersDTO))
            )
            .andExpect(status().isOk());

        // Validate the Filters in the database
        List<Filters> filtersList = filtersRepository.findAll();
        assertThat(filtersList).hasSize(databaseSizeBeforeUpdate);
        Filters testFilters = filtersList.get(filtersList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingFilters() throws Exception {
        int databaseSizeBeforeUpdate = filtersRepository.findAll().size();
        filters.setId(count.incrementAndGet());

        // Create the Filters
        FiltersDTO filtersDTO = filtersMapper.toDto(filters);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiltersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filtersDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filtersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filters in the database
        List<Filters> filtersList = filtersRepository.findAll();
        assertThat(filtersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFilters() throws Exception {
        int databaseSizeBeforeUpdate = filtersRepository.findAll().size();
        filters.setId(count.incrementAndGet());

        // Create the Filters
        FiltersDTO filtersDTO = filtersMapper.toDto(filters);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiltersMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filtersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filters in the database
        List<Filters> filtersList = filtersRepository.findAll();
        assertThat(filtersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFilters() throws Exception {
        int databaseSizeBeforeUpdate = filtersRepository.findAll().size();
        filters.setId(count.incrementAndGet());

        // Create the Filters
        FiltersDTO filtersDTO = filtersMapper.toDto(filters);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiltersMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filtersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Filters in the database
        List<Filters> filtersList = filtersRepository.findAll();
        assertThat(filtersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFiltersWithPatch() throws Exception {
        // Initialize the database
        filtersRepository.saveAndFlush(filters);

        int databaseSizeBeforeUpdate = filtersRepository.findAll().size();

        // Update the filters using partial update
        Filters partialUpdatedFilters = new Filters();
        partialUpdatedFilters.setId(filters.getId());

        restFiltersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilters.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFilters))
            )
            .andExpect(status().isOk());

        // Validate the Filters in the database
        List<Filters> filtersList = filtersRepository.findAll();
        assertThat(filtersList).hasSize(databaseSizeBeforeUpdate);
        Filters testFilters = filtersList.get(filtersList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateFiltersWithPatch() throws Exception {
        // Initialize the database
        filtersRepository.saveAndFlush(filters);

        int databaseSizeBeforeUpdate = filtersRepository.findAll().size();

        // Update the filters using partial update
        Filters partialUpdatedFilters = new Filters();
        partialUpdatedFilters.setId(filters.getId());

        restFiltersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilters.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFilters))
            )
            .andExpect(status().isOk());

        // Validate the Filters in the database
        List<Filters> filtersList = filtersRepository.findAll();
        assertThat(filtersList).hasSize(databaseSizeBeforeUpdate);
        Filters testFilters = filtersList.get(filtersList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingFilters() throws Exception {
        int databaseSizeBeforeUpdate = filtersRepository.findAll().size();
        filters.setId(count.incrementAndGet());

        // Create the Filters
        FiltersDTO filtersDTO = filtersMapper.toDto(filters);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFiltersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, filtersDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filtersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filters in the database
        List<Filters> filtersList = filtersRepository.findAll();
        assertThat(filtersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFilters() throws Exception {
        int databaseSizeBeforeUpdate = filtersRepository.findAll().size();
        filters.setId(count.incrementAndGet());

        // Create the Filters
        FiltersDTO filtersDTO = filtersMapper.toDto(filters);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiltersMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filtersDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filters in the database
        List<Filters> filtersList = filtersRepository.findAll();
        assertThat(filtersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFilters() throws Exception {
        int databaseSizeBeforeUpdate = filtersRepository.findAll().size();
        filters.setId(count.incrementAndGet());

        // Create the Filters
        FiltersDTO filtersDTO = filtersMapper.toDto(filters);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFiltersMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filtersDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Filters in the database
        List<Filters> filtersList = filtersRepository.findAll();
        assertThat(filtersList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFilters() throws Exception {
        // Initialize the database
        filtersRepository.saveAndFlush(filters);

        int databaseSizeBeforeDelete = filtersRepository.findAll().size();

        // Delete the filters
        restFiltersMockMvc
            .perform(delete(ENTITY_API_URL_ID, filters.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Filters> filtersList = filtersRepository.findAll();
        assertThat(filtersList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
