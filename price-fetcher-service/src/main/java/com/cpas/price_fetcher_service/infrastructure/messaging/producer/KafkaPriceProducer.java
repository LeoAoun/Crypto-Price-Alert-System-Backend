package com.cpas.price_fetcher_service.infrastructure.messaging.producer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.cpas.price_fetcher_service.domain.model.CryptoPrice;

@Slf4j
@Service
public class KafkaPriceProducer implements com.cpas.price_fetcher_service.application.port.out.PricePublisher {

    private final KafkaTemplate<String, CryptoPrice> kafkaTemplate;
    private final String topicName;

    public KafkaPriceProducer(
            KafkaTemplate<String, CryptoPrice> kafkaTemplate,
            @Value("${crypto.kafka.topic.name}") String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public void sendPriceUpdate(CryptoPrice cryptoPrice) {
        log.info("Sending price update to Kafka: {}", cryptoPrice);
        kafkaTemplate.send(topicName, cryptoPrice.getCoin(), cryptoPrice);
    }
}