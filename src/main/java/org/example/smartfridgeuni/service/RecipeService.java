package org.example.smartfridgeuni.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.smartfridgeuni.model.dto.RecipeDTO;
import org.example.smartfridgeuni.model.dto.RecipeIngredientDTO;
import org.example.smartfridgeuni.model.dto.RecipeSummaryDTO;
import org.example.smartfridgeuni.model.entity.Recipe;
import org.example.smartfridgeuni.model.entity.RecipeIngredient;
import org.example.smartfridgeuni.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeService {

    private final RecipeRepository recipeRepository;

    @Transactional
    public RecipeDTO addRecipe(RecipeDTO recipeDTO) {
        log.info("Adding new recipe: {}", recipeDTO.getName());

        Recipe recipe = convertToEntity(recipeDTO);
        Recipe savedRecipe = recipeRepository.save(recipe);

        log.info("Successfully added recipe with ID: {}", savedRecipe.getId());
        return convertToDTO(savedRecipe);
    }

    @Transactional(readOnly = true)
    public Optional<RecipeDTO> getRecipeById(Long id) {
        log.info("Retrieving recipe with ID: {}", id);

        return recipeRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public boolean deleteRecipe(Long id) {
        log.info("Attempting to delete recipe with ID: {}", id);

        if (recipeRepository.existsById(id)) {
            recipeRepository.deleteById(id);
            log.info("Successfully deleted recipe with ID: {}", id);
            return true;
        }

        log.warn("Recipe with ID {} not found for deletion", id);
        return false;
    }

    @Transactional
    public Optional<RecipeDTO> updateRecipe(Long id, RecipeDTO recipeDTO) {
        log.info("Updating recipe with ID: {}", id);

        return recipeRepository.findById(id)
                .map(existingRecipe -> {
                    // Update basic fields
                    existingRecipe.setName(recipeDTO.getName());
                    existingRecipe.setDescription(recipeDTO.getDescription());
                    existingRecipe.setInstructions(recipeDTO.getInstructions());
                    existingRecipe.setPrepTime(recipeDTO.getPrepTime());
                    existingRecipe.setServings(recipeDTO.getServings());

                    // Clear existing ingredients and add new ones
                    existingRecipe.getIngredients().clear();

                    for (RecipeIngredientDTO ingredientDTO : recipeDTO.getIngredients()) {
                        RecipeIngredient ingredient = new RecipeIngredient();
                        ingredient.setIngredientName(ingredientDTO.getIngredientName());
                        ingredient.setRequiredQuantity(ingredientDTO.getRequiredQuantity());
                        ingredient.setUnit(ingredientDTO.getUnit());
                        existingRecipe.addIngredient(ingredient);
                    }

                    Recipe updatedRecipe = recipeRepository.save(existingRecipe);
                    log.info("Successfully updated recipe with ID: {}", id);
                    return convertToDTO(updatedRecipe);
                });
    }

    @Transactional(readOnly = true)
    public List<RecipeSummaryDTO> searchRecipes(String name, Integer prepTimeMin, Integer prepTimeMax) {
        log.info("Searching recipes by name: {}", name);

        List<Recipe> recipes = recipeRepository.findByNameContainingIgnoreCase(name == null || name.isEmpty() ? "null" : name,name == null || name.isEmpty(), prepTimeMin, prepTimeMax);
        return recipes.stream()
                .map(this::convertToSummaryDTO)
                .collect(Collectors.toList());
    }

    // Helper methods for conversion
    private RecipeDTO convertToDTO(Recipe recipe) {
        RecipeDTO dto = new RecipeDTO();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setDescription(recipe.getDescription());
        dto.setInstructions(recipe.getInstructions());
        dto.setPrepTime(recipe.getPrepTime());
        dto.setServings(recipe.getServings());
        dto.setCreatedDate(recipe.getCreatedDate());

        List<RecipeIngredientDTO> ingredientDTOs = recipe.getIngredients().stream()
                .map(this::convertIngredientToDTO)
                .collect(Collectors.toList());
        dto.setIngredients(ingredientDTOs);

        return dto;
    }

    private RecipeSummaryDTO convertToSummaryDTO(Recipe recipe) {
        RecipeSummaryDTO dto = new RecipeSummaryDTO();
        dto.setId(recipe.getId());
        dto.setName(recipe.getName());
        dto.setDescription(recipe.getDescription());
        dto.setPrepTime(recipe.getPrepTime());
        dto.setServings(recipe.getServings());
        dto.setCreatedDate(recipe.getCreatedDate());
        dto.setIngredientCount(recipe.getIngredients().size());
        return dto;
    }

    private RecipeIngredientDTO convertIngredientToDTO(RecipeIngredient ingredient) {
        RecipeIngredientDTO dto = new RecipeIngredientDTO();
        dto.setId(ingredient.getId());
        dto.setIngredientName(ingredient.getIngredientName());
        dto.setRequiredQuantity(ingredient.getRequiredQuantity());
        dto.setUnit(ingredient.getUnit());
        return dto;
    }

    private Recipe convertToEntity(RecipeDTO dto) {
        Recipe recipe = new Recipe();
        recipe.setName(dto.getName());
        recipe.setDescription(dto.getDescription());
        recipe.setInstructions(dto.getInstructions());
        recipe.setPrepTime(dto.getPrepTime());
        recipe.setServings(dto.getServings());

        // Add ingredients
        for (RecipeIngredientDTO ingredientDTO : dto.getIngredients()) {
            RecipeIngredient ingredient = new RecipeIngredient();
            ingredient.setIngredientName(ingredientDTO.getIngredientName());
            ingredient.setRequiredQuantity(ingredientDTO.getRequiredQuantity());
            ingredient.setUnit(ingredientDTO.getUnit());
            recipe.addIngredient(ingredient);
        }

        return recipe;
    }
}
