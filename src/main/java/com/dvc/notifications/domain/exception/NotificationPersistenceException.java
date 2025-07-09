package com.dvc.notifications.domain.exception;

public class NotificationPersistenceException extends NotificationException {
    
    public NotificationPersistenceException(String message) {
        super(message);
    }
    
    public NotificationPersistenceException(String message, Throwable cause) {
        super(message, cause);
    }
} 