package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FilterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Filter.class);
        Filter filter1 = new Filter();
        filter1.setId(1L);
        Filter filter2 = new Filter();
        filter2.setId(filter1.getId());
        assertThat(filter1).isEqualTo(filter2);
        filter2.setId(2L);
        assertThat(filter1).isNotEqualTo(filter2);
        filter1.setId(null);
        assertThat(filter1).isNotEqualTo(filter2);
    }
}
