package ru.ingredients.dto.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.ingredients.dto.CategoryDTO;
import ru.ingredients.dto.mapper.CategoryMapper;
import ru.ingredients.repo.CategoryRepository;

@Component
public class StringToCategoryDTOConverter implements Converter<String, CategoryDTO> {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryDTO convert(@NonNull String source) {
        Long id = Long.parseLong(source);
        return categoryRepository.findById(id).map(categoryMapper::toDto).orElseThrow();
    }
}
