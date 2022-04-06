package com.mycompany.myapp.service.impl;

import com.mycompany.myapp.domain.Crawler;
import com.mycompany.myapp.repository.CrawlerRepository;
import com.mycompany.myapp.service.CrawlerService;
import com.mycompany.myapp.service.dto.CrawlerDTO;
import com.mycompany.myapp.service.mapper.CrawlerMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final CrawlerMapper crawlerMapper;

    public CrawlerServiceImpl(CrawlerRepository crawlerRepository, CrawlerMapper crawlerMapper) {
        this.crawlerRepository = crawlerRepository;
        this.crawlerMapper = crawlerMapper;
    }

    @Override
    public CrawlerDTO save(CrawlerDTO crawlerDTO) {
        log.debug("Request to save Crawler : {}", crawlerDTO);
        Crawler crawler = crawlerMapper.toEntity(crawlerDTO);
        crawler = crawlerRepository.save(crawler);
        return crawlerMapper.toDto(crawler);
    }

    @Override
    public Optional<CrawlerDTO> partialUpdate(CrawlerDTO crawlerDTO) {
        log.debug("Request to partially update Crawler : {}", crawlerDTO);

        return crawlerRepository
            .findById(crawlerDTO.getId())
            .map(existingCrawler -> {
                crawlerMapper.partialUpdate(existingCrawler, crawlerDTO);

                return existingCrawler;
            })
            .map(crawlerRepository::save)
            .map(crawlerMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CrawlerDTO> findAll() {
        log.debug("Request to get all Crawlers");
        return crawlerRepository.findAll().stream().map(crawlerMapper::toDto).collect(Collectors.toCollection(LinkedList::new));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CrawlerDTO> findOne(Long id) {
        log.debug("Request to get Crawler : {}", id);
        return crawlerRepository.findById(id).map(crawlerMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Crawler : {}", id);
        crawlerRepository.deleteById(id);
    }
}
