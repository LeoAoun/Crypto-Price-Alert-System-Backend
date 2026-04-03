package com.cpas.user_preference_service.application.port.out;

import java.util.UUID;
import com.cpas.user_preference_service.domain.model.UserPreference;

public interface PreferencePublisher {
    void sendPreferenceSync(UserPreference preference);
    void sendPreferenceDelete(UUID id);
}
