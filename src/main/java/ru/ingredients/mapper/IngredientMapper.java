package ru.ingredients.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import ru.ingredients.dto.IngredientDTO;
import ru.ingredients.entity.Ingredient;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {FunctionMapper.class, CategoryMapper.class, PercentMapper.class})
public interface IngredientMapper {
    IngredientDTO toDto(Ingredient entity);
    Ingredient toEntity(IngredientDTO dto);
}
