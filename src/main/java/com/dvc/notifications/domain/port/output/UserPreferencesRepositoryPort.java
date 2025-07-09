package com.dvc.notifications.domain.port.output;

import com.dvc.notifications.domain.model.UserPreferences;

public interface UserPreferencesRepositoryPort {
    UserPreferences save(UserPreferences preferences);
    UserPreferences findByUserId(String userId);
} 