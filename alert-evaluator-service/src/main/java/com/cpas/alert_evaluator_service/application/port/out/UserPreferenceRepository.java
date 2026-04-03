package com.cpas.alert_evaluator_service.application.port.out;

import java.util.List;
import java.util.UUID;

import com.cpas.alert_evaluator_service.domain.model.UserPreference;

public interface UserPreferenceRepository {

    List<UserPreference> findByBelowAlerts(String coinName, Double currentPrice);

    List<UserPreference> findByAboveAlerts(String coinName, Double currentPrice);
    
    UserPreference save(UserPreference preference);
    
    void deleteById(UUID id);
}
