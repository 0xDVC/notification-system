package com.dvc.notifications.domain.exception;

public class NotificationConfigurationException extends NotificationException {
    
    public NotificationConfigurationException(String message) {
        super(message);
    }
    
    public NotificationConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }
} 