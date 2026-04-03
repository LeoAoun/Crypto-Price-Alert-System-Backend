package com.cpas.alert_evaluator_service.presentation.messaging.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import com.cpas.alert_evaluator_service.domain.model.dto.CryptoPrice;
import com.cpas.alert_evaluator_service.application.port.in.EvaluateAlertUseCase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Kafka consumer that triggers the evaluation of price alerts.
@Slf4j
@Service
@RequiredArgsConstructor
public class EvaluatorConsumer {

    private final EvaluateAlertUseCase evaluateAlertUseCase;

    @KafkaListener(topics = "${app.topics.prices}", groupId = "alert-evaluator-group")
    public void listen(CryptoPrice cryptoPrice) {
        log.info("Received price update from Kafka: {}", cryptoPrice.coin());
        evaluateAlertUseCase.evaluatePriceUpdate(cryptoPrice);
    }

}

