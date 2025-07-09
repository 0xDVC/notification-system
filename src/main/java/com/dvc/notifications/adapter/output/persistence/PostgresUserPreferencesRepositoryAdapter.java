package com.dvc.notifications.adapter.output.persistence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.dvc.notifications.adapter.output.persistence.entity.UserPreferencesEntity;
import com.dvc.notifications.adapter.output.persistence.jpa.UserPreferencesJpaRepository;
import com.dvc.notifications.domain.exception.NotificationPersistenceException;
import com.dvc.notifications.domain.model.UserPreferences;
import com.dvc.notifications.domain.port.output.UserPreferencesRepositoryPort;

@Component
public class PostgresUserPreferencesRepositoryAdapter implements UserPreferencesRepositoryPort {
    private final UserPreferencesJpaRepository userPreferencesJpaRepository;

    @Autowired
    public PostgresUserPreferencesRepositoryAdapter(UserPreferencesJpaRepository userPreferencesJpaRepository) {
        this.userPreferencesJpaRepository = userPreferencesJpaRepository;
    }

    @Override
    public UserPreferences save(UserPreferences preferences) {
        try {
            UserPreferencesEntity entity = toEntity(preferences);
            UserPreferencesEntity saved = userPreferencesJpaRepository.save(entity);
            return toDomain(saved);
        } catch (Exception e) {
            throw new NotificationPersistenceException("Failed to save user preferences", e);
        }
    }

    @Override
    public UserPreferences findByUserId(String userId) {
        try {
            UserPreferencesEntity entity = userPreferencesJpaRepository.findByUserId(userId);
            return toDomain(entity);
        } catch (Exception e) {
            throw new NotificationPersistenceException("Failed to find user preferences by userId: " + userId, e);
        }
    }

    private UserPreferencesEntity toEntity(UserPreferences preferences) {
        if (preferences == null) return null;
        return UserPreferencesEntity.builder()
                .id(preferences.getId())
                .userId(preferences.getUserId())
                .emailEnabled(preferences.isEmailEnabled())
                .smsEnabled(preferences.isSmsEnabled())
                .pushEnabled(preferences.isPushEnabled())
                .preferredChannel(preferences.getPreferredChannel())
                .build();
    }

    private UserPreferences toDomain(UserPreferencesEntity entity) {
        if (entity == null) return null;
        return UserPreferences.builder()
                .id(entity.getId())
                .userId(entity.getUserId())
                .emailEnabled(entity.isEmailEnabled())
                .smsEnabled(entity.isSmsEnabled())
                .pushEnabled(entity.isPushEnabled())
                .preferredChannel(entity.getPreferredChannel())
                .build();
    }
} 