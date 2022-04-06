package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Filters;
import com.mycompany.myapp.service.dto.FiltersDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Filters} and its DTO {@link FiltersDTO}.
 */
@Mapper(componentModel = "spring", uses = { CrawlerMapper.class })
public interface FiltersMapper extends EntityMapper<FiltersDTO, Filters> {
    @Mapping(target = "crawler", source = "crawler", qualifiedByName = "id")
    FiltersDTO toDto(Filters s);
}
