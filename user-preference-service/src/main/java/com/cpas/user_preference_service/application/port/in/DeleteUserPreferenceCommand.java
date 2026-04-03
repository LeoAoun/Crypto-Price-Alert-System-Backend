package com.cpas.user_preference_service.application.port.in;

import java.util.List;
import java.util.UUID;

public record DeleteUserPreferenceCommand(
    UUID id,
    UUID userId,
    List<String> roles
) {}
