package org.example.smartfridgeuni.model.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeIngredientDTO {

    private Long id;

    @NotBlank(message = "Ingredient name is required")
    @Size(max = 100, message = "Ingredient name cannot exceed 100 characters")
    private String ingredientName;

    @NotNull(message = "Required quantity is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Required quantity must be greater than 0")
    private Double requiredQuantity;

    @NotBlank(message = "Unit is required")
    @Size(max = 20, message = "Unit cannot exceed 20 characters")
    private String unit;
}
