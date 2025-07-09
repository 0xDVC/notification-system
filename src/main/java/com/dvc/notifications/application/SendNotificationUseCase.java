package com.dvc.notifications.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dvc.notifications.domain.exception.NotificationSendException;
import com.dvc.notifications.domain.exception.NotificationValidationException;
import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.model.NotificationResult;
import com.dvc.notifications.domain.port.input.NotificationInputPort;
import com.dvc.notifications.domain.port.output.EmailSenderPort;
import com.dvc.notifications.domain.port.output.NotificationRepositoryPort;
import com.dvc.notifications.domain.port.output.PushNotificationSenderPort;
import com.dvc.notifications.domain.port.output.SmsSenderPort;

@Service
public class SendNotificationUseCase implements NotificationInputPort {
    private final NotificationRepositoryPort notificationRepository;
    private final EmailSenderPort emailSender;
    private final SmsSenderPort smsSender;
    private final PushNotificationSenderPort pushSender;

    @Autowired
    public SendNotificationUseCase(
            NotificationRepositoryPort notificationRepository,
            EmailSenderPort emailSender,
            SmsSenderPort smsSender,
            PushNotificationSenderPort pushSender) {
        this.notificationRepository = notificationRepository;
        this.emailSender = emailSender;
        this.smsSender = smsSender;
        this.pushSender = pushSender;
    }

    @Override
    public NotificationResult sendNotification(Notification notification) {
        // Validate notification
        if (notification == null) {
            throw new NotificationValidationException("Notification cannot be null");
        }
        if (notification.getRecipient() == null || notification.getRecipient().trim().isEmpty()) {
            throw new NotificationValidationException("Recipient cannot be null or empty");
        }
        if (notification.getMessage() == null || notification.getMessage().trim().isEmpty()) {
            throw new NotificationValidationException("Message cannot be null or empty");
        }
        if (notification.getChannel() == null) {
            throw new NotificationValidationException("Channel cannot be null");
        }

        // Save notification as QUEUED
        notificationRepository.save(notification);
        NotificationResult result = switch (notification.getChannel()) {
            case EMAIL -> {
                var emailResult = emailSender.sendEmail(notification);
                yield (!emailResult.isSuccess() && "OTP".equalsIgnoreCase(notification.getMessage())) 
                    ? smsSender.sendSms(notification) 
                    : emailResult;
            }
            case SMS -> smsSender.sendSms(notification);
            case PUSH -> pushSender.sendPush(notification);
        };
        
        // Update notification status
        notification.setStatus(result.getStatus());
        notificationRepository.save(notification);
        
        // Throw exception if sending failed
        if (!result.isSuccess()) {
            throw new NotificationSendException("Failed to send notification: " + result.getErrorMessage());
        }
        
        return result;
    }
} 