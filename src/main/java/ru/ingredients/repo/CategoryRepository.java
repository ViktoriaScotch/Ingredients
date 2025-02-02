package ru.ingredients.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ingredients.models.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
