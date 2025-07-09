package com.dvc.notifications.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

import com.dvc.notifications.adapter.output.email.MockSesEmailSenderAdapter;
import com.dvc.notifications.adapter.output.push.MockPushNotificationSenderAdapter;
import com.dvc.notifications.adapter.output.sms.MockSmsSenderAdapter;
import com.dvc.notifications.domain.port.output.EmailSenderPort;
import com.dvc.notifications.domain.port.output.PushNotificationSenderPort;
import com.dvc.notifications.domain.port.output.SmsSenderPort;

@Configuration
@Profile("dev")
public class MockConfig {

    @Bean
    @Primary
    public EmailSenderPort mockEmailSender() {
        return new MockSesEmailSenderAdapter();
    }

    @Bean
    @Primary
    public SmsSenderPort mockSmsSender() {
        return new MockSmsSenderAdapter();
    }

    @Bean
    @Primary
    public PushNotificationSenderPort mockPushSender() {
        return new MockPushNotificationSenderAdapter();
    }
} 