package com.dvc.notifications.domain.port.input;

import com.dvc.notifications.domain.model.NotificationStatus;

public interface NotificationStatusInputPort {
    NotificationStatus getNotificationStatus(Long notificationId);
} 