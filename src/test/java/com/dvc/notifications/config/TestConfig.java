package com.dvc.notifications.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.dvc.notifications.domain.model.NotificationResult;
import com.dvc.notifications.domain.port.output.PushNotificationSenderPort;

@TestConfiguration
public class TestConfig {
    
    @Bean
    @Primary
    public PushNotificationSenderPort mockPushNotificationSenderPort() {
        return notification -> {
            System.out.println("Mock push notification sent to: " + notification.getRecipient());
            return NotificationResult.successSent();
        };
    }
} 