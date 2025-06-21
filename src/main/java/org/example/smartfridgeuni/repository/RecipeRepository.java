package org.example.smartfridgeuni.repository;

import org.example.smartfridgeuni.model.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    // Find recipes by name (case-insensitive, partial match)
    @Query("SELECT r FROM Recipe r WHERE (:nameNull is true OR LOWER(r.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND (:prepTimeMin is null or r.prepTime >= :prepTimeMin) AND (:prepTimeMax is null or r.prepTime <= :prepTimeMax)")
    List<Recipe> findByNameContainingIgnoreCase(@Param("name") String name, @Param("nameNull") Boolean nameNull, @Param("prepTimeMin") Integer prepTimeMin, @Param("prepTimeMax") Integer prepTimeMax);

}
