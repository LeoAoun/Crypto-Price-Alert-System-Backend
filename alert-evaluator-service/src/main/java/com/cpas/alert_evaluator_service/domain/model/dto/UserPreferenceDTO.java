package com.cpas.alert_evaluator_service.domain.model.dto;

import java.util.UUID;

public record UserPreferenceDTO(UUID id, String phoneNumber, String coinName, Double priceTargeted,
        Boolean notifyWhenBelow, Boolean notifyWhenAbove) {
}
