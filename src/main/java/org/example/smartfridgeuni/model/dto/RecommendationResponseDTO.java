package org.example.smartfridgeuni.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecommendationResponseDTO {

    private List<AvailableIngredientDTO> availableIngredients;
    private List<RecipeRecommendationDTO> recommendations;
    private Integer totalRecipes;
    private Integer matchingRecipes;
}
