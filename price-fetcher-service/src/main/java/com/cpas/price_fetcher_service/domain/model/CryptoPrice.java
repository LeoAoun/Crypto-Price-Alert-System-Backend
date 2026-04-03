package com.cpas.price_fetcher_service.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoPrice {
    private String coin;
    private Double priceUsd;
    private Double change24h;
    private LocalDateTime timestamp;
}