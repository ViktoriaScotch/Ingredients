package ru.ingredients.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ingredients.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
