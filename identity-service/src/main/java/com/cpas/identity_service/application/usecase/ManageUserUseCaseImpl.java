package com.cpas.identity_service.application.usecase;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cpas.identity_service.application.port.in.ManageUserUseCase;
import com.cpas.identity_service.application.port.out.UserPersistencePort;
import com.cpas.identity_service.domain.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManageUserUseCaseImpl implements ManageUserUseCase {

    private final UserPersistencePort userPersistencePort;

    @Override
    public List<User> getAllUsers(int page, int size) {
        return userPersistencePort.findAll(page, size);
    }

    @Override
    public User getUserById(UUID id) {
        return userPersistencePort.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @Override
    public void deleteUser(UUID id) {
        userPersistencePort.deleteById(id);
    }
}
