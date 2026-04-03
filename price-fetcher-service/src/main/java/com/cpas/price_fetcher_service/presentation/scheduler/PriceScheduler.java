package com.cpas.price_fetcher_service.presentation.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.cpas.price_fetcher_service.application.port.in.FetchPriceUseCase;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PriceScheduler {

    private final FetchPriceUseCase fetchPriceUseCase;

    @Scheduled(fixedRateString = "${crypto.api.polling-interval}")
    public void fetchAndPublishPrice() {
        fetchPriceUseCase.fetchAndPublishPrice();
    }
}
