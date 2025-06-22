package org.example.smartfridgeuni.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.smartfridgeuni.model.dto.ApiResponseDTO;
import org.example.smartfridgeuni.model.dto.FoodItemDTO;
import org.example.smartfridgeuni.model.dto.FoodItemRequest;
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

@RestController
@RequestMapping("/food-items")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Food Items", description = "API for managing food items in the smart fridge")
public class FoodItemController {

    private final FoodItemService foodItemService;

    @PostMapping
    public ResponseEntity<ApiResponseDTO<FoodItemDTO>> addFoodItem(
            @Valid @RequestBody FoodItemRequest foodItemDTO) {

        log.info("Received request to add food item: {}", foodItemDTO.getName());

        FoodItemDTO savedItem = foodItemService.addFoodItem(foodItemDTO);
        ApiResponseDTO<FoodItemDTO> response = ApiResponseDTO.success(
                "Food item added successfully", savedItem);

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
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
    public ResponseEntity<ApiResponseDTO<FoodItemDTO>> getFoodItemById(@PathVariable Long id) {

        log.info("Received request to get food item with ID: {}", id);

        FoodItemDTO foodItem = foodItemService.getFoodItemById(id);

        ApiResponseDTO<FoodItemDTO> response = ApiResponseDTO.success(foodItem);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<FoodItemDTO>> updateFoodItem(
            @PathVariable Long id,
            @Valid @RequestBody FoodItemRequest foodItemDTO) {

        log.info("Received request to update food item with ID: {}", id);

        FoodItemDTO updatedItem = foodItemService.updateFoodItem(id, foodItemDTO);

        ApiResponseDTO<FoodItemDTO> response = ApiResponseDTO.success(
                "Food item updated successfully", updatedItem);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDTO<String>> deleteFoodItem(@PathVariable Long id) {

        log.info("Received request to delete food item with ID: {}", id);

        foodItemService.deleteFoodItem(id);

        ApiResponseDTO<String> response = ApiResponseDTO.success(
                "Food item deleted successfully", "Item with ID " + id + " has been removed");
        return ResponseEntity.ok(response);

    }

    @GetMapping("/expired")
    public ResponseEntity<ApiResponseDTO<List<FoodItemDTO>>> getExpiredItems() {

        log.info("Received request to get expired items");

        List<FoodItemDTO> expiredItems = foodItemService.getExpiredItems();
        ApiResponseDTO<List<FoodItemDTO>> response = ApiResponseDTO.success(expiredItems);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/expiring-soon")
    public ResponseEntity<ApiResponseDTO<List<FoodItemDTO>>> getItemsExpiringSoon(@RequestParam(defaultValue = "3") int days) {

        log.info("Received request to get items expiring within {} days", days);

        List<FoodItemDTO> expiringSoonItems = foodItemService.getItemsExpiringWithinDays(days);
        ApiResponseDTO<List<FoodItemDTO>> response = ApiResponseDTO.success(expiringSoonItems);

        return ResponseEntity.ok(response);
    }
}
