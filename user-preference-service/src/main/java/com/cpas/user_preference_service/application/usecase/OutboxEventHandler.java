package com.cpas.user_preference_service.application.usecase;

import com.cpas.user_preference_service.domain.model.OutboxEvent;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface OutboxEventHandler {
    boolean supports(String eventType);
    void handle(OutboxEvent event) throws JsonProcessingException;
}
