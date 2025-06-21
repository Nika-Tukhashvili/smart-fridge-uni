package org.example.smartfridgeuni.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.smartfridgeuni.model.dto.ApiResponseDTO;
import org.example.smartfridgeuni.model.dto.FoodItemDTO;
import org.example.smartfridgeuni.service.FoodItemService;
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
@RequestMapping("/food-items")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Food Items", description = "API for managing food items in the smart fridge")
public class FoodItemController {

    private final FoodItemService foodItemService;

    @PostMapping
    @Operation(summary = "Add a new food item", description = "Add a new food item to the smart fridge")
    public ResponseEntity<ApiResponseDTO<FoodItemDTO>> addFoodItem(
            @Valid @RequestBody FoodItemDTO foodItemDTO) {

        log.info("Received request to add food item: {}", foodItemDTO.getName());

        FoodItemDTO savedItem = foodItemService.addFoodItem(foodItemDTO);
        ApiResponseDTO<FoodItemDTO> response = ApiResponseDTO.success(
                "Food item added successfully", savedItem);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all food items", description = "Retrieve all food items from the smart fridge")
    public ResponseEntity<ApiResponseDTO<List<FoodItemDTO>>> getAllFoodItems(
            @Parameter(description = "Filter by category") @RequestParam(required = false) String category,
            @Parameter(description = "Search by name") @RequestParam(required = false) String search) {

        log.info("Received request to get all food items (category: {}, search: {})", category, search);

        List<FoodItemDTO> foodItems;

        if (category != null && !category.trim().isEmpty()) {
            foodItems = foodItemService.getFoodItemsByCategory(category);
        } else if (search != null && !search.trim().isEmpty()) {
            foodItems = foodItemService.searchFoodItems(search);
        } else {
            foodItems = foodItemService.getAllFoodItems();
        }

        ApiResponseDTO<List<FoodItemDTO>> response = ApiResponseDTO.success(foodItems);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get food item by ID", description = "Retrieve a specific food item by its ID")
    public ResponseEntity<ApiResponseDTO<FoodItemDTO>> getFoodItemById(
            @Parameter(description = "Food item ID") @PathVariable Long id) {

        log.info("Received request to get food item with ID: {}", id);

        Optional<FoodItemDTO> foodItem = foodItemService.getFoodItemById(id);

        if (foodItem.isPresent()) {
            ApiResponseDTO<FoodItemDTO> response = ApiResponseDTO.success(foodItem.get());
            return ResponseEntity.ok(response);
        } else {
            throw new EntityNotFoundException("Food item with ID " + id + " not found");
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update food item", description = "Update an existing food item")
    public ResponseEntity<ApiResponseDTO<FoodItemDTO>> updateFoodItem(
            @Parameter(description = "Food item ID") @PathVariable Long id,
            @Valid @RequestBody FoodItemDTO foodItemDTO) {

        log.info("Received request to update food item with ID: {}", id);

        Optional<FoodItemDTO> updatedItem = foodItemService.updateFoodItem(id, foodItemDTO);

        if (updatedItem.isPresent()) {
            ApiResponseDTO<FoodItemDTO> response = ApiResponseDTO.success(
                    "Food item updated successfully", updatedItem.get());
            return ResponseEntity.ok(response);
        } else {
            throw new EntityNotFoundException("Food item with ID " + id + " not found");
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete food item", description = "Remove a food item from the smart fridge")
    public ResponseEntity<ApiResponseDTO<String>> deleteFoodItem(
            @Parameter(description = "Food item ID") @PathVariable Long id) {

        log.info("Received request to delete food item with ID: {}", id);

        boolean deleted = foodItemService.deleteFoodItem(id);

        if (deleted) {
            ApiResponseDTO<String> response = ApiResponseDTO.success(
                    "Food item deleted successfully", "Item with ID " + id + " has been removed");
            return ResponseEntity.ok(response);
        } else {
            throw new EntityNotFoundException("Food item with ID " + id + " not found");
        }
    }

    @GetMapping("/expired")
    @Operation(summary = "Get expired items", description = "Retrieve all expired food items")
    public ResponseEntity<ApiResponseDTO<List<FoodItemDTO>>> getExpiredItems() {

        log.info("Received request to get expired items");

        List<FoodItemDTO> expiredItems = foodItemService.getExpiredItems();
        ApiResponseDTO<List<FoodItemDTO>> response = ApiResponseDTO.success(expiredItems);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/expiring-soon")
    @Operation(summary = "Get items expiring soon", description = "Retrieve items expiring within specified days")
    public ResponseEntity<ApiResponseDTO<List<FoodItemDTO>>> getItemsExpiringSoon(
            @Parameter(description = "Number of days to check ahead")
            @RequestParam(defaultValue = "3") int days) {

        log.info("Received request to get items expiring within {} days", days);

        if (days < 1 || days > 30) {
            throw new IllegalArgumentException("Days must be between 1 and 30");
        }

        List<FoodItemDTO> expiringSoonItems = foodItemService.getItemsExpiringWithinDays(days);
        ApiResponseDTO<List<FoodItemDTO>> response = ApiResponseDTO.success(expiringSoonItems);

        return ResponseEntity.ok(response);
    }
}
