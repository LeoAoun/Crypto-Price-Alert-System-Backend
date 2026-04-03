package com.cpas.alert_evaluator_service.domain.model.dto;

import java.time.LocalDateTime;

public record CryptoPrice(String coin, Double priceUsd, Double change24h, LocalDateTime timestamp) {
}
