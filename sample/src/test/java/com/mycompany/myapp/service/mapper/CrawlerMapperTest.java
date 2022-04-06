package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CrawlerMapperTest {

    private CrawlerMapper crawlerMapper;

    @BeforeEach
    public void setUp() {
        crawlerMapper = new CrawlerMapperImpl();
    }
}
