package com.cpas.user_preference_service.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cpas.user_preference_service.domain.model.UserPreference;

@Repository
public interface SpringDataUserPreferenceRepository extends JpaRepository<UserPreference, UUID> {

        List<UserPreference> findByUserId(UUID userId);
}
