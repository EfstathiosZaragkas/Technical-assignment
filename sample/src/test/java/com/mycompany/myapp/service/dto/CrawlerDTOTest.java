package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CrawlerDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CrawlerDTO.class);
        CrawlerDTO crawlerDTO1 = new CrawlerDTO();
        crawlerDTO1.setId(1L);
        CrawlerDTO crawlerDTO2 = new CrawlerDTO();
        assertThat(crawlerDTO1).isNotEqualTo(crawlerDTO2);
        crawlerDTO2.setId(crawlerDTO1.getId());
        assertThat(crawlerDTO1).isEqualTo(crawlerDTO2);
        crawlerDTO2.setId(2L);
        assertThat(crawlerDTO1).isNotEqualTo(crawlerDTO2);
        crawlerDTO1.setId(null);
        assertThat(crawlerDTO1).isNotEqualTo(crawlerDTO2);
    }
}
