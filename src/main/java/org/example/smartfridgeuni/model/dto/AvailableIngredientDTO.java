package org.example.smartfridgeuni.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AvailableIngredientDTO {

    private String name;
    private Double quantity;
    private String unit;
}
