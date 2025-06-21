package org.example.smartfridgeuni.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeDTO {

    private Long id;

    @NotBlank(message = "Recipe name is required")
    @Size(max = 150, message = "Recipe name cannot exceed 150 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotBlank(message = "Instructions are required")
    private String instructions;

    @NotNull(message = "Preparation time is required")
    @Min(value = 1, message = "Preparation time must be at least 1 minute")
    private Integer prepTime;

    @NotNull(message = "Number of servings is required")
    @Min(value = 1, message = "Servings must be at least 1")
    private Integer servings;

    private LocalDateTime createdDate;

    @Valid
    @NotEmpty(message = "Recipe must have at least one ingredient")
    private List<RecipeIngredientDTO> ingredients;
}
