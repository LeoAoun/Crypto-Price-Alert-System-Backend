package com.cpas.identity_service.application.port.out;

import com.cpas.identity_service.domain.model.User;

public interface TokenGeneratorPort {
    String generateToken(User user);
}
