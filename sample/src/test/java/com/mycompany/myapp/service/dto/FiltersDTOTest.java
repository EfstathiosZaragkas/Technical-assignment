package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FiltersDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FiltersDTO.class);
        FiltersDTO filtersDTO1 = new FiltersDTO();
        filtersDTO1.setId(1L);
        FiltersDTO filtersDTO2 = new FiltersDTO();
        assertThat(filtersDTO1).isNotEqualTo(filtersDTO2);
        filtersDTO2.setId(filtersDTO1.getId());
        assertThat(filtersDTO1).isEqualTo(filtersDTO2);
        filtersDTO2.setId(2L);
        assertThat(filtersDTO1).isNotEqualTo(filtersDTO2);
        filtersDTO1.setId(null);
        assertThat(filtersDTO1).isNotEqualTo(filtersDTO2);
    }
}
