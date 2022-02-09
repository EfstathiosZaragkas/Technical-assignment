package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Crawler;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link Crawler}.
 */
public interface CrawlerService {
    /**
     * Save a crawler.
     *
     * @param crawler the entity to save.
     * @return the persisted entity.
     */
    Crawler save(Crawler crawler);

    /**
     * Partially updates a crawler.
     *
     * @param crawler the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Crawler> partialUpdate(Crawler crawler);

    /**
     * Get all the crawlers.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Crawler> findAll(Pageable pageable);

    /**
     * Get the "id" crawler.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Crawler> findOne(Long id);

    /**
     * Delete the "id" crawler.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
