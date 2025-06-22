package org.example.smartfridgeuni.repository;

import org.example.smartfridgeuni.model.entity.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface FoodItemRepository extends JpaRepository<FoodItem, Long> {

    @Query("SELECT f FROM FoodItem f WHERE f.expirationDate < CURRENT_DATE")
    List<FoodItem> findExpiredItems();

    @Query("SELECT f FROM FoodItem f WHERE f.expirationDate BETWEEN CURRENT_DATE AND :date")
    List<FoodItem> findItemsExpiringBefore(@Param("date") LocalDate date);

    List<FoodItem> findByCategoryIgnoreCase(String category);

    @Query("SELECT f FROM FoodItem f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<FoodItem> findByNameContainingIgnoreCase(@Param("name") String name);

    @Query("SELECT f FROM FoodItem f WHERE f.expirationDate >= CURRENT_DATE")
    List<FoodItem> findNonExpiredItems();

}

