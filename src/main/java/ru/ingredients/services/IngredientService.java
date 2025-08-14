package ru.ingredients.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.ingredients.dto.CategoryDTO;
import ru.ingredients.dto.FunctionDTO;
import ru.ingredients.dto.IngredientDTO;
import ru.ingredients.dto.mapper.CategoryMapper;
import ru.ingredients.dto.mapper.FunctionMapper;
import ru.ingredients.dto.mapper.IngredientMapper;
import ru.ingredients.models.Ingredient;
import ru.ingredients.repo.CategoryRepository;
import ru.ingredients.repo.FunctionRepository;
import ru.ingredients.repo.IngredientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class IngredientService {
    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private FunctionRepository functionRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private FunctionMapper functionMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private IngredientMapper ingredientMapper;


    public List<FunctionDTO> getAllFunctions() {
        return this.functionRepository.findAll().stream().map(functionMapper::toDto).toList();
    }

    public List<CategoryDTO> getAllCategories() {
        return this.categoryRepository.findAll().stream().map(categoryMapper::toDto).toList();
    }

    public List<FunctionDTO> getFuncById(List<Long> funcIds) {
        return this.functionRepository.findAllById(funcIds).stream().map(functionMapper::toDto).toList();
    }

    public List<CategoryDTO> getCatById(List<Long> catIds) {
        return this.categoryRepository.findAllById(catIds).stream().map(categoryMapper::toDto).toList();
    }

    public List<Map<String, String>> getIngredientsForAutocomplete() {
        return ingredientRepository.getIngredientsForAutocomplete();
    }

    public IngredientDTO getIngredientById(long id) {
        return ingredientRepository.findById(id).map(ingredientMapper::toDto).orElseThrow();
    }

    public Page<IngredientDTO> getIngredients(String search, int pageNumber, int pageSize, List<Long> functions, List<Long> categories) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        if ((search == null || search.isEmpty()) && functions == null && categories == null) {
            return ingredientRepository.findAll(pageable).map(ingredientMapper::toDto);
        } else {
            search = search == null ? "" : search.toLowerCase().replaceAll("[^a-zA-Zа-яА-Я0-9]+", "");
            return ingredientRepository.searchIngredients(search, functions, categories, pageable).map(ingredientMapper::toDto);
        }
    }

    public void saveIngredient(IngredientDTO ingToSave) {
        List<Ingredient> existingIng = getIngWithSameNames(ingToSave);

        if (!existingIng.isEmpty()) {
            String ingIds = existingIng.stream().map(Ingredient::getId).map(String::valueOf).collect(Collectors.joining(", "));
            throw new IllegalArgumentException("Перепроверьте все указанные наименования, есть совпадения с id " + ingIds);
        }

        ingredientRepository.save(ingredientMapper.toEntity(ingToSave));
    }

    public void deleteIngredient(long id) {
        if (ingredientRepository.existsById(id)) {
            ingredientRepository.deleteById(id);
        } else throw new NoSuchElementException();
    }

    public List<Ingredient> getIngWithSameNames(IngredientDTO ingToSave) {
        List<String> allNames = new ArrayList<>(ingToSave.getOtherNames());
        allNames.add(ingToSave.getInci());
        allNames.add(ingToSave.getTradeName());

        List<Ingredient> existingIng = ingredientRepository.findByAllNames(allNames);
        existingIng.removeIf(i -> i.getId().equals(ingToSave.getId()));
        return existingIng;
    }
}
