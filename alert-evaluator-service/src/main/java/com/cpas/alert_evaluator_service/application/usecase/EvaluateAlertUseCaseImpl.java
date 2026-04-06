package com.cpas.alert_evaluator_service.application.usecase;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cpas.alert_evaluator_service.domain.model.dto.CryptoAlert;
import com.cpas.alert_evaluator_service.domain.model.dto.CryptoPrice;
import com.cpas.alert_evaluator_service.domain.model.UserPreference;
import com.cpas.alert_evaluator_service.infrastructure.messaging.producer.AlertProducer;
import com.cpas.alert_evaluator_service.application.port.out.UserPreferenceRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Service responsible for evaluating price updates against local user preferences.
@Slf4j
@Service
@RequiredArgsConstructor
public class EvaluateAlertUseCaseImpl implements com.cpas.alert_evaluator_service.application.port.in.EvaluateAlertUseCase {

    private static final int DEFAULT_COOLDOWN_MINUTES = 60;
    private final UserPreferenceRepository userPreferenceRepository;
    private final AlertProducer alertProducer;

    @Transactional
    public void evaluatePriceUpdate(CryptoPrice cryptoPrice) {
        log.info("Processing price update: {}: {}", cryptoPrice.coin(), cryptoPrice.priceUsd());

        List<UserPreference> belowOrEqualAlerts = userPreferenceRepository
                .findByBelowAlerts(cryptoPrice.coin(), cryptoPrice.priceUsd());
        List<UserPreference> aboveOrEqualAlerts = userPreferenceRepository
                .findByAboveAlerts(cryptoPrice.coin(), cryptoPrice.priceUsd());

        processAlerts(belowOrEqualAlerts, cryptoPrice, "BELOW_OR_EQUAL");
        processAlerts(aboveOrEqualAlerts, cryptoPrice, "ABOVE_OR_EQUAL");
    }

    private void processAlerts(List<UserPreference> preferences, CryptoPrice cryptoPrice, String type) {
        LocalDateTime now = LocalDateTime.now();

        preferences.stream()
                .filter(preference -> isEligibleForAlert(preference, now))
                .forEach(preference -> {
                    triggerAlert(preference, cryptoPrice, type, now);
                });
    }

    private boolean isEligibleForAlert(UserPreference preference, LocalDateTime now) {
        if (preference.getLastAlertTime() == null) {
            return true;
        }
        int cooldown = (preference.getCooldownMinutes() != null) ? preference.getCooldownMinutes() : DEFAULT_COOLDOWN_MINUTES;
        LocalDateTime nextAllowedAlert = preference.getLastAlertTime().plusMinutes(cooldown);
        return now.isAfter(nextAllowedAlert);
    }

    private void triggerAlert(UserPreference preference, CryptoPrice cryptoPrice, String type, LocalDateTime now) {
        log.info("Triggering alert '{}' for {}: target {}, current {}",
                type, preference.getPhoneNumber(), preference.getPriceTargeted(), cryptoPrice.priceUsd());


        CryptoAlert cryptoAlert = new CryptoAlert(
                preference.getId().toString(),
                preference.getPhoneNumber(),
                preference.getCoinName(),
                cryptoPrice.priceUsd(),
                preference.getPriceTargeted(),
                type,
                cryptoPrice.change24h(),
                System.currentTimeMillis()
        );

        alertProducer.sendAlert(cryptoAlert);

        preference.setLastAlertTime(now);
        userPreferenceRepository.save(preference);
    }
}
