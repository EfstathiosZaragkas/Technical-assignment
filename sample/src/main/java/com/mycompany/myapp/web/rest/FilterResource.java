package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.FilterRepository;
import com.mycompany.myapp.service.FilterService;
import com.mycompany.myapp.service.dto.FilterDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Filter}.
 */
@RestController
@RequestMapping("/api")
public class FilterResource {

    private final Logger log = LoggerFactory.getLogger(FilterResource.class);

    private static final String ENTITY_NAME = "postgresFilter";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FilterService filterService;

    private final FilterRepository filterRepository;

    public FilterResource(FilterService filterService, FilterRepository filterRepository) {
        this.filterService = filterService;
        this.filterRepository = filterRepository;
    }

    /**
     * {@code POST  /filters} : Create a new filter.
     *
     * @param filterDTO the filterDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new filterDTO, or with status {@code 400 (Bad Request)} if the filter has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/filters")
    public ResponseEntity<FilterDTO> createFilter(@RequestBody FilterDTO filterDTO) throws URISyntaxException {
        log.debug("REST request to save Filter : {}", filterDTO);
        if (filterDTO.getId() != null) {
            throw new BadRequestAlertException("A new filter cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FilterDTO result = filterService.save(filterDTO);
        return ResponseEntity
            .created(new URI("/api/filters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /filters/:id} : Updates an existing filter.
     *
     * @param id the id of the filterDTO to save.
     * @param filterDTO the filterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filterDTO,
     * or with status {@code 400 (Bad Request)} if the filterDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the filterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/filters/{id}")
    public ResponseEntity<FilterDTO> updateFilter(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FilterDTO filterDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Filter : {}, {}", id, filterDTO);
        if (filterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FilterDTO result = filterService.save(filterDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, filterDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /filters/:id} : Partial updates given fields of an existing filter, field will ignore if it is null
     *
     * @param id the id of the filterDTO to save.
     * @param filterDTO the filterDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filterDTO,
     * or with status {@code 400 (Bad Request)} if the filterDTO is not valid,
     * or with status {@code 404 (Not Found)} if the filterDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the filterDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/filters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FilterDTO> partialUpdateFilter(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FilterDTO filterDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Filter partially : {}, {}", id, filterDTO);
        if (filterDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filterDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filterRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FilterDTO> result = filterService.partialUpdate(filterDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, filterDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /filters} : get all the filters.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of filters in body.
     */
    @GetMapping("/filters")
    public List<FilterDTO> getAllFilters() {
        log.debug("REST request to get all Filters");
        return filterService.findAll();
    }

    /**
     * {@code GET  /filters/:id} : get the "id" filter.
     *
     * @param id the id of the filterDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the filterDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/filters/{id}")
    public ResponseEntity<FilterDTO> getFilter(@PathVariable Long id) {
        log.debug("REST request to get Filter : {}", id);
        Optional<FilterDTO> filterDTO = filterService.findOne(id);
        return ResponseUtil.wrapOrNotFound(filterDTO);
    }

    /**
     * {@code DELETE  /filters/:id} : delete the "id" filter.
     *
     * @param id the id of the filterDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/filters/{id}")
    public ResponseEntity<Void> deleteFilter(@PathVariable Long id) {
        log.debug("REST request to delete Filter : {}", id);
        filterService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
