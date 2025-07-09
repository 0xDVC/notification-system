package com.dvc.notifications.domain.model;

public enum NotificationStatus {
    QUEUED,
    SENDING,
    SENT,
    DELIVERED,
    OPENED,
    FAILED,
    DROPPED
} 