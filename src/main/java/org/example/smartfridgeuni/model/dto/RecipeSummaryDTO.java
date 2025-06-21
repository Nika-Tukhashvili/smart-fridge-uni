package org.example.smartfridgeuni.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecipeSummaryDTO {

    private Long id;
    private String name;
    private String description;
    private Integer prepTime;
    private Integer servings;
    private LocalDateTime createdDate;
    private Integer ingredientCount;
}
