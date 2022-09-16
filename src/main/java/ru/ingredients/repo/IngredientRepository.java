package ru.ingredients.repo;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.ingredients.models.Ingredient;

import java.util.List;

public interface IngredientRepository extends CrudRepository<Ingredient, Long> {

    Ingredient findByInci(String inci);

    Ingredient findByTradeName(String tradeName);

    Ingredient findByOtherNames(String otherName);

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
                    "WHERE t.inci LIKE %:searchString%",
            nativeQuery = true
    )
    List<Ingredient> searchIngredients(@Param("searchString") String searchString);
}
