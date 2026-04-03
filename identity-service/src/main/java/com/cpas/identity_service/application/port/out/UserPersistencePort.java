package com.cpas.identity_service.application.port.out;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.cpas.identity_service.domain.model.User;

public interface UserPersistencePort {
    User save(User user);
    Optional<User> findByUsername(String username);
    Optional<User> findByPhoneNumber(String phoneNumber);
    boolean existsByUsername(String username);
    boolean existsByPhoneNumber(String phoneNumber);
    List<User> findAll(int page, int size);
    Optional<User> findById(UUID id);
    void deleteById(UUID id);
}
