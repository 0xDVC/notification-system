package com.dvc.notifications.domain.exception;

public class NotificationSendException extends NotificationException {
    
    public NotificationSendException(String message) {
        super(message);
    }
    
    public NotificationSendException(String message, Throwable cause) {
        super(message, cause);
    }
} 