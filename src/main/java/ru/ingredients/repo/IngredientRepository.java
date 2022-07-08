package ru.ingredients.repo;

import org.springframework.data.repository.CrudRepository;
import ru.ingredients.models.Ingredient;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {
}
