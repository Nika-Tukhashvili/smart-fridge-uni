package org.example.smartfridgeuni.model.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name cannot exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Category is required")
    @Size(max = 50, message = "Category cannot exceed 50 characters")
    @Column(nullable = false, length = 50)
    private String category;

    @NotNull(message = "Quantity is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Quantity must be greater than 0")
    @Column(nullable = false)
    private Double quantity;

    @NotBlank(message = "Unit is required")
    @Size(max = 20, message = "Unit cannot exceed 20 characters")
    @Column(nullable = false, length = 20)
    private String unit;

    @NotNull(message = "Expiration date is required")
    @Future(message = "Expiration date must be in the future")
    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    @Column(name = "added_date", nullable = false, updatable = false)
    private LocalDateTime addedDate = LocalDateTime.now(DateUtils.ASIA_TBILISI);

    // Computed property - not stored in database
    @Transient
    public boolean isExpired() {
        return expirationDate.isBefore(LocalDate.now(DateUtils.ASIA_TBILISI));
    }

    @Transient
    public long getDaysUntilExpiration() {
        return LocalDate.now(DateUtils.ASIA_TBILISI).until(expirationDate).getDays();
    }
}

