package org.example.smartfridgeuni.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.smartfridgeuni.util.DateUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "food_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FoodItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name ="category" , nullable = false, length = 50)
    private String category;

    @Column(name = "quantity", nullable = false)
    private Double quantity;

    @Column(name = "unit", nullable = false, length = 20)
    private String unit;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "added_date", nullable = false, updatable = false)
    private LocalDateTime addedDate = LocalDateTime.now(DateUtils.ASIA_TBILISI);

    @Transient
    public boolean isExpired() {
        return expirationDate.isBefore(LocalDate.now(DateUtils.ASIA_TBILISI));
    }

    @Transient
    public long getDaysUntilExpiration() {
        return LocalDate.now(DateUtils.ASIA_TBILISI).until(expirationDate).getDays();
    }
}

