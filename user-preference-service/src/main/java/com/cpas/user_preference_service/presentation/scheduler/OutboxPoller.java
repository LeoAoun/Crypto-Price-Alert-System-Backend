package com.cpas.user_preference_service.presentation.scheduler;

import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.cpas.user_preference_service.domain.model.OutboxEvent;
import com.cpas.user_preference_service.application.port.out.OutboxRepository;
import com.cpas.user_preference_service.application.usecase.OutboxEventHandler;
import com.fasterxml.jackson.core.JsonProcessingException;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class OutboxPoller {

    private final OutboxRepository outboxRepository;
    private final List<OutboxEventHandler> handlers;

    @Scheduled(fixedDelay = 2000)
    @Transactional
    public void pollOutbox() {
        List<OutboxEvent> pendingEvents = outboxRepository.findByProcessedFalseOrderByCreatedAtAsc();
        
        if (pendingEvents.isEmpty()) {
            return;
        }

        log.info("Polling outbox: {} pending events found", pendingEvents.size());

        for (OutboxEvent event : pendingEvents) {
            try {
                processEvent(event);
                event.setProcessed(true);
                outboxRepository.save(event);
            } catch (Exception e) {
                log.error("Failed to process outbox event: {}", event.getId(), e);
                // Not marked as processed, so it will be retried
            }
        }
    }

    private void processEvent(OutboxEvent event) throws JsonProcessingException {
        handlers.stream()
                .filter(handler -> handler.supports(event.getEventType()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No handler found for event type: " + event.getEventType()))
                .handle(event);
    }
}
