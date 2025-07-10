package com.dvc.notifications.adapter.output.sms;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.dvc.notifications.domain.exception.NotificationSendException;
import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.model.NotificationResult;
import com.dvc.notifications.domain.port.output.SmsSenderPort;

// @Component
public class MNotifySmsSenderAdapter implements SmsSenderPort {
    private final RestTemplate restTemplate;
    
    @Value("${mnotify.api.key}")
    private String apiKey;
    
    @Value("${mnotify.api.url:https://api.mnotify.com/api}")
    private String apiUrl;
    
    @Value("${mnotify.sender}")
    private String sender;

    @Autowired
    public MNotifySmsSenderAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public NotificationResult sendSms(Notification notification) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("recipient", List.of(notification.getRecipient()));
            requestBody.put("sender", sender);
            requestBody.put("message", notification.getMessage());
            requestBody.put("is_schedule", false);
            requestBody.put("schedule_date", "");

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            
            // Make request to MNotify API with API key as query parameter
            String url = apiUrl + "/sms/quick?key=" + apiKey;
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.postForObject(url, request, Map.class);
            
            if (response != null && "success".equals(response.get("status"))) {
                System.out.println("SMS sent successfully to: " + notification.getRecipient());
                System.out.println("Campaign ID: " + response.get("summary"));
                return NotificationResult.successSent();
            } else {
                String errorMessage = response != null ? 
                    (String) response.get("message") : "Unknown error from MNotify API";
                throw new NotificationSendException("SMS send failed: " + errorMessage);
            }
        } catch (NotificationSendException | org.springframework.web.client.RestClientException e) {
            if (e instanceof NotificationSendException) {
                throw e;
            } else {
                System.err.println("SMS send failed: " + e.getMessage());
                throw new NotificationSendException("SMS send failed: " + e.getMessage(), e);
            }
        }
    }
} 