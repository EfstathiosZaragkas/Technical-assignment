package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import javax.validation.constraints.*;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Crawler} entity.
 */
public class CrawlerDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @NotNull
    @Min(value = -1)
    private Integer fetch;

    @NotNull
    private String source;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFetch() {
        return fetch;
    }

    public void setFetch(Integer fetch) {
        this.fetch = fetch;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CrawlerDTO)) {
            return false;
        }

        CrawlerDTO crawlerDTO = (CrawlerDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, crawlerDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CrawlerDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", fetch=" + getFetch() +
            ", source='" + getSource() + "'" +
            "}";
    }
}
