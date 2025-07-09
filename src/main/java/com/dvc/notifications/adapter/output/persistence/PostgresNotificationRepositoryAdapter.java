package com.dvc.notifications.adapter.output.persistence;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dvc.notifications.adapter.output.persistence.entity.NotificationEntity;
import com.dvc.notifications.adapter.output.persistence.jpa.NotificationJpaRepository;
import com.dvc.notifications.domain.exception.NotificationPersistenceException;
import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.port.output.NotificationRepositoryPort;

@Component
public class PostgresNotificationRepositoryAdapter implements NotificationRepositoryPort {
    private final NotificationJpaRepository notificationJpaRepository;

    @Autowired
    public PostgresNotificationRepositoryAdapter(NotificationJpaRepository notificationJpaRepository) {
        this.notificationJpaRepository = notificationJpaRepository;
    }

    @Override
    public Notification save(Notification notification) {
        try {
            NotificationEntity entity = toEntity(notification);
            NotificationEntity saved = notificationJpaRepository.save(entity);
            return toDomain(saved);
        } catch (Exception e) {
            throw new NotificationPersistenceException("Failed to save notification", e);
        }
    }

    @Override
    public Notification findById(Long id) {
        try {
            return notificationJpaRepository.findById(id)
                    .map(this::toDomain)
                    .orElse(null);
        } catch (Exception e) {
            throw new NotificationPersistenceException("Failed to find notification by id: " + id, e);
        }
    }

    @Override
    public List<Notification> findAllByRecipient(String recipient) {
        try {
            return notificationJpaRepository.findAllByRecipient(recipient)
                    .stream()
                    .map(this::toDomain)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new NotificationPersistenceException("Failed to find notifications by recipient: " + recipient, e);
        }
    }

    private NotificationEntity toEntity(Notification notification) {
        if (notification == null) return null;
        return NotificationEntity.builder()
                .id(notification.getId())
                .recipient(notification.getRecipient())
                .channel(notification.getChannel())
                .message(notification.getMessage())
                .status(notification.getStatus())
                .createdAt(notification.getCreatedAt())
                .updatedAt(notification.getUpdatedAt())
                .build();
    }

    private Notification toDomain(NotificationEntity entity) {
        if (entity == null) return null;
        return Notification.builder()
                .id(entity.getId())
                .recipient(entity.getRecipient())
                .channel(entity.getChannel())
                .message(entity.getMessage())
                .status(entity.getStatus())
                .createdAt(entity.getCreatedAt())
                .updatedAt(entity.getUpdatedAt())
                .build();
    }
} 