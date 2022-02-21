package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Filter;
import com.mycompany.myapp.repository.FilterRepository;
import com.mycompany.myapp.service.dto.FilterDTO;
import com.mycompany.myapp.service.mapper.FilterMapper;
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
 * Integration tests for the {@link FilterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FilterResourceIT {

    private static final String ENTITY_API_URL = "/api/filters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private FilterRepository filterRepository;

    @Autowired
    private FilterMapper filterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFilterMockMvc;

    private Filter filter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filter createEntity(EntityManager em) {
        Filter filter = new Filter();
        return filter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Filter createUpdatedEntity(EntityManager em) {
        Filter filter = new Filter();
        return filter;
    }

    @BeforeEach
    public void initTest() {
        filter = createEntity(em);
    }

    @Test
    @Transactional
    void createFilter() throws Exception {
        int databaseSizeBeforeCreate = filterRepository.findAll().size();
        // Create the Filter
        FilterDTO filterDTO = filterMapper.toDto(filter);
        restFilterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filterDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Filter in the database
        List<Filter> filterList = filterRepository.findAll();
        assertThat(filterList).hasSize(databaseSizeBeforeCreate + 1);
        Filter testFilter = filterList.get(filterList.size() - 1);
    }

    @Test
    @Transactional
    void createFilterWithExistingId() throws Exception {
        // Create the Filter with an existing ID
        filter.setId(1L);
        FilterDTO filterDTO = filterMapper.toDto(filter);

        int databaseSizeBeforeCreate = filterRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFilterMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filter in the database
        List<Filter> filterList = filterRepository.findAll();
        assertThat(filterList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllFilters() throws Exception {
        // Initialize the database
        filterRepository.saveAndFlush(filter);

        // Get all the filterList
        restFilterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(filter.getId().intValue())));
    }

    @Test
    @Transactional
    void getFilter() throws Exception {
        // Initialize the database
        filterRepository.saveAndFlush(filter);

        // Get the filter
        restFilterMockMvc
            .perform(get(ENTITY_API_URL_ID, filter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(filter.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingFilter() throws Exception {
        // Get the filter
        restFilterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewFilter() throws Exception {
        // Initialize the database
        filterRepository.saveAndFlush(filter);

        int databaseSizeBeforeUpdate = filterRepository.findAll().size();

        // Update the filter
        Filter updatedFilter = filterRepository.findById(filter.getId()).get();
        // Disconnect from session so that the updates on updatedFilter are not directly saved in db
        em.detach(updatedFilter);
        FilterDTO filterDTO = filterMapper.toDto(updatedFilter);

        restFilterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filterDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filterDTO))
            )
            .andExpect(status().isOk());

        // Validate the Filter in the database
        List<Filter> filterList = filterRepository.findAll();
        assertThat(filterList).hasSize(databaseSizeBeforeUpdate);
        Filter testFilter = filterList.get(filterList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingFilter() throws Exception {
        int databaseSizeBeforeUpdate = filterRepository.findAll().size();
        filter.setId(count.incrementAndGet());

        // Create the Filter
        FilterDTO filterDTO = filterMapper.toDto(filter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, filterDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filter in the database
        List<Filter> filterList = filterRepository.findAll();
        assertThat(filterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFilter() throws Exception {
        int databaseSizeBeforeUpdate = filterRepository.findAll().size();
        filter.setId(count.incrementAndGet());

        // Create the Filter
        FilterDTO filterDTO = filterMapper.toDto(filter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filter in the database
        List<Filter> filterList = filterRepository.findAll();
        assertThat(filterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFilter() throws Exception {
        int databaseSizeBeforeUpdate = filterRepository.findAll().size();
        filter.setId(count.incrementAndGet());

        // Create the Filter
        FilterDTO filterDTO = filterMapper.toDto(filter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilterMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(filterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Filter in the database
        List<Filter> filterList = filterRepository.findAll();
        assertThat(filterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFilterWithPatch() throws Exception {
        // Initialize the database
        filterRepository.saveAndFlush(filter);

        int databaseSizeBeforeUpdate = filterRepository.findAll().size();

        // Update the filter using partial update
        Filter partialUpdatedFilter = new Filter();
        partialUpdatedFilter.setId(filter.getId());

        restFilterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilter.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFilter))
            )
            .andExpect(status().isOk());

        // Validate the Filter in the database
        List<Filter> filterList = filterRepository.findAll();
        assertThat(filterList).hasSize(databaseSizeBeforeUpdate);
        Filter testFilter = filterList.get(filterList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateFilterWithPatch() throws Exception {
        // Initialize the database
        filterRepository.saveAndFlush(filter);

        int databaseSizeBeforeUpdate = filterRepository.findAll().size();

        // Update the filter using partial update
        Filter partialUpdatedFilter = new Filter();
        partialUpdatedFilter.setId(filter.getId());

        restFilterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFilter.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedFilter))
            )
            .andExpect(status().isOk());

        // Validate the Filter in the database
        List<Filter> filterList = filterRepository.findAll();
        assertThat(filterList).hasSize(databaseSizeBeforeUpdate);
        Filter testFilter = filterList.get(filterList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingFilter() throws Exception {
        int databaseSizeBeforeUpdate = filterRepository.findAll().size();
        filter.setId(count.incrementAndGet());

        // Create the Filter
        FilterDTO filterDTO = filterMapper.toDto(filter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFilterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, filterDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filter in the database
        List<Filter> filterList = filterRepository.findAll();
        assertThat(filterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFilter() throws Exception {
        int databaseSizeBeforeUpdate = filterRepository.findAll().size();
        filter.setId(count.incrementAndGet());

        // Create the Filter
        FilterDTO filterDTO = filterMapper.toDto(filter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Filter in the database
        List<Filter> filterList = filterRepository.findAll();
        assertThat(filterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFilter() throws Exception {
        int databaseSizeBeforeUpdate = filterRepository.findAll().size();
        filter.setId(count.incrementAndGet());

        // Create the Filter
        FilterDTO filterDTO = filterMapper.toDto(filter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFilterMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(filterDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Filter in the database
        List<Filter> filterList = filterRepository.findAll();
        assertThat(filterList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFilter() throws Exception {
        // Initialize the database
        filterRepository.saveAndFlush(filter);

        int databaseSizeBeforeDelete = filterRepository.findAll().size();

        // Delete the filter
        restFilterMockMvc
            .perform(delete(ENTITY_API_URL_ID, filter.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Filter> filterList = filterRepository.findAll();
        assertThat(filterList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
