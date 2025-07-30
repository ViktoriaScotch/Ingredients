package ru.ingredients.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import ru.ingredients.models.Category;
import ru.ingredients.models.Function;
import ru.ingredients.models.Ingredient;
import ru.ingredients.repo.IngredientRepository;

import java.util.*;

@Service
public class DecodingService {

    @Autowired
    private IngredientRepository ingredientRepository;

    private static final String REGEX_INSIDE_PARENTHESES = "\\(.+?\\)";
    private static final String REGEX_NON_ALPHANUMERIC = "[^a-zA-Zа-яА-Я0-9]+";

    public List<Ingredient> findIng(String text) {
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
        return ingredients;
    }

    public Map<String, List<Ingredient>> groupByFunc(List<Ingredient> ingredients) {
        Map<String, List<Ingredient>> ingByFunc = new HashMap<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getId() != null) {
                for (Function func : ingredient.getFunctions()) {
                    ingByFunc.computeIfAbsent(func.getName(), k -> new ArrayList<>()).add(ingredient);
                }
            }
        }
        return ingByFunc;
    }

    public Map<String, List<Ingredient>> groupByCat(List<Ingredient> ingredients) {
        Map<String, List<Ingredient>> ingByCat = new HashMap<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getId() != null) {
                for (Category cat : ingredient.getCategories()) {
                    ingByCat.computeIfAbsent(cat.getName(), k -> new ArrayList<>()).add(ingredient);
                }
            }
        }
        return ingByCat;
    }
}
