package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Crawler;
import com.mycompany.myapp.repository.CrawlerRepository;
import com.mycompany.myapp.service.criteria.CrawlerCriteria;
import com.mycompany.myapp.service.dto.CrawlerDTO;
import com.mycompany.myapp.service.mapper.CrawlerMapper;
import java.util.List;
import javax.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Crawler} entities in the database.
 * The main input is a {@link CrawlerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link CrawlerDTO} or a {@link Page} of {@link CrawlerDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CrawlerQueryService extends QueryService<Crawler> {

    private final Logger log = LoggerFactory.getLogger(CrawlerQueryService.class);

    private final CrawlerRepository crawlerRepository;

    private final CrawlerMapper crawlerMapper;

    public CrawlerQueryService(CrawlerRepository crawlerRepository, CrawlerMapper crawlerMapper) {
        this.crawlerRepository = crawlerRepository;
        this.crawlerMapper = crawlerMapper;
    }

    /**
     * Return a {@link List} of {@link CrawlerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<CrawlerDTO> findByCriteria(CrawlerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Crawler> specification = createSpecification(criteria);
        return crawlerMapper.toDto(crawlerRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link CrawlerDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<CrawlerDTO> findByCriteria(CrawlerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Crawler> specification = createSpecification(criteria);
        return crawlerRepository.findAll(specification, page).map(crawlerMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CrawlerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Crawler> specification = createSpecification(criteria);
        return crawlerRepository.count(specification);
    }

    /**
     * Function to convert {@link CrawlerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Crawler> createSpecification(CrawlerCriteria criteria) {
        Specification<Crawler> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Crawler_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Crawler_.name));
            }
            if (criteria.getFetch() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getFetch(), Crawler_.fetch));
            }
            if (criteria.getSource() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSource(), Crawler_.source));
            }
        }
        return specification;
    }
}
