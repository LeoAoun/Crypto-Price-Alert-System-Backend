package com.cpas.notification_service.domain.model.dto;

public record CryptoAlert(
        String id,
        String phoneNumber,
        String coinName,
        Double currentPrice,
        Double priceTargeted,
        Long timestamp) {
}
