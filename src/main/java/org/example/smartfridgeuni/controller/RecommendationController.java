package org.example.smartfridgeuni.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.smartfridgeuni.model.dto.ApiResponseDTO;
import org.example.smartfridgeuni.model.dto.ExpirationSummaryDTO;
import org.example.smartfridgeuni.model.dto.RecipeRecommendationDTO;
import org.example.smartfridgeuni.model.dto.RecommendationResponseDTO;
import org.example.smartfridgeuni.service.ExpirationTrackingService;
import org.example.smartfridgeuni.service.RecipeRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Recommendations", description = "API for recipe recommendations and expiration tracking")
public class RecommendationController {

    private final RecipeRecommendationService recommendationService;
    private final ExpirationTrackingService expirationTrackingService;

    @GetMapping
    @Operation(summary = "Get recipe recommendations",
            description = "Get recipe recommendations based on available ingredients")
    public ResponseEntity<ApiResponseDTO<RecommendationResponseDTO>> getRecipeRecommendations(
            @Parameter(description = "Minimum match percentage (0-100)")
            @RequestParam(required = false) Double minMatch,
            @Parameter(description = "Only show recipes that can be made completely")
            @RequestParam(required = false, defaultValue = "false") Boolean canMakeOnly) {

        log.info("Received request for recipe recommendations (minMatch: {}%, canMakeOnly: {})",
                minMatch, canMakeOnly);

        if (minMatch != null && (minMatch < 0 || minMatch > 100)) {
            throw new IllegalArgumentException("Minimum match percentage must be between 0 and 100");
        }

        RecommendationResponseDTO recommendations = recommendationService
                .getRecipeRecommendations(minMatch, canMakeOnly);

        ApiResponseDTO<RecommendationResponseDTO> response = ApiResponseDTO.success(recommendations);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/for-ingredient/{ingredientName}")
    @Operation(summary = "Get recipes for specific ingredient",
            description = "Get recipe recommendations that use a specific ingredient")
    public ResponseEntity<ApiResponseDTO<List<RecipeRecommendationDTO>>> getRecipesForIngredient(
            @Parameter(description = "Name of the ingredient") @PathVariable String ingredientName) {

        log.info("Received request for recipes using ingredient: {}", ingredientName);

        List<RecipeRecommendationDTO> recommendations = recommendationService
                .getRecipesForIngredient(ingredientName);

        ApiResponseDTO<List<RecipeRecommendationDTO>> response = ApiResponseDTO.success(recommendations);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/expiration-summary")
    @Operation(summary = "Get expiration summary",
            description = "Get summary of expired and expiring items")
    public ResponseEntity<ApiResponseDTO<ExpirationSummaryDTO>> getExpirationSummary() {

        log.info("Received request for expiration summary");

        ExpirationSummaryDTO summary = expirationTrackingService.getExpirationSummary();

        ApiResponseDTO<ExpirationSummaryDTO> response = ApiResponseDTO.success(summary);
        return ResponseEntity.ok(response);
    }
}
