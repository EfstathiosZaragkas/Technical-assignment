package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;

/**
 * A Crawler.
 */
@Entity
@Table(name = "crawler")
public class Crawler implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "jhi_fetch")
    private Integer fetch;

    @Column(name = "source")
    private String source;

    @OneToMany(mappedBy = "crawler")
    @JsonIgnoreProperties(value = { "crawler" }, allowSetters = true)
    private Set<Filter> filters = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Crawler id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Crawler name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFetch() {
        return this.fetch;
    }

    public Crawler fetch(Integer fetch) {
        this.setFetch(fetch);
        return this;
    }

    public void setFetch(Integer fetch) {
        this.fetch = fetch;
    }

    public String getSource() {
        return this.source;
    }

    public Crawler source(String source) {
        this.setSource(source);
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Set<Filter> getFilters() {
        return this.filters;
    }

    public void setFilters(Set<Filter> filters) {
        if (this.filters != null) {
            this.filters.forEach(i -> i.setCrawler(null));
        }
        if (filters != null) {
            filters.forEach(i -> i.setCrawler(this));
        }
        this.filters = filters;
    }

    public Crawler filters(Set<Filter> filters) {
        this.setFilters(filters);
        return this;
    }

    public Crawler addFilter(Filter filter) {
        this.filters.add(filter);
        filter.setCrawler(this);
        return this;
    }

    public Crawler removeFilter(Filter filter) {
        this.filters.remove(filter);
        filter.setCrawler(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Crawler)) {
            return false;
        }
        return id != null && id.equals(((Crawler) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Crawler{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", fetch=" + getFetch() +
            ", source='" + getSource() + "'" +
            "}";
    }
}
