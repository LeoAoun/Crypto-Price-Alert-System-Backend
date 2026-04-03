package com.cpas.notification_service.application.port.out;

public interface NotificationProvider {
    void sendNotification(String to, String message);
}
