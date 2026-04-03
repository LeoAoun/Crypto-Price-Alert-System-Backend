package com.cpas.identity_service.application.usecase;

import com.cpas.identity_service.application.port.in.RegisterUserCommand;
import com.cpas.identity_service.application.port.in.RegisterUserUseCase;
import com.cpas.identity_service.application.port.out.PasswordEncoderPort;
import com.cpas.identity_service.application.port.out.UserPersistencePort;
import com.cpas.identity_service.domain.exception.DuplicateResourceException;
import com.cpas.identity_service.domain.model.Role;
import com.cpas.identity_service.domain.model.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserPersistencePort userPersistencePort;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    public User register(RegisterUserCommand command) {
        if (userPersistencePort.existsByUsername(command.username())) {
            throw new DuplicateResourceException("Username already exists");
        }
        if (userPersistencePort.existsByPhoneNumber(command.phoneNumber())) {
            throw new DuplicateResourceException("Phone number already exists");
        }

        List<Role> roleSet = Collections.singletonList(Role.USER);

        User user = User.builder()
                .id(java.util.UUID.randomUUID())
                .username(command.username())
                .phoneNumber(command.phoneNumber())
                .password(passwordEncoderPort.encode(command.password()))
                .roles(roleSet)
                .build();

        return userPersistencePort.save(user);
    }
}
