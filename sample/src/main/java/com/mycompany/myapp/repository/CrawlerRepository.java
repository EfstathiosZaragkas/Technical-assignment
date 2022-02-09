package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Crawler;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Crawler entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CrawlerRepository extends JpaRepository<Crawler, Long> {}
