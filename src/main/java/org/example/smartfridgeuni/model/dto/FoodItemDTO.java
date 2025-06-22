package org.example.smartfridgeuni.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodItemDTO {

    private Long id;

    private String name;

    private String category;

    private Double quantity;

    private String unit;

    private LocalDate expirationDate;

    private LocalDateTime addedDate;

    private Boolean isExpired;

    private Long daysUntilExpiration;
}
