package org.example.smartfridgeuni.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.smartfridgeuni.model.dto.AvailableIngredientDTO;
import org.example.smartfridgeuni.model.dto.FoodItemDTO;
import org.example.smartfridgeuni.model.dto.RecipeRecommendationDTO;
import org.example.smartfridgeuni.model.dto.RecipeSummaryDTO;
import org.example.smartfridgeuni.model.dto.RecommendationResponseDTO;
import org.example.smartfridgeuni.model.entity.Recipe;
import org.example.smartfridgeuni.model.entity.RecipeIngredient;
import org.example.smartfridgeuni.repository.RecipeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecipeRecommendationService {

    private final RecipeRepository recipeRepository;
    private final FoodItemService foodItemService;

    @Transactional(readOnly = true)
    public RecommendationResponseDTO getRecipeRecommendations(Double minMatchPercentage, Boolean canMakeOnly) {
        log.info("Generating recipe recommendations with minMatch: {}%, canMakeOnly: {}",
                minMatchPercentage, canMakeOnly);

        // Get available non-expired ingredients
        List<FoodItemDTO> availableFoodItems = foodItemService.getNonExpiredItems();
        List<AvailableIngredientDTO> availableIngredients = availableFoodItems.stream()
                .map(this::convertToAvailableIngredient)
                .collect(Collectors.toList());

        // Create a map of available ingredient names for quick lookup
        Set<String> availableIngredientNames = availableFoodItems.stream()
                .map(item -> item.getName().toLowerCase())
                .collect(Collectors.toSet());

        // Get all recipes
        List<Recipe> allRecipes = recipeRepository.findAll();

        // Calculate recommendations for each recipe
        List<RecipeRecommendationDTO> recommendations = allRecipes.stream()
                .map(recipe -> calculateRecipeMatch(recipe, availableIngredientNames))
                .filter(recommendation -> {
                    // Apply filters
                    boolean matchesMinPercentage = minMatchPercentage == null ||
                            recommendation.getMatchPercentage() >= minMatchPercentage;
                    boolean matchesCanMakeFilter = canMakeOnly == null || !canMakeOnly ||
                            recommendation.getCanMake();

                    return matchesMinPercentage && matchesCanMakeFilter;
                })
                .sorted((r1, r2) -> Double.compare(r2.getMatchPercentage(), r1.getMatchPercentage()))
                .collect(Collectors.toList());

        RecommendationResponseDTO response = new RecommendationResponseDTO();
        response.setAvailableIngredients(availableIngredients);
        response.setRecommendations(recommendations);
        response.setTotalRecipes(allRecipes.size());
        response.setMatchingRecipes(recommendations.size());

        log.info("Generated {} recommendations out of {} total recipes",
                recommendations.size(), allRecipes.size());

        return response;
    }

    private RecipeRecommendationDTO calculateRecipeMatch(Recipe recipe, Set<String> availableIngredientNames) {
        List<RecipeIngredient> recipeIngredients = recipe.getIngredients();

        if (recipeIngredients.isEmpty()) {
            return createEmptyRecommendation(recipe);
        }

        List<String> availableForRecipe = new ArrayList<>();
        List<String> missingIngredients = new ArrayList<>();

        for (RecipeIngredient ingredient : recipeIngredients) {
            String ingredientName = ingredient.getIngredientName().toLowerCase();

            if (availableIngredientNames.contains(ingredientName)) {
                availableForRecipe.add(ingredient.getIngredientName());
            } else {
                missingIngredients.add(ingredient.getIngredientName());
            }
        }

        double matchPercentage = (double) availableForRecipe.size() / recipeIngredients.size() * 100;
        boolean canMake = missingIngredients.isEmpty();

        String recommendationText = getRecommendationText(matchPercentage);

        RecipeSummaryDTO recipeSummary = convertToRecipeSummary(recipe);

        RecipeRecommendationDTO recommendation = new RecipeRecommendationDTO();
        recommendation.setRecipe(recipeSummary);
        recommendation.setMatchPercentage(Math.round(matchPercentage * 100.0) / 100.0);
        recommendation.setAvailableIngredients(availableForRecipe);
        recommendation.setMissingIngredients(missingIngredients);
        recommendation.setCanMake(canMake);
        recommendation.setRecommendation(recommendationText);

        return recommendation;
    }

    private RecipeRecommendationDTO createEmptyRecommendation(Recipe recipe) {
        RecipeRecommendationDTO recommendation = new RecipeRecommendationDTO();
        recommendation.setRecipe(convertToRecipeSummary(recipe));
        recommendation.setMatchPercentage(0.0);
        recommendation.setAvailableIngredients(Collections.emptyList());
        recommendation.setMissingIngredients(Collections.emptyList());
        recommendation.setCanMake(false);
        recommendation.setRecommendation("No ingredients specified");
        return recommendation;
    }

    private String getRecommendationText(double matchPercentage) {
        if (matchPercentage == 100.0) {
            return "Perfect match";
        } else if (matchPercentage >= 80.0) {
            return "Good match";
        } else if (matchPercentage >= 50.0) {
            return "Partial match";
        } else {
            return "Low match";
        }
    }

    private AvailableIngredientDTO convertToAvailableIngredient(FoodItemDTO foodItem) {
        AvailableIngredientDTO dto = new AvailableIngredientDTO();
        dto.setName(foodItem.getName());
        dto.setQuantity(foodItem.getQuantity());
        dto.setUnit(foodItem.getUnit());
        return dto;
    }

    private RecipeSummaryDTO convertToRecipeSummary(Recipe recipe) {
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
}
