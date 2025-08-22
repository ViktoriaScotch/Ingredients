package ru.ingredients.dto.mapper;

import org.mapstruct.Mapper;
import ru.ingredients.dto.FunctionDTO;
import ru.ingredients.models.Function;

@Mapper(componentModel = "spring")
public interface FunctionMapper {
    FunctionDTO toDto (Function entity);
    Function toEntity (FunctionDTO dto);
}
