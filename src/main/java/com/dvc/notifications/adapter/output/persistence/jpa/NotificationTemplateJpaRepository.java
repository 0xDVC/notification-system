package com.dvc.notifications.adapter.output.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dvc.notifications.adapter.output.persistence.entity.NotificationTemplateEntity;

public interface NotificationTemplateJpaRepository extends JpaRepository<NotificationTemplateEntity, Long> {
    NotificationTemplateEntity findByNameAndChannel(String name, String channel);
} 