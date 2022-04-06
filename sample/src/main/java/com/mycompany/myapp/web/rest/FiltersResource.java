package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.FiltersRepository;
import com.mycompany.myapp.service.FiltersQueryService;
import com.mycompany.myapp.service.FiltersService;
import com.mycompany.myapp.service.criteria.FiltersCriteria;
import com.mycompany.myapp.service.dto.FiltersDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Filters}.
 */
@RestController
@RequestMapping("/api")
public class FiltersResource {

    private final Logger log = LoggerFactory.getLogger(FiltersResource.class);

    private static final String ENTITY_NAME = "sampleFilters";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final FiltersService filtersService;

    private final FiltersRepository filtersRepository;

    private final FiltersQueryService filtersQueryService;

    public FiltersResource(FiltersService filtersService, FiltersRepository filtersRepository, FiltersQueryService filtersQueryService) {
        this.filtersService = filtersService;
        this.filtersRepository = filtersRepository;
        this.filtersQueryService = filtersQueryService;
    }

    /**
     * {@code POST  /filters} : Create a new filters.
     *
     * @param filtersDTO the filtersDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new filtersDTO, or with status {@code 400 (Bad Request)} if the filters has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/filters")
    public ResponseEntity<FiltersDTO> createFilters(@RequestBody FiltersDTO filtersDTO) throws URISyntaxException {
        log.debug("REST request to save Filters : {}", filtersDTO);
        if (filtersDTO.getId() != null) {
            throw new BadRequestAlertException("A new filters cannot already have an ID", ENTITY_NAME, "idexists");
        }
        FiltersDTO result = filtersService.save(filtersDTO);
        return ResponseEntity
            .created(new URI("/api/filters/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /filters/:id} : Updates an existing filters.
     *
     * @param id the id of the filtersDTO to save.
     * @param filtersDTO the filtersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filtersDTO,
     * or with status {@code 400 (Bad Request)} if the filtersDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the filtersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/filters/{id}")
    public ResponseEntity<FiltersDTO> updateFilters(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FiltersDTO filtersDTO
    ) throws URISyntaxException {
        log.debug("REST request to update Filters : {}, {}", id, filtersDTO);
        if (filtersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filtersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filtersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        FiltersDTO result = filtersService.save(filtersDTO);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, filtersDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /filters/:id} : Partial updates given fields of an existing filters, field will ignore if it is null
     *
     * @param id the id of the filtersDTO to save.
     * @param filtersDTO the filtersDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated filtersDTO,
     * or with status {@code 400 (Bad Request)} if the filtersDTO is not valid,
     * or with status {@code 404 (Not Found)} if the filtersDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the filtersDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/filters/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<FiltersDTO> partialUpdateFilters(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody FiltersDTO filtersDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update Filters partially : {}, {}", id, filtersDTO);
        if (filtersDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, filtersDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!filtersRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<FiltersDTO> result = filtersService.partialUpdate(filtersDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, filtersDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /filters} : get all the filters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of filters in body.
     */
    @GetMapping("/filters")
    public ResponseEntity<List<FiltersDTO>> getAllFilters(FiltersCriteria criteria) {
        log.debug("REST request to get Filters by criteria: {}", criteria);
        List<FiltersDTO> entityList = filtersQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /filters/count} : count all the filters.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/filters/count")
    public ResponseEntity<Long> countFilters(FiltersCriteria criteria) {
        log.debug("REST request to count Filters by criteria: {}", criteria);
        return ResponseEntity.ok().body(filtersQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /filters/:id} : get the "id" filters.
     *
     * @param id the id of the filtersDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the filtersDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/filters/{id}")
    public ResponseEntity<FiltersDTO> getFilters(@PathVariable Long id) {
        log.debug("REST request to get Filters : {}", id);
        Optional<FiltersDTO> filtersDTO = filtersService.findOne(id);
        return ResponseUtil.wrapOrNotFound(filtersDTO);
    }

    /**
     * {@code DELETE  /filters/:id} : delete the "id" filters.
     *
     * @param id the id of the filtersDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/filters/{id}")
    public ResponseEntity<Void> deleteFilters(@PathVariable Long id) {
        log.debug("REST request to delete Filters : {}", id);
        filtersService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
