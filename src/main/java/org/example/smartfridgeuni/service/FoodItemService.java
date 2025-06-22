package org.example.smartfridgeuni.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.smartfridgeuni.model.dto.FoodItemDTO;
import org.example.smartfridgeuni.model.dto.FoodItemRequest;
import org.example.smartfridgeuni.model.entity.FoodItem;
import org.example.smartfridgeuni.repository.FoodItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodItemService {

    private final FoodItemRepository foodItemRepository;

    @Transactional
    public FoodItemDTO addFoodItem(FoodItemRequest foodItemDTO) {
        log.info("Adding new food item: {}", foodItemDTO.getName());

        FoodItem foodItem = convertToEntity(foodItemDTO);
        FoodItem savedFoodItem = foodItemRepository.save(foodItem);

        log.info("Successfully added food item with ID: {}", savedFoodItem.getId());
        return convertToDTO(savedFoodItem);
    }

    @Transactional(readOnly = true)
    public List<FoodItemDTO> getAllFoodItems() {
        log.info("Retrieving all food items");

        List<FoodItem> foodItems = foodItemRepository.findAll();
        return foodItems.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<FoodItemDTO> getFoodItemById(Long id) {
        log.info("Retrieving food item with ID: {}", id);

        return foodItemRepository.findById(id)
                .map(this::convertToDTO);
    }

    @Transactional
    public boolean deleteFoodItem(Long id) {
        log.info("Attempting to delete food item with ID: {}", id);

        if (foodItemRepository.existsById(id)) {
            foodItemRepository.deleteById(id);
            log.info("Successfully deleted food item with ID: {}", id);
            return true;
        }

        log.warn("Food item with ID {} not found for deletion", id);
        return false;
    }

    @Transactional
    public Optional<FoodItemDTO> updateFoodItem(Long id, FoodItemRequest foodItemDTO) {
        log.info("Updating food item with ID: {}", id);

        return foodItemRepository.findById(id)
                .map(existingItem -> {
                    existingItem.setName(foodItemDTO.getName());
                    existingItem.setCategory(foodItemDTO.getCategory());
                    existingItem.setQuantity(foodItemDTO.getQuantity());
                    existingItem.setUnit(foodItemDTO.getUnit());
                    existingItem.setExpirationDate(foodItemDTO.getExpirationDate());

                    FoodItem updatedItem = foodItemRepository.save(existingItem);
                    log.info("Successfully updated food item with ID: {}", id);
                    return convertToDTO(updatedItem);
                });
    }

    @Transactional(readOnly = true)
    public List<FoodItemDTO> getExpiredItems() {
        log.info("Retrieving expired food items");

        List<FoodItem> expiredItems = foodItemRepository.findExpiredItems();
        return expiredItems.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FoodItemDTO> getItemsExpiringWithinDays(int days) {
        log.info("Retrieving items expiring within {} days", days);

        LocalDate targetDate = LocalDate.now().plusDays(days);
        List<FoodItem> expiringItems = foodItemRepository.findItemsExpiringBefore(targetDate);

        return expiringItems.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FoodItemDTO> getNonExpiredItems() {
        log.info("Retrieving non-expired food items");

        List<FoodItem> nonExpiredItems = foodItemRepository.findNonExpiredItems();
        return nonExpiredItems.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FoodItemDTO> getFoodItemsByCategory(String category) {
        log.info("Retrieving food items by category: {}", category);

        List<FoodItem> items = foodItemRepository.findByCategoryIgnoreCase(category);
        return items.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FoodItemDTO> searchFoodItems(String name) {
        log.info("Searching food items by name: {}", name);

        List<FoodItem> items = foodItemRepository.findByNameContainingIgnoreCase(name);
        return items.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long getExpiredItemsCount() {
        return foodItemRepository.countExpiredItems();
    }

    // Helper methods for conversion
    private FoodItemDTO convertToDTO(FoodItem foodItem) {
        FoodItemDTO dto = new FoodItemDTO();
        dto.setId(foodItem.getId());
        dto.setName(foodItem.getName());
        dto.setCategory(foodItem.getCategory());
        dto.setQuantity(foodItem.getQuantity());
        dto.setUnit(foodItem.getUnit());
        dto.setExpirationDate(foodItem.getExpirationDate());
        dto.setAddedDate(foodItem.getAddedDate());
        dto.setIsExpired(foodItem.isExpired());
        dto.setDaysUntilExpiration(foodItem.getDaysUntilExpiration());
        return dto;
    }

    private FoodItem convertToEntity(FoodItemRequest dto) {
        FoodItem foodItem = new FoodItem();
        foodItem.setName(dto.getName());
        foodItem.setCategory(dto.getCategory());
        foodItem.setQuantity(dto.getQuantity());
        foodItem.setUnit(dto.getUnit());
        foodItem.setExpirationDate(dto.getExpirationDate());
        return foodItem;
    }
}
