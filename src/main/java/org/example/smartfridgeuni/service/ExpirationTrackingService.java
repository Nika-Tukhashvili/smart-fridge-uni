package org.example.smartfridgeuni.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.smartfridgeuni.model.dto.ExpirationSummaryDTO;
import org.example.smartfridgeuni.model.dto.FoodItemDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExpirationTrackingService {

    private final FoodItemService foodItemService;
    private final NotificationService notificationService;

    /**
     * Scheduled task to check for expired items every day at 8:00 AM
     */
    @Scheduled(cron = "0 0 8 * * *")
    public void checkExpiredItems() {
        log.info("Running scheduled task to check for expired items");

        try {
            List<FoodItemDTO> expiredItems = foodItemService.getExpiredItems();

            if (!expiredItems.isEmpty()) {
                log.warn("Found {} expired items", expiredItems.size());
                notificationService.sendExpiredItemsNotification(expiredItems);
            } else {
                log.info("No expired items found");
            }

            // Also check items expiring soon (within 2 days)
            List<FoodItemDTO> expiringSoonItems = foodItemService.getItemsExpiringWithinDays(2);

            if (!expiringSoonItems.isEmpty()) {
                log.info("Found {} items expiring soon", expiringSoonItems.size());
                notificationService.sendExpiringSoonNotification(expiringSoonItems);
            }

        } catch (Exception e) {
            log.error("Error occurred while checking expired items", e);
        }
    }

    /**
     * Get expiration summary
     */
    public ExpirationSummaryDTO getExpirationSummary() {
        log.info("Generating expiration summary");

        List<FoodItemDTO> expiredItems = foodItemService.getExpiredItems();
        List<FoodItemDTO> expiringSoonItems = foodItemService.getItemsExpiringWithinDays(3);
        List<FoodItemDTO> expiringThisWeekItems = foodItemService.getItemsExpiringWithinDays(7);

        ExpirationSummaryDTO summary = new ExpirationSummaryDTO();
        summary.setExpiredCount(expiredItems.size());
        summary.setExpiringSoonCount(expiringSoonItems.size());
        summary.setExpiringThisWeekCount(expiringThisWeekItems.size());
        summary.setExpiredItems(expiredItems);
        summary.setExpiringSoonItems(expiringSoonItems);

        return summary;
    }
}
