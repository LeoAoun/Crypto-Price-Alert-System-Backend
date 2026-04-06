package com.cpas.alert_evaluator_service.domain.model.dto;

public record CryptoAlert(
        String id,
        String phoneNumber,
        String coinName,
        Double currentPrice,
        Double priceTargeted,
        String alertType,
        Double change24h,
        Long timestamp) {
}
