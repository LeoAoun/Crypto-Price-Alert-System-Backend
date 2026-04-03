package com.cpas.alert_evaluator_service.application.usecase;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cpas.alert_evaluator_service.domain.model.UserPreference;
import com.cpas.alert_evaluator_service.application.port.out.UserPreferenceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Service responsible for synchronizing the local user preference cache.
@Slf4j
@Service
@RequiredArgsConstructor
public class SyncPreferenceUseCaseImpl implements com.cpas.alert_evaluator_service.application.port.in.SyncPreferenceUseCase {

    private final UserPreferenceRepository userPreferenceRepository;

    @Transactional
    public void syncPreference(UserPreference preference) {
        log.info("Syncing preference: {}", preference.getId());
        userPreferenceRepository.save(preference);
    }

    @Transactional
    public void deletePreference(UUID id) {
        log.info("Removing preference: {}", id);
        userPreferenceRepository.deleteById(id);
    }

}
