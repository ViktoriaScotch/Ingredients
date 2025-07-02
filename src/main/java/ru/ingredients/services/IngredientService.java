package ru.ingredients.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.ingredients.models.Category;
import ru.ingredients.models.Function;
import ru.ingredients.models.Ingredient;
import ru.ingredients.repo.CategoryRepository;
import ru.ingredients.repo.FunctionRepository;
import ru.ingredients.repo.IngredientRepository;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

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

    public List<Function> getFuncById(List<Long> funcIds) {
        return this.functionRepository.findAllById(funcIds);
    }

    public List<Category> getCatById(List<Long> catIds) {
        return this.categoryRepository.findAllById(catIds);
    }

    public List<Map<String, String>> getIngredientsForAutocomplete() {
        return ingredientRepository.getAllNamesId();
    }

    public Page<Ingredient> getIngredients(String search, int pageNumber, int pageSize, List<Long> functions, List<Long> categories) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        if ((search == null || search.isEmpty()) && functions == null && categories == null) {
            return ingredientRepository.findAll(pageable);
        } else {
            search = search == null ? "" : search.toLowerCase().replaceAll("[^a-zA-Zа-яА-Я0-9]+", "");
            return ingredientRepository.searchIngredients(search, functions, categories, pageable);
        }
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
