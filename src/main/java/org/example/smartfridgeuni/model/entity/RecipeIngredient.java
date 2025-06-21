package org.example.smartfridgeuni.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recipe_ingredients")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @NotBlank(message = "Ingredient name is required")
    @Size(max = 100, message = "Ingredient name cannot exceed 100 characters")
    @Column(name = "ingredient_name", nullable = false, length = 100)
    private String ingredientName;

    @NotNull(message = "Required quantity is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Required quantity must be greater than 0")
    @Column(name = "required_quantity", nullable = false)
    private Double requiredQuantity;

    @NotBlank(message = "Unit is required")
    @Size(max = 20, message = "Unit cannot exceed 20 characters")
    @Column(nullable = false, length = 20)
    private String unit;
}
