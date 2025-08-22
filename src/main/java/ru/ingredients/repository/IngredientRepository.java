package ru.ingredients.repo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.ingredients.models.Ingredient;

import java.util.List;
import java.util.Map;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {

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
            value = "SELECT DISTINCT ingredient.* FROM (" +
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
                    "           '\\W+', '', 'g')) IN :name",
            nativeQuery = true
    )
    List<Ingredient> findByAllNames(@Param("name") List<String> name);

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
                    "LEFT JOIN ingredient_category ic ON ingredient.id = ic.ingredient_id " +
                    "LEFT JOIN ingredient_function ifn ON ingredient.id = ifn.ingredient_id " +
                    "WHERE LOWER(REGEXP_REPLACE(t.inci, '\\W+', '', 'g')) LIKE %:searchString% " +
                    "AND (:categories IS NULL OR ic.category_id IN (:categories)) " +
                    "AND (:functions IS NULL OR ifn.function_id IN (:functions))",
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
    List<Map<String, String>> getIngredientsForAutocomplete();
}
