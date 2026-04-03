package com.cpas.identity_service.application.usecase;

import com.cpas.identity_service.application.port.in.AuthenticateUserUseCase;
import com.cpas.identity_service.application.port.out.PasswordEncoderPort;
import com.cpas.identity_service.application.port.out.TokenGeneratorPort;
import com.cpas.identity_service.application.port.out.UserPersistencePort;
import com.cpas.identity_service.domain.exception.InvalidCredentialsException;
import com.cpas.identity_service.domain.model.User;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticateUserUseCaseImpl implements AuthenticateUserUseCase {

    private final UserPersistencePort userPersistencePort;
    private final TokenGeneratorPort tokenGeneratorPort;
    private final PasswordEncoderPort passwordEncoderPort;

    @Override
    public String authenticate(String phoneNumber, String password) {
        User user = userPersistencePort.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new InvalidCredentialsException("Invalid credentials"));

        if (!passwordEncoderPort.matches(password, user.getPassword())) {
            throw new InvalidCredentialsException("Invalid credentials");
        }

        return tokenGeneratorPort.generateToken(user);
    }
}
