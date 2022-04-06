package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Crawler;
import com.mycompany.myapp.repository.CrawlerRepository;
import com.mycompany.myapp.service.criteria.CrawlerCriteria;
import com.mycompany.myapp.service.dto.CrawlerDTO;
import com.mycompany.myapp.service.mapper.CrawlerMapper;
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
 * Integration tests for the {@link CrawlerResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CrawlerResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_FETCH = -1;
    private static final Integer UPDATED_FETCH = 0;
    private static final Integer SMALLER_FETCH = -1 - 1;

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/crawlers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CrawlerRepository crawlerRepository;

    @Autowired
    private CrawlerMapper crawlerMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCrawlerMockMvc;

    private Crawler crawler;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Crawler createEntity(EntityManager em) {
        Crawler crawler = new Crawler().name(DEFAULT_NAME).fetch(DEFAULT_FETCH).source(DEFAULT_SOURCE);
        return crawler;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Crawler createUpdatedEntity(EntityManager em) {
        Crawler crawler = new Crawler().name(UPDATED_NAME).fetch(UPDATED_FETCH).source(UPDATED_SOURCE);
        return crawler;
    }

    @BeforeEach
    public void initTest() {
        crawler = createEntity(em);
    }

    @Test
    @Transactional
    void createCrawler() throws Exception {
        int databaseSizeBeforeCreate = crawlerRepository.findAll().size();
        // Create the Crawler
        CrawlerDTO crawlerDTO = crawlerMapper.toDto(crawler);
        restCrawlerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crawlerDTO))
            )
            .andExpect(status().isCreated());

        // Validate the Crawler in the database
        List<Crawler> crawlerList = crawlerRepository.findAll();
        assertThat(crawlerList).hasSize(databaseSizeBeforeCreate + 1);
        Crawler testCrawler = crawlerList.get(crawlerList.size() - 1);
        assertThat(testCrawler.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCrawler.getFetch()).isEqualTo(DEFAULT_FETCH);
        assertThat(testCrawler.getSource()).isEqualTo(DEFAULT_SOURCE);
    }

    @Test
    @Transactional
    void createCrawlerWithExistingId() throws Exception {
        // Create the Crawler with an existing ID
        crawler.setId(1L);
        CrawlerDTO crawlerDTO = crawlerMapper.toDto(crawler);

        int databaseSizeBeforeCreate = crawlerRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCrawlerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crawlerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Crawler in the database
        List<Crawler> crawlerList = crawlerRepository.findAll();
        assertThat(crawlerList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = crawlerRepository.findAll().size();
        // set the field null
        crawler.setName(null);

        // Create the Crawler, which fails.
        CrawlerDTO crawlerDTO = crawlerMapper.toDto(crawler);

        restCrawlerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crawlerDTO))
            )
            .andExpect(status().isBadRequest());

        List<Crawler> crawlerList = crawlerRepository.findAll();
        assertThat(crawlerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFetchIsRequired() throws Exception {
        int databaseSizeBeforeTest = crawlerRepository.findAll().size();
        // set the field null
        crawler.setFetch(null);

        // Create the Crawler, which fails.
        CrawlerDTO crawlerDTO = crawlerMapper.toDto(crawler);

        restCrawlerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crawlerDTO))
            )
            .andExpect(status().isBadRequest());

        List<Crawler> crawlerList = crawlerRepository.findAll();
        assertThat(crawlerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSourceIsRequired() throws Exception {
        int databaseSizeBeforeTest = crawlerRepository.findAll().size();
        // set the field null
        crawler.setSource(null);

        // Create the Crawler, which fails.
        CrawlerDTO crawlerDTO = crawlerMapper.toDto(crawler);

        restCrawlerMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crawlerDTO))
            )
            .andExpect(status().isBadRequest());

        List<Crawler> crawlerList = crawlerRepository.findAll();
        assertThat(crawlerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCrawlers() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList
        restCrawlerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crawler.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fetch").value(hasItem(DEFAULT_FETCH)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)));
    }

    @Test
    @Transactional
    void getCrawler() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get the crawler
        restCrawlerMockMvc
            .perform(get(ENTITY_API_URL_ID, crawler.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(crawler.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.fetch").value(DEFAULT_FETCH))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE));
    }

    @Test
    @Transactional
    void getCrawlersByIdFiltering() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        Long id = crawler.getId();

        defaultCrawlerShouldBeFound("id.equals=" + id);
        defaultCrawlerShouldNotBeFound("id.notEquals=" + id);

        defaultCrawlerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCrawlerShouldNotBeFound("id.greaterThan=" + id);

        defaultCrawlerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCrawlerShouldNotBeFound("id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCrawlersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where name equals to DEFAULT_NAME
        defaultCrawlerShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the crawlerList where name equals to UPDATED_NAME
        defaultCrawlerShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCrawlersByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where name not equals to DEFAULT_NAME
        defaultCrawlerShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the crawlerList where name not equals to UPDATED_NAME
        defaultCrawlerShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCrawlersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCrawlerShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the crawlerList where name equals to UPDATED_NAME
        defaultCrawlerShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCrawlersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where name is not null
        defaultCrawlerShouldBeFound("name.specified=true");

        // Get all the crawlerList where name is null
        defaultCrawlerShouldNotBeFound("name.specified=false");
    }

    @Test
    @Transactional
    void getAllCrawlersByNameContainsSomething() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where name contains DEFAULT_NAME
        defaultCrawlerShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the crawlerList where name contains UPDATED_NAME
        defaultCrawlerShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCrawlersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where name does not contain DEFAULT_NAME
        defaultCrawlerShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the crawlerList where name does not contain UPDATED_NAME
        defaultCrawlerShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllCrawlersByFetchIsEqualToSomething() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where fetch equals to DEFAULT_FETCH
        defaultCrawlerShouldBeFound("fetch.equals=" + DEFAULT_FETCH);

        // Get all the crawlerList where fetch equals to UPDATED_FETCH
        defaultCrawlerShouldNotBeFound("fetch.equals=" + UPDATED_FETCH);
    }

    @Test
    @Transactional
    void getAllCrawlersByFetchIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where fetch not equals to DEFAULT_FETCH
        defaultCrawlerShouldNotBeFound("fetch.notEquals=" + DEFAULT_FETCH);

        // Get all the crawlerList where fetch not equals to UPDATED_FETCH
        defaultCrawlerShouldBeFound("fetch.notEquals=" + UPDATED_FETCH);
    }

    @Test
    @Transactional
    void getAllCrawlersByFetchIsInShouldWork() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where fetch in DEFAULT_FETCH or UPDATED_FETCH
        defaultCrawlerShouldBeFound("fetch.in=" + DEFAULT_FETCH + "," + UPDATED_FETCH);

        // Get all the crawlerList where fetch equals to UPDATED_FETCH
        defaultCrawlerShouldNotBeFound("fetch.in=" + UPDATED_FETCH);
    }

    @Test
    @Transactional
    void getAllCrawlersByFetchIsNullOrNotNull() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where fetch is not null
        defaultCrawlerShouldBeFound("fetch.specified=true");

        // Get all the crawlerList where fetch is null
        defaultCrawlerShouldNotBeFound("fetch.specified=false");
    }

    @Test
    @Transactional
    void getAllCrawlersByFetchIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where fetch is greater than or equal to DEFAULT_FETCH
        defaultCrawlerShouldBeFound("fetch.greaterThanOrEqual=" + DEFAULT_FETCH);

        // Get all the crawlerList where fetch is greater than or equal to UPDATED_FETCH
        defaultCrawlerShouldNotBeFound("fetch.greaterThanOrEqual=" + UPDATED_FETCH);
    }

    @Test
    @Transactional
    void getAllCrawlersByFetchIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where fetch is less than or equal to DEFAULT_FETCH
        defaultCrawlerShouldBeFound("fetch.lessThanOrEqual=" + DEFAULT_FETCH);

        // Get all the crawlerList where fetch is less than or equal to SMALLER_FETCH
        defaultCrawlerShouldNotBeFound("fetch.lessThanOrEqual=" + SMALLER_FETCH);
    }

    @Test
    @Transactional
    void getAllCrawlersByFetchIsLessThanSomething() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where fetch is less than DEFAULT_FETCH
        defaultCrawlerShouldNotBeFound("fetch.lessThan=" + DEFAULT_FETCH);

        // Get all the crawlerList where fetch is less than UPDATED_FETCH
        defaultCrawlerShouldBeFound("fetch.lessThan=" + UPDATED_FETCH);
    }

    @Test
    @Transactional
    void getAllCrawlersByFetchIsGreaterThanSomething() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where fetch is greater than DEFAULT_FETCH
        defaultCrawlerShouldNotBeFound("fetch.greaterThan=" + DEFAULT_FETCH);

        // Get all the crawlerList where fetch is greater than SMALLER_FETCH
        defaultCrawlerShouldBeFound("fetch.greaterThan=" + SMALLER_FETCH);
    }

    @Test
    @Transactional
    void getAllCrawlersBySourceIsEqualToSomething() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where source equals to DEFAULT_SOURCE
        defaultCrawlerShouldBeFound("source.equals=" + DEFAULT_SOURCE);

        // Get all the crawlerList where source equals to UPDATED_SOURCE
        defaultCrawlerShouldNotBeFound("source.equals=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllCrawlersBySourceIsNotEqualToSomething() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where source not equals to DEFAULT_SOURCE
        defaultCrawlerShouldNotBeFound("source.notEquals=" + DEFAULT_SOURCE);

        // Get all the crawlerList where source not equals to UPDATED_SOURCE
        defaultCrawlerShouldBeFound("source.notEquals=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllCrawlersBySourceIsInShouldWork() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where source in DEFAULT_SOURCE or UPDATED_SOURCE
        defaultCrawlerShouldBeFound("source.in=" + DEFAULT_SOURCE + "," + UPDATED_SOURCE);

        // Get all the crawlerList where source equals to UPDATED_SOURCE
        defaultCrawlerShouldNotBeFound("source.in=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllCrawlersBySourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where source is not null
        defaultCrawlerShouldBeFound("source.specified=true");

        // Get all the crawlerList where source is null
        defaultCrawlerShouldNotBeFound("source.specified=false");
    }

    @Test
    @Transactional
    void getAllCrawlersBySourceContainsSomething() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where source contains DEFAULT_SOURCE
        defaultCrawlerShouldBeFound("source.contains=" + DEFAULT_SOURCE);

        // Get all the crawlerList where source contains UPDATED_SOURCE
        defaultCrawlerShouldNotBeFound("source.contains=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllCrawlersBySourceNotContainsSomething() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        // Get all the crawlerList where source does not contain DEFAULT_SOURCE
        defaultCrawlerShouldNotBeFound("source.doesNotContain=" + DEFAULT_SOURCE);

        // Get all the crawlerList where source does not contain UPDATED_SOURCE
        defaultCrawlerShouldBeFound("source.doesNotContain=" + UPDATED_SOURCE);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCrawlerShouldBeFound(String filter) throws Exception {
        restCrawlerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(crawler.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].fetch").value(hasItem(DEFAULT_FETCH)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)));

        // Check, that the count call also returns 1
        restCrawlerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCrawlerShouldNotBeFound(String filter) throws Exception {
        restCrawlerMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCrawlerMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCrawler() throws Exception {
        // Get the crawler
        restCrawlerMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCrawler() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        int databaseSizeBeforeUpdate = crawlerRepository.findAll().size();

        // Update the crawler
        Crawler updatedCrawler = crawlerRepository.findById(crawler.getId()).get();
        // Disconnect from session so that the updates on updatedCrawler are not directly saved in db
        em.detach(updatedCrawler);
        updatedCrawler.name(UPDATED_NAME).fetch(UPDATED_FETCH).source(UPDATED_SOURCE);
        CrawlerDTO crawlerDTO = crawlerMapper.toDto(updatedCrawler);

        restCrawlerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crawlerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crawlerDTO))
            )
            .andExpect(status().isOk());

        // Validate the Crawler in the database
        List<Crawler> crawlerList = crawlerRepository.findAll();
        assertThat(crawlerList).hasSize(databaseSizeBeforeUpdate);
        Crawler testCrawler = crawlerList.get(crawlerList.size() - 1);
        assertThat(testCrawler.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCrawler.getFetch()).isEqualTo(UPDATED_FETCH);
        assertThat(testCrawler.getSource()).isEqualTo(UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void putNonExistingCrawler() throws Exception {
        int databaseSizeBeforeUpdate = crawlerRepository.findAll().size();
        crawler.setId(count.incrementAndGet());

        // Create the Crawler
        CrawlerDTO crawlerDTO = crawlerMapper.toDto(crawler);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrawlerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, crawlerDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crawlerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Crawler in the database
        List<Crawler> crawlerList = crawlerRepository.findAll();
        assertThat(crawlerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCrawler() throws Exception {
        int databaseSizeBeforeUpdate = crawlerRepository.findAll().size();
        crawler.setId(count.incrementAndGet());

        // Create the Crawler
        CrawlerDTO crawlerDTO = crawlerMapper.toDto(crawler);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrawlerMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crawlerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Crawler in the database
        List<Crawler> crawlerList = crawlerRepository.findAll();
        assertThat(crawlerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCrawler() throws Exception {
        int databaseSizeBeforeUpdate = crawlerRepository.findAll().size();
        crawler.setId(count.incrementAndGet());

        // Create the Crawler
        CrawlerDTO crawlerDTO = crawlerMapper.toDto(crawler);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrawlerMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(crawlerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Crawler in the database
        List<Crawler> crawlerList = crawlerRepository.findAll();
        assertThat(crawlerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCrawlerWithPatch() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        int databaseSizeBeforeUpdate = crawlerRepository.findAll().size();

        // Update the crawler using partial update
        Crawler partialUpdatedCrawler = new Crawler();
        partialUpdatedCrawler.setId(crawler.getId());

        partialUpdatedCrawler.source(UPDATED_SOURCE);

        restCrawlerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrawler.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrawler))
            )
            .andExpect(status().isOk());

        // Validate the Crawler in the database
        List<Crawler> crawlerList = crawlerRepository.findAll();
        assertThat(crawlerList).hasSize(databaseSizeBeforeUpdate);
        Crawler testCrawler = crawlerList.get(crawlerList.size() - 1);
        assertThat(testCrawler.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCrawler.getFetch()).isEqualTo(DEFAULT_FETCH);
        assertThat(testCrawler.getSource()).isEqualTo(UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void fullUpdateCrawlerWithPatch() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        int databaseSizeBeforeUpdate = crawlerRepository.findAll().size();

        // Update the crawler using partial update
        Crawler partialUpdatedCrawler = new Crawler();
        partialUpdatedCrawler.setId(crawler.getId());

        partialUpdatedCrawler.name(UPDATED_NAME).fetch(UPDATED_FETCH).source(UPDATED_SOURCE);

        restCrawlerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCrawler.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCrawler))
            )
            .andExpect(status().isOk());

        // Validate the Crawler in the database
        List<Crawler> crawlerList = crawlerRepository.findAll();
        assertThat(crawlerList).hasSize(databaseSizeBeforeUpdate);
        Crawler testCrawler = crawlerList.get(crawlerList.size() - 1);
        assertThat(testCrawler.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCrawler.getFetch()).isEqualTo(UPDATED_FETCH);
        assertThat(testCrawler.getSource()).isEqualTo(UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void patchNonExistingCrawler() throws Exception {
        int databaseSizeBeforeUpdate = crawlerRepository.findAll().size();
        crawler.setId(count.incrementAndGet());

        // Create the Crawler
        CrawlerDTO crawlerDTO = crawlerMapper.toDto(crawler);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCrawlerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, crawlerDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crawlerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Crawler in the database
        List<Crawler> crawlerList = crawlerRepository.findAll();
        assertThat(crawlerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCrawler() throws Exception {
        int databaseSizeBeforeUpdate = crawlerRepository.findAll().size();
        crawler.setId(count.incrementAndGet());

        // Create the Crawler
        CrawlerDTO crawlerDTO = crawlerMapper.toDto(crawler);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrawlerMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crawlerDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Crawler in the database
        List<Crawler> crawlerList = crawlerRepository.findAll();
        assertThat(crawlerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCrawler() throws Exception {
        int databaseSizeBeforeUpdate = crawlerRepository.findAll().size();
        crawler.setId(count.incrementAndGet());

        // Create the Crawler
        CrawlerDTO crawlerDTO = crawlerMapper.toDto(crawler);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCrawlerMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(crawlerDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Crawler in the database
        List<Crawler> crawlerList = crawlerRepository.findAll();
        assertThat(crawlerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCrawler() throws Exception {
        // Initialize the database
        crawlerRepository.saveAndFlush(crawler);

        int databaseSizeBeforeDelete = crawlerRepository.findAll().size();

        // Delete the crawler
        restCrawlerMockMvc
            .perform(delete(ENTITY_API_URL_ID, crawler.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Crawler> crawlerList = crawlerRepository.findAll();
        assertThat(crawlerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
