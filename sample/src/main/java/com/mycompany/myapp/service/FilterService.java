package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Filter;
import com.mycompany.myapp.repository.FilterRepository;
import com.mycompany.myapp.service.dto.FilterDTO;
import com.mycompany.myapp.service.mapper.FilterMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Filter}.
 */
@Service
@Transactional
public class FilterService {

    private final Logger log = LoggerFactory.getLogger(FilterService.class);

    private final FilterRepository filterRepository;

    private final FilterMapper filterMapper;

    public FilterService(FilterRepository filterRepository, FilterMapper filterMapper) {
        this.filterRepository = filterRepository;
        this.filterMapper = filterMapper;
    }

    /**
     * Save a filter.
     *
     * @param filterDTO the entity to save.
     * @return the persisted entity.
     */
    public FilterDTO save(FilterDTO filterDTO) {
        log.debug("Request to save Filter : {}", filterDTO);
        Filter filter = filterMapper.toEntity(filterDTO);
        filter = filterRepository.save(filter);
        return filterMapper.toDto(filter);
    }

    /**
     * Partially update a filter.
     *
     * @param filterDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<FilterDTO> partialUpdate(FilterDTO filterDTO) {
        log.debug("Request to partially update Filter : {}", filterDTO);

        return filterRepository
            .findById(filterDTO.getId())
            .map(existingFilter -> {
                filterMapper.partialUpdate(existingFilter, filterDTO);

                return existingFilter;
            })
            .map(filterRepository::save)
            .map(filterMapper::toDto);
    }

    /**
     * Get all the filters.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FilterDTO> findAll() {
        log.debug("Request to get all Filters");
        return filterRepository.findAll().stream().map(filterMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one filter by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FilterDTO> findOne(Long id) {
        log.debug("Request to get Filter : {}", id);
        return filterRepository.findById(id).map(filterMapper::toDto);
    }

    /**
     * Delete the filter by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Filter : {}", id);
        filterRepository.deleteById(id);
    }
}
