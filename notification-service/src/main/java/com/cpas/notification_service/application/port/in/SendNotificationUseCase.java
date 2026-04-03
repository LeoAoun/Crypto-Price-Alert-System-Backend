package com.cpas.notification_service.application.port.in;

import com.cpas.notification_service.domain.model.dto.PriceAlert;

public interface SendNotificationUseCase {
    void processAlert(PriceAlert alert);
}
