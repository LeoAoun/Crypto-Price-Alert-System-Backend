package com.cpas.identity_service.application.port.in;

import com.cpas.identity_service.domain.model.User;

public interface AuthenticateUserUseCase {
    String authenticate(String phoneNumber, String password);
}
