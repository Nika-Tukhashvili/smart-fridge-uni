package org.example.smartfridgeuni.service;

import lombok.extern.slf4j.Slf4j;
import org.example.smartfridgeuni.model.dto.FoodItemDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class NotificationService {

    public void sendExpiredItemsNotification(List<FoodItemDTO> expiredItems) {
        log.info("Sending notification for {} expired items", expiredItems.size());

        // In a real application, you would implement email/SMS/push notification logic here
        // For now, we'll just log the notification

        StringBuilder message = new StringBuilder();
        message.append("⚠️ EXPIRED ITEMS ALERT ⚠️\n\n");
        message.append("The following items in your smart fridge have expired:\n\n");

        for (FoodItemDTO item : expiredItems) {
            message.append(String.format("• %s (%s %s) - Expired on %s\n",
                    item.getName(),
                    item.getQuantity(),
                    item.getUnit(),
                    item.getExpirationDate()));
        }

        message.append("\nPlease remove these items from your fridge.");

        log.warn("NOTIFICATION: {}", message);

        // TODO: Implement actual notification sending (email, SMS, push notification)
        // Examples:
        // - emailService.sendEmail(userEmail, "Expired Items Alert", message.toString());
        // - smsService.sendSMS(userPhone, message.toString());
        // - pushNotificationService.send(userId, "Expired Items", message.toString());
    }

    public void sendExpiringSoonNotification(List<FoodItemDTO> expiringSoonItems) {
        log.info("Sending notification for {} items expiring soon", expiringSoonItems.size());

        StringBuilder message = new StringBuilder();
        message.append("🔔 EXPIRATION WARNING 🔔\n\n");
        message.append("The following items will expire soon:\n\n");

        for (FoodItemDTO item : expiringSoonItems) {
            long daysUntilExpiration = item.getDaysUntilExpiration();
            String timeText = daysUntilExpiration == 1 ? "tomorrow" :
                    daysUntilExpiration == 0 ? "today" :
                            "in " + daysUntilExpiration + " days";

            message.append(String.format("• %s (%s %s) - Expires %s\n",
                    item.getName(),
                    item.getQuantity(),
                    item.getUnit(),
                    timeText));
        }

        message.append("\nConsider using these items soon!");

        log.info("NOTIFICATION: {}", message.toString());

        // TODO: Implement actual notification sending
    }
}
