package com.cpas.user_preference_service.application.port.in;

import java.util.List;
import java.util.UUID;

import com.cpas.user_preference_service.domain.model.UserPreference;

public interface ManageUserPreferenceUseCase {
    UserPreference createUserPreference(CreateUserPreferenceCommand command);
    List<UserPreference> getAllUserPreferences(int page, int size);
    List<UserPreference> getUserPreferencesByUserId(UUID userId);
    void updateLastAlertTime(UUID id);
    void deleteUserPreferenceById(DeleteUserPreferenceCommand command);
    void deleteAllUserPreferences();
}
