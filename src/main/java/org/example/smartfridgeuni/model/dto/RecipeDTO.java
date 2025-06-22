package org.example.smartfridgeuni.model.dto;

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

    private String name;

    private String description;

    private String instructions;

    private Integer prepTime;

    private Integer servings;

    private LocalDateTime createdDate;

    private List<RecipeIngredientDTO> ingredients;
}
