package ru.ingredients.repo;

import org.springframework.data.repository.CrudRepository;
import ru.ingredients.models.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
