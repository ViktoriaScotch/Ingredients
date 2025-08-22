package ru.ingredients.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import ru.ingredients.dto.FunctionDTO;
import ru.ingredients.mapper.FunctionMapper;
import ru.ingredients.repository.FunctionRepository;

@Component
public class StringToFunctionDTOConverter implements Converter<String, FunctionDTO> {
    @Autowired
    private FunctionRepository functionRepository;
    @Autowired
    private FunctionMapper functionMapper;

    @Override
    public FunctionDTO convert(@NonNull String source) {
        Long id = Long.parseLong(source);
        return functionRepository.findById(id).map(functionMapper::toDto).orElseThrow();
    }
}
