package org.example.smartfridgeuni.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeRecommendationDTO {

    private RecipeSummaryDTO recipe;
    private Double matchPercentage;
    private List<String> availableIngredients;
    private List<String> missingIngredients;
    private Boolean canMake;
    private String recommendation;
}
