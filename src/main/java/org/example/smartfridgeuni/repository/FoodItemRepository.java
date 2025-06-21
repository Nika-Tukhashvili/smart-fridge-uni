package org.example.smartfridgeuni.repository;

import org.example.smartfridgeuni.model.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    // Find all expired items
    @Query("SELECT f FROM FoodItem f WHERE f.expirationDate < CURRENT_DATE")
    List<FoodItem> findExpiredItems();

    // Find items expiring within specified days
    @Query("SELECT f FROM FoodItem f WHERE f.expirationDate BETWEEN CURRENT_DATE AND :date")
    List<FoodItem> findItemsExpiringBefore(@Param("date") LocalDate date);

    // Find items by category
    List<FoodItem> findByCategoryIgnoreCase(String category);

    // Find items by name (case-insensitive, partial match)
    @Query("SELECT f FROM FoodItem f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<FoodItem> findByNameContainingIgnoreCase(@Param("name") String name);

    // Find items that are not expired
    @Query("SELECT f FROM FoodItem f WHERE f.expirationDate >= CURRENT_DATE")
    List<FoodItem> findNonExpiredItems();

    // Find items by ingredient name (for recipe matching)
    @Query("SELECT f FROM FoodItem f WHERE LOWER(f.name) = LOWER(:ingredientName) AND f.expirationDate >= CURRENT_DATE")
    Optional<FoodItem> findByIngredientName(@Param("ingredientName") String ingredientName);

    // Count expired items
    @Query("SELECT COUNT(f) FROM FoodItem f WHERE f.expirationDate < CURRENT_DATE")
    long countExpiredItems();
}

