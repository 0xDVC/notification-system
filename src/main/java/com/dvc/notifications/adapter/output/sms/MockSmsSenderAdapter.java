package com.dvc.notifications.adapter.output.sms;

import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.model.NotificationResult;
import com.dvc.notifications.domain.port.output.SmsSenderPort;

public class MockSmsSenderAdapter implements SmsSenderPort {

    @Override
    public NotificationResult sendSms(Notification notification) {
        System.out.println("[MOCK] SMS sent to: " + notification.getRecipient());
        System.out.println("[MOCK] Message: " + notification.getMessage());
        System.out.println("[MOCK] SMS would be sent via MNotify in production");
        
        return NotificationResult.successSent();
    }
} 