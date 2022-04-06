package com.mycompany.myapp.service;

import com.mycompany.myapp.service.dto.CrawlerDTO;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link com.mycompany.myapp.domain.Crawler}.
 */
public interface CrawlerService {
    /**
     * Save a crawler.
     *
     * @param crawlerDTO the entity to save.
     * @return the persisted entity.
     */
    CrawlerDTO save(CrawlerDTO crawlerDTO);

    /**
     * Partially updates a crawler.
     *
     * @param crawlerDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CrawlerDTO> partialUpdate(CrawlerDTO crawlerDTO);

    /**
     * Get all the crawlers.
     *
     * @return the list of entities.
     */
    List<CrawlerDTO> findAll();

    /**
     * Get the "id" crawler.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CrawlerDTO> findOne(Long id);

    /**
     * Delete the "id" crawler.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
