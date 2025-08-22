package ru.ingredients.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ingredients.models.Percent;

public interface PercentRepository extends JpaRepository<Percent, Long> {
}
