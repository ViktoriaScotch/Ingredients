package ru.ingredients.mapper;

import org.mapstruct.Mapper;
import ru.ingredients.dto.CategoryDTO;
import ru.ingredients.entity.Category;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDTO toDto (Category entity);
    Category toEntity (CategoryDTO dto);
}
