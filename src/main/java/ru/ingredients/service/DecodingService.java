package ru.ingredients.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import ru.ingredients.dto.CategoryDTO;
import ru.ingredients.dto.FunctionDTO;
import ru.ingredients.dto.IngredientDTO;
import ru.ingredients.mapper.IngredientMapper;
import ru.ingredients.entity.Ingredient;
import ru.ingredients.repository.IngredientRepository;

import java.util.*;

@Service
public class DecodingService {

    @Autowired
    private IngredientRepository ingredientRepository;
    @Autowired
    private IngredientMapper ingredientMapper;

    private static final String REGEX_INSIDE_PARENTHESES = "\\(.+?\\)";
    private static final String REGEX_NON_ALPHANUMERIC = "[^a-zA-Zа-яА-Я0-9]+";

    public List<IngredientDTO> findIng(String text) {
        List<String> ingText = new ArrayList<>();
        if (text != null) {
            ingText = Arrays.stream(text.split(", ")).toList();
        }
        List<Ingredient> ingredients = new ArrayList<>();
        for (String ing : ingText) {
            Ingredient ingredient;
            String ingLowerCase = ing.toLowerCase();
            try {
                ingredient = ingredientRepository.findByAllNames(ingLowerCase
                        .replaceAll(REGEX_INSIDE_PARENTHESES, "")
                        .replaceAll(REGEX_NON_ALPHANUMERIC, ""));
            } catch (IncorrectResultSizeDataAccessException e) {
                ingredient = ingredientRepository.findByAllNames(ingLowerCase
                        .substring(ingLowerCase.indexOf('(') + 1, ingLowerCase.indexOf(')'))
                        .replaceAll(REGEX_NON_ALPHANUMERIC, ""));
            }
            if (ingredient == null) {
                ingredients.add(new Ingredient().setTradeName(ing));
            } else {
                ingredients.add(ingredient);
            }
        }
        return ingredients.stream().map(ingredientMapper::toDto).toList();
    }

    public Map<String, List<IngredientDTO>> groupByFunc(List<IngredientDTO> ingredients) {
        Map<String, List<IngredientDTO>> ingByFunc = new HashMap<>();
        for (IngredientDTO ingredient : ingredients) {
            if (ingredient.getId() != null) {
                for (FunctionDTO func : ingredient.getFunctions()) {
                    ingByFunc.computeIfAbsent(func.getName(), k -> new ArrayList<>()).add(ingredient);
                }
            }
        }
        return ingByFunc;
    }

    public Map<String, List<IngredientDTO>> groupByCat(List<IngredientDTO> ingredients) {
        Map<String, List<IngredientDTO>> ingByCat = new HashMap<>();
        for (IngredientDTO ingredient : ingredients) {
            if (ingredient.getId() != null) {
                for (CategoryDTO cat : ingredient.getCategories()) {
                    ingByCat.computeIfAbsent(cat.getName(), k -> new ArrayList<>()).add(ingredient);
                }
            }
        }
        return ingByCat;
    }
}
