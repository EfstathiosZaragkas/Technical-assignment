package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FiltersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Filters.class);
        Filters filters1 = new Filters();
        filters1.setId(1L);
        Filters filters2 = new Filters();
        filters2.setId(filters1.getId());
        assertThat(filters1).isEqualTo(filters2);
        filters2.setId(2L);
        assertThat(filters1).isNotEqualTo(filters2);
        filters1.setId(null);
        assertThat(filters1).isNotEqualTo(filters2);
    }
}
