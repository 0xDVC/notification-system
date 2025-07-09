package com.dvc.notifications.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.dvc.notifications.application.NotificationStatusQueryUseCase;
import com.dvc.notifications.application.SendNotificationUseCase;
import com.dvc.notifications.domain.port.output.EmailSenderPort;
import com.dvc.notifications.domain.port.output.NotificationRepositoryPort;
import com.dvc.notifications.domain.port.output.PushNotificationSenderPort;
import com.dvc.notifications.domain.port.output.SmsSenderPort;

@Configuration
public class AppConfig {
    @Bean
    public SendNotificationUseCase sendNotificationUseCase(
            NotificationRepositoryPort notificationRepository,
            EmailSenderPort emailSender,
            SmsSenderPort smsSender,
            PushNotificationSenderPort pushSender) {
        return new SendNotificationUseCase(
                notificationRepository,
                emailSender,
                smsSender,
                pushSender);
    }

    @Bean
    public NotificationStatusQueryUseCase notificationStatusQueryUseCase(
            NotificationRepositoryPort notificationRepository) {
        return new NotificationStatusQueryUseCase(notificationRepository);
    }
} 