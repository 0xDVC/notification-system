package com.dvc.notifications.domain.port.output;

import java.util.List;

import com.dvc.notifications.domain.model.Notification;

public interface NotificationRepositoryPort {
    Notification save(Notification notification);
    Notification findById(Long id);
    List<Notification> findAllByRecipient(String recipient);
} 