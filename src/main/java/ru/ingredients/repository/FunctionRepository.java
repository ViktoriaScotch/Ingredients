package ru.ingredients.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ingredients.entity.Function;

public interface FunctionRepository  extends JpaRepository<Function, Long> {
}
