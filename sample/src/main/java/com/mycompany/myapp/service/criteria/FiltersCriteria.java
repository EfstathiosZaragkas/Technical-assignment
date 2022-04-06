package com.mycompany.myapp.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.DoubleFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.FloatFilter;
import tech.jhipster.service.filter.IntegerFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Filters} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.FiltersResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /filters?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class FiltersCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private LongFilter crawlerId;

    private Boolean distinct;

    public FiltersCriteria() {}

    public FiltersCriteria(FiltersCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.crawlerId = other.crawlerId == null ? null : other.crawlerId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public FiltersCriteria copy() {
        return new FiltersCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public LongFilter getCrawlerId() {
        return crawlerId;
    }

    public LongFilter crawlerId() {
        if (crawlerId == null) {
            crawlerId = new LongFilter();
        }
        return crawlerId;
    }

    public void setCrawlerId(LongFilter crawlerId) {
        this.crawlerId = crawlerId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final FiltersCriteria that = (FiltersCriteria) o;
        return Objects.equals(id, that.id) && Objects.equals(crawlerId, that.crawlerId) && Objects.equals(distinct, that.distinct);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, crawlerId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FiltersCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (crawlerId != null ? "crawlerId=" + crawlerId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
