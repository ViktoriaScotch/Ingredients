package ru.ingredients.repo;

import org.springframework.data.repository.CrudRepository;
import ru.ingredients.models.Percent;

public interface PercentRepository extends CrudRepository<Percent, Long> {
}
