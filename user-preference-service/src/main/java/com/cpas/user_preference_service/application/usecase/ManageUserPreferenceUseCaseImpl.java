package com.cpas.user_preference_service.application.usecase;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cpas.user_preference_service.application.port.in.CreateUserPreferenceCommand;
import com.cpas.user_preference_service.application.port.in.DeleteUserPreferenceCommand;
import com.cpas.user_preference_service.application.port.in.ManageUserPreferenceUseCase;
import com.cpas.user_preference_service.application.port.out.OutboxRepository;
import com.cpas.user_preference_service.application.port.out.UserPreferenceRepository;
import com.cpas.user_preference_service.domain.exception.UnauthorizedException;
import com.cpas.user_preference_service.domain.model.OutboxEvent;
import com.cpas.user_preference_service.domain.model.OutboxEventType;
import com.cpas.user_preference_service.domain.model.UserPreference;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ManageUserPreferenceUseCaseImpl implements ManageUserPreferenceUseCase {
    private final UserPreferenceRepository userPreferenceRepository;
    private final OutboxRepository outboxRepository;
    private final ObjectMapper objectMapper;

    @Override
    public UserPreference createUserPreference(CreateUserPreferenceCommand command) {
        UserPreference userPreference = new UserPreference();
        userPreference.setId(UUID.randomUUID());
        userPreference.setUserId(command.userId());
        userPreference.setPhoneNumber(command.phoneNumber());
        userPreference.setCoinName(command.dto().coinName());
        userPreference.setPriceTargeted(command.dto().priceTargeted());
        userPreference.setNotifyWhenBelow(command.dto().notifyWhenBelow());
        userPreference.setNotifyWhenAbove(command.dto().notifyWhenAbove());
        userPreference.setCooldownMinutes(command.dto().cooldownMinutes());
        
        UserPreference saved = userPreferenceRepository.save(userPreference);
        createOutboxEvent(saved.getId(), OutboxEventType.SYNC, saved);
        
        return saved;
    }

    @Override
    public List<UserPreference> getAllUserPreferences(int page, int size) {
        return userPreferenceRepository.findAll(page, size);
    }

    @Override
    public List<UserPreference> getUserPreferencesByUserId(UUID userId) {
        return userPreferenceRepository.findByUserId(userId);
    }

    @Override
    public void updateLastAlertTime(UUID id) {
        userPreferenceRepository.findById(id).ifPresent(p -> {
            p.setLastAlertTime(LocalDateTime.now());
            UserPreference updated = userPreferenceRepository.save(p);
            createOutboxEvent(updated.getId(), OutboxEventType.SYNC, updated);
        });
    }

    @Override
    public void deleteUserPreferenceById(DeleteUserPreferenceCommand command) {
        userPreferenceRepository.findById(command.id()).ifPresent(p -> {
            boolean isAdmin = command.roles() != null && command.roles().contains("ADMIN");
            if (!isAdmin && !p.getUserId().equals(command.userId())) {
                throw new UnauthorizedException("Unauthorized");
            }
            userPreferenceRepository.deleteById(command.id());
            createOutboxEvent(command.id(), OutboxEventType.DELETE, null);
        });
    }

    @Override
    public void deleteAllUserPreferences() {
        List<UserPreference> all = userPreferenceRepository.findAll();
        userPreferenceRepository.deleteAll();
        all.forEach(p -> createOutboxEvent(p.getId(), OutboxEventType.DELETE, null));
    }

    private void createOutboxEvent(UUID aggregateId, OutboxEventType eventType, Object payload) {
        try {
            String jsonPayload = (payload != null) ? objectMapper.writeValueAsString(payload) : null;
            
            OutboxEvent event = OutboxEvent.builder()
                    .id(UUID.randomUUID())
                    .aggregateType("UserPreference")
                    .aggregateId(aggregateId)
                    .eventType(eventType.name())
                    .payload(jsonPayload)
                    .createdAt(LocalDateTime.now())
                    .processed(false)
                    .build();
                    
            outboxRepository.save(event);
            log.info("Outbox event created: {} - {}", eventType, aggregateId);
        } catch (JsonProcessingException e) {
            log.error("Error serializing outbox payload", e);
            throw new RuntimeException("Failed to create outbox event", e);
        }
    }
}


