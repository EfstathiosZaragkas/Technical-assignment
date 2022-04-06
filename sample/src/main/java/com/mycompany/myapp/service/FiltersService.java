package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Filters;
import com.mycompany.myapp.repository.FiltersRepository;
import com.mycompany.myapp.service.dto.FiltersDTO;
import com.mycompany.myapp.service.mapper.FiltersMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Filters}.
 */
@Service
@Transactional
public class FiltersService {

    private final Logger log = LoggerFactory.getLogger(FiltersService.class);

    private final FiltersRepository filtersRepository;

    private final FiltersMapper filtersMapper;

    public FiltersService(FiltersRepository filtersRepository, FiltersMapper filtersMapper) {
        this.filtersRepository = filtersRepository;
        this.filtersMapper = filtersMapper;
    }

    /**
     * Save a filters.
     *
     * @param filtersDTO the entity to save.
     * @return the persisted entity.
     */
    public FiltersDTO save(FiltersDTO filtersDTO) {
        log.debug("Request to save Filters : {}", filtersDTO);
        Filters filters = filtersMapper.toEntity(filtersDTO);
        filters = filtersRepository.save(filters);
        return filtersMapper.toDto(filters);
    }

    /**
     * Partially update a filters.
     *
     * @param filtersDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FiltersDTO> partialUpdate(FiltersDTO filtersDTO) {
        log.debug("Request to partially update Filters : {}", filtersDTO);

        return filtersRepository
            .findById(filtersDTO.getId())
            .map(existingFilters -> {
                filtersMapper.partialUpdate(existingFilters, filtersDTO);

                return existingFilters;
            })
            .map(filtersRepository::save)
            .map(filtersMapper::toDto);
    }

    /**
     * Get all the filters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FiltersDTO> findAll() {
        log.debug("Request to get all Filters");
        return filtersRepository.findAll().stream().map(filtersMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one filters by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FiltersDTO> findOne(Long id) {
        log.debug("Request to get Filters : {}", id);
        return filtersRepository.findById(id).map(filtersMapper::toDto);
    }

    /**
     * Delete the filters by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Filters : {}", id);
        filtersRepository.deleteById(id);
    }
}
