package com.cpas.alert_evaluator_service.application.port.in;

import com.cpas.alert_evaluator_service.domain.model.dto.CryptoPrice;

public interface EvaluateAlertUseCase {
    void evaluatePriceUpdate(CryptoPrice cryptoPrice);
}
