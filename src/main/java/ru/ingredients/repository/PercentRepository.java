package ru.ingredients.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.ingredients.entity.Percent;

public interface PercentRepository extends JpaRepository<Percent, Long> {
}
