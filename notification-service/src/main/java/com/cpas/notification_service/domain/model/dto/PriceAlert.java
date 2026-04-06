package com.cpas.notification_service.domain.model.dto;

public record PriceAlert(
        String id,
        String phoneNumber,
        String coinName,
        Double currentPrice,
        Double priceTargeted,
        String alertType,
        Double change24h,
        Long timestamp) {
}
