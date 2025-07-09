package com.dvc.notifications.application;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dvc.notifications.domain.exception.NotificationNotFoundException;
import com.dvc.notifications.domain.model.NotificationStatus;
import com.dvc.notifications.domain.port.input.NotificationStatusInputPort;
import com.dvc.notifications.domain.port.output.NotificationRepositoryPort;

@Service
public class NotificationStatusQueryUseCase implements NotificationStatusInputPort {
    private final NotificationRepositoryPort notificationRepository;

    @Autowired
    public NotificationStatusQueryUseCase(NotificationRepositoryPort notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public NotificationStatus getNotificationStatus(Long notificationId) {
        var notification = notificationRepository.findById(notificationId);
        if (notification == null) {
            throw new NotificationNotFoundException(notificationId);
        }
        return notification.getStatus();
    }
} 