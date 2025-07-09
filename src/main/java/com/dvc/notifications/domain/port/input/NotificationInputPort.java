package com.dvc.notifications.domain.port.input;

import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.model.NotificationResult;

public interface NotificationInputPort {
    NotificationResult sendNotification(Notification notification);
} 