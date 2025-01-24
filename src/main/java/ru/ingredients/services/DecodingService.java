package ru.ingredients.services;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.stereotype.Service;
import ru.ingredients.models.Category;
import ru.ingredients.models.Function;
import ru.ingredients.models.Ingredient;
import ru.ingredients.repo.IngredientRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DecodingService {

    @Autowired
    private IngredientRepository ingredientRepository;

    public List<Ingredient> findIng(String text) {
        List<String> ingText = new ArrayList<>();
        if (text != null) {
            ingText = Arrays.stream(text.split(", ")).collect(Collectors.toList());
        }
        List<Ingredient> ingredients = new ArrayList<>();
        for (String ing : ingText) {
            Ingredient ingredient;
            try {
                ingredient = ingredientRepository.findByAllNames(ing.toLowerCase()
                        .replaceAll("\\(.+?\\)", "").replaceAll("[^a-zA-Zа-яА-Я0-9]+", ""));
            } catch (IncorrectResultSizeDataAccessException e) {
                ingredient = ingredientRepository.findByAllNames(
                        StringUtils.substringBetween(ing.toLowerCase(), "(", ")").replaceAll("[^a-zA-Zа-яА-Я0-9]+", ""));
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
                for (String func : ingredient.getFunctions().stream().map(Function::getName).collect(Collectors.toList())) {
                    if (ingByFunc.containsKey(func)) {
                        List<Ingredient> elements = new ArrayList<>(ingByFunc.get(func));
                        elements.add(ingredient);
                        ingByFunc.put(func, elements);
                    } else {
                        ingByFunc.put(func, List.of(ingredient));
                    }
                }
            }
        }
        return ingByFunc;
    }

    public Map<String, List<Ingredient>> groupByCat(List<Ingredient> ingredients) {
        Map<String, List<Ingredient>> ingByCat = new HashMap<>();
        for (Ingredient ingredient : ingredients) {
            if (ingredient.getId() != null) {
                for (String cat : ingredient.getCategories().stream().map(Category::getName).collect(Collectors.toList())) {
                    if (ingByCat.containsKey(cat)) {
                        List<Ingredient> elements = new ArrayList<>(ingByCat.get(cat));
                        elements.add(ingredient);
                        ingByCat.put(cat, elements);
                    } else {
                        ingByCat.put(cat, List.of(ingredient));
                    }
                }
            }
        }
        return ingByCat;
    }

}
