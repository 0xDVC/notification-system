package com.dvc.notifications.domain.port.output;

import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.model.NotificationResult;

public interface SmsSenderPort {
    NotificationResult sendSms(Notification notification);
} 