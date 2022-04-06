package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Filters;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Filters entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FiltersRepository extends JpaRepository<Filters, Long>, JpaSpecificationExecutor<Filters> {}
