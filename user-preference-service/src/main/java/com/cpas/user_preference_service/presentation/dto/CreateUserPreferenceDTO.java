package com.cpas.user_preference_service.presentation.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateUserPreferenceDTO(
        @NotBlank(message = "Coin name is required")
        String coinName,

        @NotNull(message = "Target price is required")
        @Positive(message = "Target price must be positive")
        Double priceTargeted,

        @NotNull(message = "Notify when below is required")
        Boolean notifyWhenBelow,

        @NotNull(message = "Notify when above is required")
        Boolean notifyWhenAbove,

        @Min(value = 10, message = "Minimum cooldown is 10 minutes")
        Integer cooldownMinutes) {
}


