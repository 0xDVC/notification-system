package com.dvc.notifications.adapter.output.email;

import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.model.NotificationResult;
import com.dvc.notifications.domain.port.output.EmailSenderPort;

public class MockSesEmailSenderAdapter implements EmailSenderPort {

    @Override
    public NotificationResult sendEmail(Notification notification) {
        System.out.println("[MOCK] Email sent to: " + notification.getRecipient());
        System.out.println("[MOCK] Message: " + notification.getMessage());
        System.out.println("[MOCK] Email would be sent via AWS SES in production");
        
        return NotificationResult.successSent();
    }
} 