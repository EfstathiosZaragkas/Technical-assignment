package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.domain.Filters;
import com.mycompany.myapp.repository.FiltersRepository;
import com.mycompany.myapp.service.criteria.FiltersCriteria;
import com.mycompany.myapp.service.dto.FiltersDTO;
import com.mycompany.myapp.service.mapper.FiltersMapper;
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
 * Service for executing complex queries for {@link Filters} entities in the database.
 * The main input is a {@link FiltersCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link FiltersDTO} or a {@link Page} of {@link FiltersDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class FiltersQueryService extends QueryService<Filters> {

    private final Logger log = LoggerFactory.getLogger(FiltersQueryService.class);

    private final FiltersRepository filtersRepository;

    private final FiltersMapper filtersMapper;

    public FiltersQueryService(FiltersRepository filtersRepository, FiltersMapper filtersMapper) {
        this.filtersRepository = filtersRepository;
        this.filtersMapper = filtersMapper;
    }

    /**
     * Return a {@link List} of {@link FiltersDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<FiltersDTO> findByCriteria(FiltersCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Filters> specification = createSpecification(criteria);
        return filtersMapper.toDto(filtersRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link FiltersDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<FiltersDTO> findByCriteria(FiltersCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Filters> specification = createSpecification(criteria);
        return filtersRepository.findAll(specification, page).map(filtersMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(FiltersCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Filters> specification = createSpecification(criteria);
        return filtersRepository.count(specification);
    }

    /**
     * Function to convert {@link FiltersCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Filters> createSpecification(FiltersCriteria criteria) {
        Specification<Filters> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Filters_.id));
            }
            if (criteria.getCrawlerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCrawlerId(), root -> root.join(Filters_.crawler, JoinType.LEFT).get(Crawler_.id))
                    );
            }
        }
        return specification;
    }
}
