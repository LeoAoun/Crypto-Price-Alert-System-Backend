package com.cpas.price_fetcher_service.infrastructure.adapter.out;

import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

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
    public Optional<PriceData> getPrice(String coinId, String currency) {
        try {
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
        } catch (Exception e) {
            log.error("Error fetching price from CoinGecko for coin {}: {}", coinId, e.getMessage());
        }
        return Optional.empty();
    }
}
