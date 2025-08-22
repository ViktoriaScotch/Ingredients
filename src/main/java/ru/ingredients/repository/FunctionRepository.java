package ru.ingredients.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ingredients.models.Function;

public interface FunctionRepository  extends JpaRepository<Function, Long> {
}
