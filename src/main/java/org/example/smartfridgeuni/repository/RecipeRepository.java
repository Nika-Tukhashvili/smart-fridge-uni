package org.example.smartfridgeuni.repository;

import org.example.smartfridgeuni.model.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    // Find recipes by name (case-insensitive, partial match)
    @Query("SELECT r FROM Recipe r WHERE LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<Recipe> findByNameContainingIgnoreCase(@Param("name") String name);

    // Find recipes by preparation time range
    List<Recipe> findByPrepTimeBetween(Integer minTime, Integer maxTime);

    // Find recipes by servings
    List<Recipe> findByServings(Integer servings);

    // Find recipes that can be made with available ingredients
    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.ingredients ri " +
            "WHERE ri.ingredientName IN :availableIngredients")
    List<Recipe> findRecipesWithAvailableIngredients(@Param("availableIngredients") List<String> availableIngredients);

    // Find recipes ordered by creation date (newest first)
    List<Recipe> findAllByOrderByCreatedDateDesc();
}
