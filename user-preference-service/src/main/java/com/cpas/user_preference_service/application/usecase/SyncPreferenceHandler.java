package com.cpas.user_preference_service.application.usecase;

import org.springframework.stereotype.Component;

import com.cpas.user_preference_service.domain.model.OutboxEvent;
import com.cpas.user_preference_service.domain.model.OutboxEventType;
import com.cpas.user_preference_service.domain.model.UserPreference;
import com.cpas.user_preference_service.application.port.out.PreferencePublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class SyncPreferenceHandler implements OutboxEventHandler {

    private final PreferencePublisher preferencePublisher;
    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(String eventType) {
        return OutboxEventType.SYNC.name().equals(eventType);
    }

    @Override
    public void handle(OutboxEvent event) throws JsonProcessingException {
        log.info("Handling sync event for preference: {}", event.getAggregateId());
        UserPreference preference = objectMapper.readValue(event.getPayload(), UserPreference.class);
        preferencePublisher.sendPreferenceSync(preference);
    }
}
