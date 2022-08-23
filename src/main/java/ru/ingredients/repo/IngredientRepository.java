package ru.ingredients.repo;

import org.springframework.data.repository.CrudRepository;
import ru.ingredients.models.Ingredient;

import java.util.List;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
    List<Ingredient> findByInci(String inci);
}
