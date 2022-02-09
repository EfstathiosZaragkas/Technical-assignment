package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Crawler;
import com.mycompany.myapp.repository.CrawlerRepository;
import com.mycompany.myapp.service.CrawlerService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Crawler}.
 */
@RestController
@RequestMapping("/api")
public class CrawlerResource {

    private final Logger log = LoggerFactory.getLogger(CrawlerResource.class);

    private final CrawlerService crawlerService;

    private final CrawlerRepository crawlerRepository;

    public CrawlerResource(CrawlerService crawlerService, CrawlerRepository crawlerRepository) {
        this.crawlerService = crawlerService;
        this.crawlerRepository = crawlerRepository;
    }

    /**
     * {@code GET  /crawlers} : get all the crawlers.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of crawlers in body.
     */
    @GetMapping("/crawlers")
    public ResponseEntity<List<Crawler>> getAllCrawlers(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Crawlers");
        Page<Crawler> page = crawlerService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /crawlers/:id} : get the "id" crawler.
     *
     * @param id the id of the crawler to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the crawler, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/crawlers/{id}")
    public ResponseEntity<Crawler> getCrawler(@PathVariable Long id) {
        log.debug("REST request to get Crawler : {}", id);
        Optional<Crawler> crawler = crawlerService.findOne(id);
        return ResponseUtil.wrapOrNotFound(crawler);
    }
}
