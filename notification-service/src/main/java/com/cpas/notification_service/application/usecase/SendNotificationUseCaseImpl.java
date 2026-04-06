package com.cpas.notification_service.application.usecase;

import org.springframework.stereotype.Service;
import com.cpas.notification_service.application.port.in.SendNotificationUseCase;
import com.cpas.notification_service.application.port.out.NotificationProvider;
import com.cpas.notification_service.domain.model.dto.PriceAlert;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendNotificationUseCaseImpl implements SendNotificationUseCase {

    private final NotificationProvider notificationProvider;

    @Override
    public void processAlert(PriceAlert alert) {
        log.info("[SendNotificationUseCase] Received alert for processing: {}", alert);
        
        if (alert.timestamp() != null) {
            long ageMillis = System.currentTimeMillis() - alert.timestamp();
            if (ageMillis > TimeUnit.MINUTES.toMillis(5)) {
                log.warn("[SendNotificationUseCase] Alert for {} is stale ({} ms old). Discarding.", alert.coinName(), ageMillis);
                return;
            }
        }
        
        String condition = "BELOW_OR_EQUAL".equals(alert.alertType()) 
                ? "just dropped below your target price!"
                : "is now above your target price!";
                
        String alertTitle = String.format(java.util.Locale.US, "*%s* %s", alert.coinName().toUpperCase(), condition);

        // US Locales for currency/percentages
        String currentPriceFormatted = String.format(java.util.Locale.US, "%,.2f", alert.currentPrice());
        String targetPriceFormatted = String.format(java.util.Locale.US, "%,.2f", alert.priceTargeted());
        
        String changeStr = "";
        if (alert.change24h() != null) {
            String emoji = alert.change24h() >= 0 ? "📈" : "📉";
            String prefix = alert.change24h() >= 0 ? "+" : "";
            changeStr = String.format(java.util.Locale.US, "%s Change (24h): %s%.2f%%", emoji, prefix, alert.change24h());
        }

        String message = String.format(java.util.Locale.US,
            "%s\n\n" +
            "💰 *Current price:* %s\n" +
            "🎯 *Your target:* %s\n" +
            "%s\n\n" +
            "⏱️ %s",
            alertTitle,
            currentPriceFormatted,
            targetPriceFormatted,
            changeStr,
            java.time.LocalTime.now().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"))
        );
        
        notificationProvider.sendNotification(alert.phoneNumber(), message);
    }
}
