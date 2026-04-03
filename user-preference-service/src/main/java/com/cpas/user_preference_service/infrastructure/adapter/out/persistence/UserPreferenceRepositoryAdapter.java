package com.cpas.user_preference_service.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import com.cpas.user_preference_service.application.port.out.UserPreferenceRepository;
import com.cpas.user_preference_service.domain.model.UserPreference;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserPreferenceRepositoryAdapter implements UserPreferenceRepository {

    private final SpringDataUserPreferenceRepository springDataRepository;

    @Override
    public List<UserPreference> findByUserId(UUID userId) {
        return springDataRepository.findByUserId(userId);
    }

    @Override
    public UserPreference save(UserPreference preference) {
        return springDataRepository.save(preference);
    }

    @Override
    public void deleteById(UUID id) {
        springDataRepository.deleteById(id);
    }

    @Override
    public List<UserPreference> findAll(int page, int size) {
        return springDataRepository.findAll(PageRequest.of(page, size)).getContent();
    }

    @Override
    public List<UserPreference> findAll() {
        return springDataRepository.findAll();
    }

    @Override
    public Optional<UserPreference> findById(UUID id) {
        return springDataRepository.findById(id);
    }

    @Override
    public void deleteAll() {
        springDataRepository.deleteAll();
    }
}
