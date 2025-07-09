package com.dvc.notifications.domain.port.output;

import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.model.NotificationResult;

public interface PushNotificationSenderPort {
    NotificationResult sendPush(Notification notification);
} 