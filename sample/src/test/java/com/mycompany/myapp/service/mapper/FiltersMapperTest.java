package com.mycompany.myapp.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FiltersMapperTest {

    private FiltersMapper filtersMapper;

    @BeforeEach
    public void setUp() {
        filtersMapper = new FiltersMapperImpl();
    }
}
