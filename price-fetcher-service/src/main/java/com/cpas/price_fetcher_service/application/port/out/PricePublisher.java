package com.cpas.price_fetcher_service.application.port.out;

import com.cpas.price_fetcher_service.domain.model.CryptoPrice;

public interface PricePublisher {
    void sendPriceUpdate(CryptoPrice price);
}
