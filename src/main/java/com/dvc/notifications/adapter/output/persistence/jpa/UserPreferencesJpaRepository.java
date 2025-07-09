package com.dvc.notifications.adapter.output.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dvc.notifications.adapter.output.persistence.entity.UserPreferencesEntity;

public interface UserPreferencesJpaRepository extends JpaRepository<UserPreferencesEntity, Long> {
    UserPreferencesEntity findByUserId(String userId);
} 