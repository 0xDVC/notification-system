package com.dvc.notifications.domain.exception;

public class NotificationRateLimitException extends NotificationException {
    
    public NotificationRateLimitException(String message) {
        super(message);
    }
    
    public NotificationRateLimitException(String message, Throwable cause) {
        super(message, cause);
    }
} 