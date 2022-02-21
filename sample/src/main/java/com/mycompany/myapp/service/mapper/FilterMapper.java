package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Filter;
import com.mycompany.myapp.service.dto.FilterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Filter} and its DTO {@link FilterDTO}.
 */
@Mapper(componentModel = "spring", uses = { CrawlerMapper.class })
public interface FilterMapper extends EntityMapper<FilterDTO, Filter> {
    @Mapping(target = "crawler", source = "crawler", qualifiedByName = "id")
    FilterDTO toDto(Filter s);
}
