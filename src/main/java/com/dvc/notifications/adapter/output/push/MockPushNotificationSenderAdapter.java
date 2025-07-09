package com.dvc.notifications.adapter.output.push;

import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.model.NotificationResult;
import com.dvc.notifications.domain.port.output.PushNotificationSenderPort;

public class MockPushNotificationSenderAdapter implements PushNotificationSenderPort {

    @Override
    public NotificationResult sendPush(Notification notification) {
        System.out.println("📱 [MOCK] Push notification sent to: " + notification.getRecipient());
        System.out.println("📱 [MOCK] Message: " + notification.getMessage());
        System.out.println("📱 [MOCK] Push notification would be sent via Firebase FCM in production");
        
        return NotificationResult.successSent();
    }
} 