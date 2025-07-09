package com.dvc.notifications.domain.model;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Notification Domain Model Tests")
class NotificationTest {

    @Test
    @DisplayName("Should create notification with builder")
    void shouldCreateNotificationWithBuilder() {
        LocalDateTime now = LocalDateTime.now();
        Notification notification = Notification.builder()
                .id(1L)
                .recipient("test@example.com")
                .channel(NotificationChannel.EMAIL)
                .message("Test message")
                .status(NotificationStatus.QUEUED)
                .createdAt(now)
                .updatedAt(now)
                .build();

        assertEquals(1L, notification.getId());
        assertEquals("test@example.com", notification.getRecipient());
        assertEquals(NotificationChannel.EMAIL, notification.getChannel());
        assertEquals("Test message", notification.getMessage());
        assertEquals(NotificationStatus.QUEUED, notification.getStatus());
        assertEquals(now, notification.getCreatedAt());
        assertEquals(now, notification.getUpdatedAt());
    }

    @Test
    @DisplayName("Should create notification with no args constructor")
    void shouldCreateNotificationWithNoArgsConstructor() {
        Notification notification = new Notification();
        assertNotNull(notification);
    }

    @Test
    @DisplayName("Should create notification with all args constructor")
    void shouldCreateNotificationWithAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        Notification notification = new Notification(
                1L, "test@example.com", NotificationChannel.SMS,
                "Test message", NotificationStatus.SENT, now, now
        );

        assertEquals(1L, notification.getId());
        assertEquals("test@example.com", notification.getRecipient());
        assertEquals(NotificationChannel.SMS, notification.getChannel());
        assertEquals("Test message", notification.getMessage());
        assertEquals(NotificationStatus.SENT, notification.getStatus());
    }

    @Test
    @DisplayName("Should set and get notification properties")
    void shouldSetAndGetNotificationProperties() {
        Notification notification = new Notification();
        LocalDateTime now = LocalDateTime.now();

        notification.setId(1L);
        notification.setRecipient("test@example.com");
        notification.setChannel(NotificationChannel.PUSH);
        notification.setMessage("Test message");
        notification.setStatus(NotificationStatus.SENDING);
        notification.setCreatedAt(now);
        notification.setUpdatedAt(now);

        assertEquals(1L, notification.getId());
        assertEquals("test@example.com", notification.getRecipient());
        assertEquals(NotificationChannel.PUSH, notification.getChannel());
        assertEquals("Test message", notification.getMessage());
        assertEquals(NotificationStatus.SENDING, notification.getStatus());
        assertEquals(now, notification.getCreatedAt());
        assertEquals(now, notification.getUpdatedAt());
    }
} 