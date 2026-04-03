package com.cpas.notification_service.infrastructure.adapter.out;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WhatsappClient implements com.cpas.notification_service.application.port.out.NotificationProvider {

    private final RestClient restClient;
    
    @Value("${callmebot.whatsapp.url}")
    private String apiUrl;
    
    @Value("${callmebot.whatsapp.phone}")
    private String defaultPhone;
    
    @Value("${callmebot.whatsapp.apikey}")
    private String apikey;

    public WhatsappClient(RestClient restClient) {
        this.restClient = restClient;
    }

    public void sendNotification(String phone, String message) {
        String targetPhone = (phone != null && !phone.isBlank()) ? phone : defaultPhone;
        
        try {
            log.info("Sending WhatsApp notification to {}: {}", targetPhone, message);

            // Manual UTF-8 encoding is the most robust way for CallMeBot
            String encodedText = java.net.URLEncoder.encode(message, java.nio.charset.StandardCharsets.UTF_8.name());
            String fullUrl = String.format("%s?phone=%s&text=%s&apikey=%s", apiUrl, targetPhone, encodedText, apikey);
            
            restClient.get()
                    .uri(java.net.URI.create(fullUrl))
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.error("Error sending WhatsApp notification: {}", e.getMessage());
        }
    }
}
