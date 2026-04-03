package com.cpas.user_preference_service.infrastructure.adapter.out.messaging.producer;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.cpas.user_preference_service.application.port.out.PreferencePublisher;
import com.cpas.user_preference_service.domain.model.UserPreference;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class PreferenceProducer implements PreferencePublisher {
    private final KafkaTemplate<String, UserPreference> kafkaTemplate;
    private final String topic;

    public PreferenceProducer(
            KafkaTemplate<String, UserPreference> kafkaTemplate,
            @Value("${app.topics.preferences}") String topic) {
        this.kafkaTemplate = kafkaTemplate;
        this.topic = topic;
    }

    public void sendPreferenceSync(UserPreference preference) {
        log.info("Syncing preference to Kafka: {}", preference.getId());
        kafkaTemplate.send(topic, preference.getId().toString(), preference);
    }

    public void sendPreferenceDelete(UUID id) {
        log.info("Sending deletion signal to Kafka: {}", id);
        kafkaTemplate.send(topic, id.toString(), null);
    }
}
