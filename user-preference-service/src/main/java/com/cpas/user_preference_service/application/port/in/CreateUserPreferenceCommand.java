package com.cpas.user_preference_service.application.port.in;

import java.util.UUID;
import com.cpas.user_preference_service.presentation.dto.CreateUserPreferenceDTO;

public record CreateUserPreferenceCommand(
    CreateUserPreferenceDTO dto,
    UUID userId,
    String phoneNumber
) {}
