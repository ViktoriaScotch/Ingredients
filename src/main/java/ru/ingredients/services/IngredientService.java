package ru.ingredients.services;

import org.javatuples.LabelValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.ingredients.models.Category;
import ru.ingredients.models.Function;
import ru.ingredients.models.Ingredient;
import ru.ingredients.repo.CategoryRepository;
import ru.ingredients.repo.FunctionRepository;
import ru.ingredients.repo.IngredientRepository;

import java.util.List;
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


    public List<Function> getAllFunctions() {
        return this.functionRepository.findAll();
    }

    public List<Category> getAllCategories() {
        return this.categoryRepository.findAll();
    }

    public List<LabelValue<String, String>> getIngredientsForAutocomplete() {
        return ingredientRepository.getAllNamesId().stream().map(LabelValue::fromArray).collect(Collectors.toList());
    }

    public Iterable<Ingredient> getIngredients(String search) {
        return search == null || search.isEmpty() ? ingredientRepository.findAll() :
                ingredientRepository.searchIngredients(search.toLowerCase().replaceAll("[^a-zA-Zа-яА-Я0-9]+", ""));
    }

    public Ingredient findIngredientById(long id) {
        return ingredientRepository.findById(id).orElseThrow();
    }

    public void saveIngredient(Ingredient ingredient) {
        ingredientRepository.save(ingredient);
    }

    public void deleteIngredient(long id) {
        if (ingredientRepository.existsById(id)) {
            ingredientRepository.deleteById(id);
        } else throw new NoSuchElementException();
    }
}
