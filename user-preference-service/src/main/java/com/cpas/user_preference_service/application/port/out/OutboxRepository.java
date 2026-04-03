package com.cpas.user_preference_service.application.port.out;

import java.util.List;
import java.util.UUID;

import com.cpas.user_preference_service.domain.model.OutboxEvent;

public interface OutboxRepository {
    List<OutboxEvent> findByProcessedFalseOrderByCreatedAtAsc();
    OutboxEvent save(OutboxEvent event);
}
