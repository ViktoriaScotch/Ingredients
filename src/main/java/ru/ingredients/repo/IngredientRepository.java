package ru.ingredients.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ingredients.models.Ingredient;

import java.util.List;
import java.util.Map;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

    @Modifying
    @Query(
            value = "SELECT inci FROM ingredient UNION SELECT trade_name FROM ingredient UNION SELECT other_names FROM ingredient_other_names",
            nativeQuery = true
    )
    List<String> getAllNames();

    @Query(
            value = "SELECT ingredient.* FROM (" +
                    "SELECT inci, id FROM ingredient " +
                    "UNION " +
                    "SELECT trade_name, id FROM ingredient " +
                    "UNION " +
                    "SELECT other_names, ingredient_id FROM ingredient_other_names" +
                    ") AS t " +
                    "JOIN ingredient ON t.id=ingredient.id " +
                    "WHERE LOWER(REGEXP_REPLACE(" +
                    "               REGEXP_REPLACE(" +
                    "                           t.inci, " +
                    "               '\\(.+?\\)','','g'), " +
                    "           '\\W+', '', 'g')) LIKE :name",
            nativeQuery = true
    )
    Ingredient findByAllNames(@Param("name") String name);

    @Query(
            value = "SELECT DISTINCT ingredient.* " +
                    "FROM ingredient " +
                    "JOIN (" +
                        "SELECT inci, id FROM ingredient " +
                        "UNION " +
                        "SELECT trade_name, id FROM ingredient " +
                        "UNION " +
                        "SELECT other_names, ingredient_id FROM ingredient_other_names" +
                    ") AS t ON t.id=ingredient.id " +
                    "LEFT JOIN ingredient_categories ic ON ingredient.id = ic.ingredients_id " +
                    "LEFT JOIN ingredient_functions ifn ON ingredient.id = ifn.ingredients_id " +
                    "WHERE LOWER(REGEXP_REPLACE(t.inci, '\\W+', '', 'g')) LIKE %:searchString% " +
                    "AND (:categories IS NULL OR ic.categories_id IN (:categories)) " +
                    "AND (:functions IS NULL OR ifn.functions_id IN (:functions))",
            nativeQuery = true
    )
    Page<Ingredient> searchIngredients(@Param("searchString") String searchString,
                                       @Param("functions") List<Long> functions,
                                       @Param("categories") List<Long> categories,
                                       Pageable pageable);

    @Query(
            value = "SELECT t.name as label, t.id as value FROM (" +
                    "SELECT inci as name, id FROM ingredient " +
                    "UNION " +
                    "SELECT trade_name as name, id FROM ingredient " +
                    "UNION " +
                    "SELECT other_names as name, ingredient_id as id FROM ingredient_other_names" +
                    ") AS t " +
                    "JOIN ingredient ON t.id=ingredient.id ",
            nativeQuery = true
    )
    List<Map<String, String>> getAllNamesId();
}
