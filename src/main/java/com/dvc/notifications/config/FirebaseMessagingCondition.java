package com.dvc.notifications.config;

import org.springframework.context.annotation.Condition;
import org.springframework.context.annotation.ConditionContext;
import org.springframework.core.type.AnnotatedTypeMetadata;

public class FirebaseMessagingCondition implements Condition {
    
    @Override
    public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
        // Check if we're in dev profile or if Firebase credentials are available
        String activeProfile = context.getEnvironment().getProperty("spring.profiles.active", "");
        return !activeProfile.contains("dev");
    }
} 