package com.dvc.notifications.domain.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationAttempt {
    private Long id;
    private Long notificationId;
    private int attemptNumber;
    private NotificationChannel channel;
    private NotificationStatus status;
    private String errorMessage;
    private LocalDateTime attemptedAt;
} 