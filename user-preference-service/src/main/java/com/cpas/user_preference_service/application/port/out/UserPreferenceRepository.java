package com.cpas.user_preference_service.application.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.cpas.user_preference_service.domain.model.UserPreference;

public interface UserPreferenceRepository {

    List<UserPreference> findByUserId(UUID userId);
    
    UserPreference save(UserPreference preference);
    
    void deleteById(UUID id);

    List<UserPreference> findAll(int page, int size);
    List<UserPreference> findAll();

    Optional<UserPreference> findById(UUID id);

    void deleteAll();
}