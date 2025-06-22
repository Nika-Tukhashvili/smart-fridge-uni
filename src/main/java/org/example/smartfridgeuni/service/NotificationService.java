package org.example.smartfridgeuni.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.smartfridgeuni.model.dto.FoodItemDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    @Value("${notification.email.to}")
    private String toEmail;

    private final EmailService emailService;

    @Async
    public void sendExpiredItemsNotification(List<FoodItemDTO> expiredItems) {
        log.info("Sending notification for {} expired items", expiredItems.size());

        StringBuilder message = new StringBuilder();
        String subject = "⚠️ EXPIRED ITEMS ALERT ⚠️";
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

        emailService.sendEmail(toEmail, subject, message.toString());
    }

    public void sendExpiringSoonNotification(List<FoodItemDTO> expiringSoonItems) {
        log.info("Sending notification for {} items expiring soon", expiringSoonItems.size());

        StringBuilder message = new StringBuilder();
        String subject = "🔔 EXPIRATION WARNING 🔔";
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

        log.info("NOTIFICATION: {}", message);

        emailService.sendEmail(toEmail, subject, message.toString());
    }
}
