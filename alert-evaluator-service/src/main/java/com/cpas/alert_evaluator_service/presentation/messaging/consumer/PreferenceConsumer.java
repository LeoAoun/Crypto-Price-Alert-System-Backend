package com.cpas.alert_evaluator_service.presentation.messaging.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.cpas.alert_evaluator_service.domain.model.UserPreference;
import com.cpas.alert_evaluator_service.application.port.in.SyncPreferenceUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

// Kafka consumer that synchronizes user preferences.
@Slf4j
@Service
@RequiredArgsConstructor
public class PreferenceConsumer {

    private final SyncPreferenceUseCase preferenceSyncService;

    @KafkaListener(topics = "${app.topics.preferences}", groupId = "alert-evaluator-sync-group")
    public void listen(@Payload(required = false) UserPreference preference, 
                       @Header(KafkaHeaders.RECEIVED_KEY) String id) {
        if (preference == null) {
            preferenceSyncService.deletePreference(UUID.fromString(id));
        } else {
            preferenceSyncService.syncPreference(preference);
        }
    }
}

