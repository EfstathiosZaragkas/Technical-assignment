package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Filters} entity.
 */
public class FiltersDTO implements Serializable {

    private Long id;

    private CrawlerDTO crawler;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public CrawlerDTO getCrawler() {
        return crawler;
    }

    public void setCrawler(CrawlerDTO crawler) {
        this.crawler = crawler;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FiltersDTO)) {
            return false;
        }

        FiltersDTO filtersDTO = (FiltersDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, filtersDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FiltersDTO{" +
            "id=" + getId() +
            ", crawler=" + getCrawler() +
            "}";
    }
}
