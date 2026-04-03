package com.cpas.identity_service.application.port.in;

public record RegisterUserCommand(
    String username,
    String phoneNumber,
    String password
) {}
