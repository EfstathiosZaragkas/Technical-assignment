package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Filter;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Filter entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FilterRepository extends JpaRepository<Filter, Long> {}
