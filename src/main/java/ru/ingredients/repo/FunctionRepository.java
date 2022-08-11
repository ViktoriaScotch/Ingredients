package ru.ingredients.repo;

import org.springframework.data.repository.CrudRepository;
import ru.ingredients.models.Function;

public interface FunctionRepository  extends CrudRepository<Function, Long> {
}
