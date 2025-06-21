package org.example.smartfridgeuni.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpirationSummaryDTO {

    private Integer expiredCount;
    private Integer expiringSoonCount;
    private Integer expiringThisWeekCount;
    private List<FoodItemDTO> expiredItems;
    private List<FoodItemDTO> expiringSoonItems;
}
