package org.example.smartfridgeuni.repository;

import org.example.smartfridgeuni.model.entity.RecipeIngredient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeIngredientRepository extends JpaRepository<RecipeIngredient, Long> {

    // Find ingredients by recipe ID
    List<RecipeIngredient> findByRecipeId(Long recipeId);

    // Find recipes containing specific ingredient
    @Query("SELECT ri FROM RecipeIngredient ri WHERE LOWER(ri.ingredientName) = LOWER(:ingredientName)")
    List<RecipeIngredient> findByIngredientNameIgnoreCase(@Param("ingredientName") String ingredientName);

    // Get all unique ingredient names
    @Query("SELECT DISTINCT ri.ingredientName FROM RecipeIngredient ri ORDER BY ri.ingredientName")
    List<String> findAllUniqueIngredientNames();

    // Count recipes that use specific ingredient
    @Query("SELECT COUNT(DISTINCT ri.recipe.id) FROM RecipeIngredient ri WHERE LOWER(ri.ingredientName) = LOWER(:ingredientName)")
    long countRecipesUsingIngredient(@Param("ingredientName") String ingredientName);
}
