package com.cpas.user_preference_service.infrastructure.adapter.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cpas.user_preference_service.application.port.out.OutboxRepository;
import com.cpas.user_preference_service.domain.model.OutboxEvent;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OutboxRepositoryAdapter implements OutboxRepository {

    private final SpringDataOutboxRepository springDataRepository;

    @Override
    public List<OutboxEvent> findByProcessedFalseOrderByCreatedAtAsc() {
        return springDataRepository.findByProcessedFalseOrderByCreatedAtAsc();
    }

    @Override
    public OutboxEvent save(OutboxEvent event) {
        return springDataRepository.save(event);
    }
}
