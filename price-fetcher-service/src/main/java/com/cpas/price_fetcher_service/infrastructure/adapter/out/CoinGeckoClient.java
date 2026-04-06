package com.cpas.price_fetcher_service.infrastructure.adapter.out;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import com.cpas.price_fetcher_service.application.port.out.PriceClient;
import com.cpas.price_fetcher_service.application.port.out.PriceData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CoinGeckoClient implements PriceClient {

    private final RestClient restClient;

    public CoinGeckoClient(@Value("${crypto.api.base-url}") String baseUrl) {
        this.restClient = RestClient.create(baseUrl);
    }

    @Override
    @CircuitBreaker(name = "coinGecko", fallbackMethod = "getPriceFallback")
    public Optional<PriceData> getPrice(String coinId, String currency) {
        Map<String, Map<String, Double>> response = restClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/api/v3/simple/price")
                        .queryParam("ids", coinId)
                        .queryParam("vs_currencies", currency)
                        .queryParam("include_24hr_change", "true")
                        .build())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
                
        if (response != null && response.containsKey(coinId)) {
            Double price = response.get(coinId).get(currency);
            Double change24h = response.get(coinId).get(currency + "_24h_change");
            return Optional.of(new PriceData(price, change24h));
        }
        return Optional.empty();
    }

    public Optional<PriceData> getPriceFallback(String coinId, String currency, Throwable t) {
        log.error("CircuitBreaker FALLBACK for {}: {}", coinId, t.getMessage());
        return Optional.empty();
    }
}
