package com.cpas.alert_evaluator_service.infrastructure.messaging.producer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.cpas.alert_evaluator_service.domain.model.dto.CryptoAlert;

@Service
public class AlertProducer {
    private final KafkaTemplate<String, CryptoAlert> kafkaTemplate;
    private final String alertTopic;

    public AlertProducer(KafkaTemplate<String, CryptoAlert> kafkaTemplate,
            @Value("${app.topics.alerts}") String alertTopic) {
        this.kafkaTemplate = kafkaTemplate;
        this.alertTopic = alertTopic;
    }

    public void sendAlert(CryptoAlert cryptoAlert) {
        kafkaTemplate.send(alertTopic, cryptoAlert);
    }
}
