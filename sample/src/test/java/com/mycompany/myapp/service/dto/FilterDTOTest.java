package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FilterDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FilterDTO.class);
        FilterDTO filterDTO1 = new FilterDTO();
        filterDTO1.setId(1L);
        FilterDTO filterDTO2 = new FilterDTO();
        assertThat(filterDTO1).isNotEqualTo(filterDTO2);
        filterDTO2.setId(filterDTO1.getId());
        assertThat(filterDTO1).isEqualTo(filterDTO2);
        filterDTO2.setId(2L);
        assertThat(filterDTO1).isNotEqualTo(filterDTO2);
        filterDTO1.setId(null);
        assertThat(filterDTO1).isNotEqualTo(filterDTO2);
    }
}
