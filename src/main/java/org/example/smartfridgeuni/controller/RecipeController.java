package org.example.smartfridgeuni.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.smartfridgeuni.model.dto.ApiResponseDTO;
import org.example.smartfridgeuni.model.dto.RecipeDTO;
import org.example.smartfridgeuni.model.dto.RecipeRequest;
import org.example.smartfridgeuni.model.dto.RecipeSummaryDTO;
import org.example.smartfridgeuni.service.RecipeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Recipes", description = "API for managing recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<RecipeDTO>> addRecipe(
            @Valid @RequestBody RecipeRequest recipeDTO) {

        log.info("Received request to add recipe: {}", recipeDTO.getName());

        RecipeDTO savedRecipe = recipeService.addRecipe(recipeDTO);
        ApiResponseDTO<RecipeDTO> response = ApiResponseDTO.success(
                "Recipe added successfully", savedRecipe);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponseDTO<List<RecipeSummaryDTO>>> getAllRecipes(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Integer minPrepTime,
            @RequestParam(required = false) Integer maxPrepTime) {

        log.info("Received request to get all recipes (search: {}, minPrepTime: {}, maxPrepTime: {})",
                search, minPrepTime, maxPrepTime);

        List<RecipeSummaryDTO> recipes = recipeService.searchRecipes(search, minPrepTime, maxPrepTime);

        ApiResponseDTO<List<RecipeSummaryDTO>> response = ApiResponseDTO.success(recipes);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<RecipeDTO>> getRecipeById(@PathVariable Long id) {

        log.info("Received request to get recipe with ID: {}", id);

        RecipeDTO recipe = recipeService.getRecipeById(id);

        ApiResponseDTO<RecipeDTO> response = ApiResponseDTO.success(recipe);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<RecipeDTO>> updateRecipe(
            @PathVariable Long id,
            @Valid @RequestBody RecipeRequest recipeDTO) {

        log.info("Received request to update recipe with ID: {}", id);

        RecipeDTO updatedRecipe = recipeService.updateRecipe(id, recipeDTO);

        ApiResponseDTO<RecipeDTO> response = ApiResponseDTO.success(
                "Recipe updated successfully", updatedRecipe);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<String>> deleteRecipe(@PathVariable Long id) {

        log.info("Received request to delete recipe with ID: {}", id);

        recipeService.deleteRecipe(id);

        ApiResponseDTO<String> response = ApiResponseDTO.success(
                "Recipe deleted successfully", "Recipe with ID " + id + " has been removed");
        return ResponseEntity.ok(response);
    }
}
