package com.dvc.notifications.adapter.output.persistence.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dvc.notifications.adapter.output.persistence.entity.NotificationAttemptEntity;

public interface NotificationAttemptJpaRepository extends JpaRepository<NotificationAttemptEntity, Long> {
    List<NotificationAttemptEntity> findAllByNotificationId(Long notificationId);
} 