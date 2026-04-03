package com.cpas.price_fetcher_service.application.port.out;

import java.util.Optional;

public interface PriceClient {
    Optional<PriceData> getPrice(String coinId, String currency);
}
