package com.cpas.alert_evaluator_service.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import com.cpas.alert_evaluator_service.application.port.out.UserPreferenceRepository;
import com.cpas.alert_evaluator_service.domain.model.UserPreference;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserPreferenceRepositoryAdapter implements UserPreferenceRepository {

    private final SpringDataUserPreferenceRepository springDataRepository;

    @Override
    public List<UserPreference> findByBelowAlerts(String coinName, Double currentPrice) {
        return springDataRepository.findByBelowAlerts(coinName, currentPrice);
    }

    @Override
    public List<UserPreference> findByAboveAlerts(String coinName, Double currentPrice) {
        return springDataRepository.findByAboveAlerts(coinName, currentPrice);
    }

    @Override
    public UserPreference save(UserPreference preference) {
        return springDataRepository.save(preference);
    }

    @Override
    public void deleteById(UUID id) {
        springDataRepository.deleteById(id);
    }
}
