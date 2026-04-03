package com.cpas.alert_evaluator_service.application.port.in;

import com.cpas.alert_evaluator_service.domain.model.UserPreference;
import java.util.UUID;

public interface SyncPreferenceUseCase {
    void syncPreference(UserPreference preference);
    void deletePreference(UUID id);
}
