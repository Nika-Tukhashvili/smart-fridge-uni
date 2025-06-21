package org.example.smartfridgeuni.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.smartfridgeuni.model.dto.ApiResponseDTO;
import org.example.smartfridgeuni.model.dto.RecipeDTO;
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
import java.util.Optional;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Recipes", description = "API for managing recipes")
public class RecipeController {

    private final RecipeService recipeService;

    @PostMapping
    @Operation(summary = "Add a new recipe", description = "Create a new recipe with ingredients")
    public ResponseEntity<ApiResponseDTO<RecipeDTO>> addRecipe(
            @Valid @RequestBody RecipeDTO recipeDTO) {

        log.info("Received request to add recipe: {}", recipeDTO.getName());

        RecipeDTO savedRecipe = recipeService.addRecipe(recipeDTO);
        ApiResponseDTO<RecipeDTO> response = ApiResponseDTO.success(
                "Recipe added successfully", savedRecipe);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all recipes", description = "Retrieve all recipes (summary view)")
    public ResponseEntity<ApiResponseDTO<List<RecipeSummaryDTO>>> getAllRecipes(
            @Parameter(description = "Search by recipe name") @RequestParam(required = false) String search,
            @Parameter(description = "Minimum preparation time") @RequestParam(required = false) Integer minPrepTime,
            @Parameter(description = "Maximum preparation time") @RequestParam(required = false) Integer maxPrepTime) {

        log.info("Received request to get all recipes (search: {}, minPrepTime: {}, maxPrepTime: {})",
                search, minPrepTime, maxPrepTime);

        List<RecipeSummaryDTO> recipes = recipeService.searchRecipes(search, minPrepTime, maxPrepTime);

        ApiResponseDTO<List<RecipeSummaryDTO>> response = ApiResponseDTO.success(recipes);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get recipe by ID", description = "Retrieve a specific recipe with full details including ingredients")
    public ResponseEntity<ApiResponseDTO<RecipeDTO>> getRecipeById(
            @Parameter(description = "Recipe ID") @PathVariable Long id) {

        log.info("Received request to get recipe with ID: {}", id);

        Optional<RecipeDTO> recipe = recipeService.getRecipeById(id);

        if (recipe.isPresent()) {
            ApiResponseDTO<RecipeDTO> response = ApiResponseDTO.success(recipe.get());
            return ResponseEntity.ok(response);
        } else {
            throw new EntityNotFoundException("Recipe with ID " + id + " not found");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update recipe", description = "Update an existing recipe")
    public ResponseEntity<ApiResponseDTO<RecipeDTO>> updateRecipe(
            @Parameter(description = "Recipe ID") @PathVariable Long id,
            @Valid @RequestBody RecipeDTO recipeDTO) {

        log.info("Received request to update recipe with ID: {}", id);

        Optional<RecipeDTO> updatedRecipe = recipeService.updateRecipe(id, recipeDTO);

        if (updatedRecipe.isPresent()) {
            ApiResponseDTO<RecipeDTO> response = ApiResponseDTO.success(
                    "Recipe updated successfully", updatedRecipe.get());
            return ResponseEntity.ok(response);
        } else {
            throw new EntityNotFoundException("Recipe with ID " + id + " not found");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete recipe", description = "Remove a recipe")
    public ResponseEntity<ApiResponseDTO<String>> deleteRecipe(
            @Parameter(description = "Recipe ID") @PathVariable Long id) {

        log.info("Received request to delete recipe with ID: {}", id);

        boolean deleted = recipeService.deleteRecipe(id);

        if (deleted) {
            ApiResponseDTO<String> response = ApiResponseDTO.success(
                    "Recipe deleted successfully", "Recipe with ID " + id + " has been removed");
            return ResponseEntity.ok(response);
        } else {
            throw new EntityNotFoundException("Recipe with ID " + id + " not found");
        }
    }
}
