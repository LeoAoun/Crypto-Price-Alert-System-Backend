package com.cpas.price_fetcher_service.application.usecase;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.cpas.price_fetcher_service.application.port.in.FetchPriceUseCase;
import com.cpas.price_fetcher_service.application.port.out.PriceClient;
import com.cpas.price_fetcher_service.application.port.out.PricePublisher;
import com.cpas.price_fetcher_service.domain.model.CryptoPrice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class FetchPriceUseCaseImpl implements FetchPriceUseCase {

    private final PriceClient priceClient;
    private final PricePublisher pricePublisher;

    @Override
    public void fetchAndPublishPrice() {
        try {
            priceClient.getPrice("bitcoin", "usd").ifPresent(priceData -> {
                CryptoPrice cryptoPrice = new CryptoPrice("bitcoin", priceData.price(), priceData.change24h(), LocalDateTime.now());
                pricePublisher.sendPriceUpdate(cryptoPrice);
                log.info("Published Bitcoin price: {} (24h change: {}%)", priceData.price(), priceData.change24h());
            });
        } catch (Exception e) {
            log.error("Failed to execute fetch and publish for Bitcoin price: {}", e.getMessage());
        }
    }
}
