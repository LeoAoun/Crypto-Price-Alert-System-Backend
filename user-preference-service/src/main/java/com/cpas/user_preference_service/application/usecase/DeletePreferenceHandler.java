package com.cpas.user_preference_service.application.usecase;

import org.springframework.stereotype.Component;

import com.cpas.user_preference_service.domain.model.OutboxEvent;
import com.cpas.user_preference_service.domain.model.OutboxEventType;
import com.cpas.user_preference_service.application.port.out.PreferencePublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeletePreferenceHandler implements OutboxEventHandler {

    private final PreferencePublisher preferencePublisher;

    @Override
    public boolean supports(String eventType) {
        return OutboxEventType.DELETE.name().equals(eventType);
    }

    @Override
    public void handle(OutboxEvent event) {
        log.info("Handling delete event for preference: {}", event.getAggregateId());
        preferencePublisher.sendPreferenceDelete(event.getAggregateId());
    }
}
