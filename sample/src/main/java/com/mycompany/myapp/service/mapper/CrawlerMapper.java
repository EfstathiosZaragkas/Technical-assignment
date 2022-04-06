package com.mycompany.myapp.service.mapper;

import com.mycompany.myapp.domain.Crawler;
import com.mycompany.myapp.service.dto.CrawlerDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Crawler} and its DTO {@link CrawlerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface CrawlerMapper extends EntityMapper<CrawlerDTO, Crawler> {
    @Named("id")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CrawlerDTO toDtoId(Crawler crawler);
}
