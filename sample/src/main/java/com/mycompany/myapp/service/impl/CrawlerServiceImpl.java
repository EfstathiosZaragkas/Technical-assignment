package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Crawler;
import com.mycompany.myapp.repository.CrawlerRepository;
import com.mycompany.myapp.service.CrawlerService;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Crawler}.
 */
@Service
@Transactional
public class CrawlerServiceImpl implements CrawlerService {

    private final Logger log = LoggerFactory.getLogger(CrawlerServiceImpl.class);

    private final CrawlerRepository crawlerRepository;

    public CrawlerServiceImpl(CrawlerRepository crawlerRepository) {
        this.crawlerRepository = crawlerRepository;
    }

    @Override
    public Crawler save(Crawler crawler) {
        log.debug("Request to save Crawler : {}", crawler);
        return crawlerRepository.save(crawler);
    }

    @Override
    public Optional<Crawler> partialUpdate(Crawler crawler) {
        log.debug("Request to partially update Crawler : {}", crawler);

        return crawlerRepository
            .findById(crawler.getId())
            .map(existingCrawler -> {
                if (crawler.getName() != null) {
                    existingCrawler.setName(crawler.getName());
                }
                if (crawler.getFetch() != null) {
                    existingCrawler.setFetch(crawler.getFetch());
                }
                if (crawler.getSource() != null) {
                    existingCrawler.setSource(crawler.getSource());
                }

                return existingCrawler;
            })
            .map(crawlerRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Crawler> findAll(Pageable pageable) {
        log.debug("Request to get all Crawlers");
        return crawlerRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Crawler> findOne(Long id) {
        log.debug("Request to get Crawler : {}", id);
        return crawlerRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Crawler : {}", id);
        crawlerRepository.deleteById(id);
    }
}
