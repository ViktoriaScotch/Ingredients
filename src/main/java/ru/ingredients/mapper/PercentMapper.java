package ru.ingredients.dto.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.ingredients.dto.PercentDTO;
import ru.ingredients.models.Percent;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PercentMapper {
    PercentDTO toDto(Percent entity);
    Percent toEntity(PercentDTO dto);
}
