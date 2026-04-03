package com.cpas.user_preference_service.presentation.messaging.consumer;

import java.util.UUID;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.cpas.user_preference_service.application.port.in.ManageUserPreferenceUseCase;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AlertConsumer {
    private final ManageUserPreferenceUseCase manageUserPreferenceUseCase;

    public AlertConsumer(ManageUserPreferenceUseCase manageUserPreferenceUseCase) {
        this.manageUserPreferenceUseCase = manageUserPreferenceUseCase;
    }

    @KafkaListener(topics = "${app.topics.alerts}", groupId = "user-preference-alert-group")
    public void listen(CryptoAlert alert) {
        log.info("Received fired alert for {}: Updating cooldown for ID {}", alert.phoneNumber(), alert.id());
        manageUserPreferenceUseCase.updateLastAlertTime(UUID.fromString(alert.id()));
    }

    // Inner class or move to model if shared, but for now we need the ID from the alert message
    public record CryptoAlert(String id, String phoneNumber, String coinName, Double currentPrice, Double priceTargeted) {}
}
