package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CrawlerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Crawler.class);
        Crawler crawler1 = new Crawler();
        crawler1.setId(1L);
        Crawler crawler2 = new Crawler();
        crawler2.setId(crawler1.getId());
        assertThat(crawler1).isEqualTo(crawler2);
        crawler2.setId(2L);
        assertThat(crawler1).isNotEqualTo(crawler2);
        crawler1.setId(null);
        assertThat(crawler1).isNotEqualTo(crawler2);
    }
}
