package com.dvc.notifications.domain.port.output;

import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.model.NotificationResult;

public interface EmailSenderPort {
    NotificationResult sendEmail(Notification notification);
} 