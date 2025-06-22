package org.example.smartfridgeuni.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.smartfridgeuni.model.dto.ApiResponseDTO;
import org.example.smartfridgeuni.model.dto.ExpirationSummaryDTO;
import org.example.smartfridgeuni.model.dto.RecommendationResponseDTO;
import org.example.smartfridgeuni.service.ExpirationTrackingService;
import org.example.smartfridgeuni.service.RecipeRecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/recommendations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Recommendations", description = "API for recipe recommendations and expiration tracking")
public class RecommendationController {

    private final RecipeRecommendationService recommendationService;
    private final ExpirationTrackingService expirationTrackingService;

    @GetMapping
    public ResponseEntity<ApiResponseDTO<RecommendationResponseDTO>> getRecipeRecommendations(
            @Parameter(description = "Minimum match percentage (0-100)")
            @RequestParam(required = false) Double minMatch,
            @Parameter(description = "Only show recipes that can be made completely")
            @RequestParam(required = false, defaultValue = "false") Boolean canMakeOnly) {

        log.info("Received request for recipe recommendations (minMatch: {}%, canMakeOnly: {})",
                minMatch, canMakeOnly);

        RecommendationResponseDTO recommendations = recommendationService
                .getRecipeRecommendations(minMatch, canMakeOnly);

        ApiResponseDTO<RecommendationResponseDTO> response = ApiResponseDTO.success(recommendations);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/expiration-summary")
    public ResponseEntity<ApiResponseDTO<ExpirationSummaryDTO>> getExpirationSummary() {

        log.info("Received request for expiration summary");

        ExpirationSummaryDTO summary = expirationTrackingService.getExpirationSummary();

        ApiResponseDTO<ExpirationSummaryDTO> response = ApiResponseDTO.success(summary);
        return ResponseEntity.ok(response);
    }
}
