package com.cpas.notification_service.domain.model.dto;

import java.time.LocalDateTime;

public record CryptoPrice(String coin, Double priceUsd, LocalDateTime timestamp) {
}
