package com.cpas.notification_service.presentation.messaging.consumer;

import com.cpas.notification_service.domain.model.dto.PriceAlert;
import com.cpas.notification_service.application.port.in.SendNotificationUseCase;

import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class AlertConsumer {

    private final SendNotificationUseCase sendNotificationUseCase;

    public AlertConsumer(SendNotificationUseCase sendNotificationUseCase) {
        this.sendNotificationUseCase = sendNotificationUseCase;
    }

    @KafkaListener(topics = "${app.topics.alerts}", groupId = "notification-group")
    public void listen(PriceAlert alert) {
        log.info("[AlertConsumer] Received alert from Kafka: {}", alert);
        sendNotificationUseCase.processAlert(alert);
    }
}
