package com.dvc.notifications.adapter.input.http;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.context.request.WebRequest;

import com.dvc.notifications.domain.exception.NotificationNotFoundException;
import com.dvc.notifications.domain.exception.NotificationSendException;
import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.model.NotificationChannel;
import com.dvc.notifications.domain.model.NotificationResult;
import com.dvc.notifications.domain.model.NotificationStatus;
import com.dvc.notifications.domain.port.input.NotificationInputPort;
import com.dvc.notifications.domain.port.input.NotificationStatusInputPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationAPI Tests")
class NotificationAPITest {

    @Mock
    private NotificationInputPort notificationInputPort;

    @Mock
    private NotificationStatusInputPort notificationStatusInputPort;

    @Mock
    private WebRequest webRequest;

    private NotificationAPI notificationAPI;

    @BeforeEach
    void setUp() {
        notificationAPI = new NotificationAPI(notificationInputPort, notificationStatusInputPort);
    }

    @Test
    @DisplayName("Should send notification successfully")
    void shouldSendNotificationSuccessfully() {
        Notification notification = Notification.builder()
                .recipient("test@example.com")
                .message("Test message")
                .channel(NotificationChannel.EMAIL)
                .build();

        NotificationResult expectedResult = NotificationResult.successSent();

        when(notificationInputPort.sendNotification(notification)).thenReturn(expectedResult);

        ResponseEntity<NotificationResult> response = notificationAPI.sendNotification(notification);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedResult, response.getBody());
        verify(notificationInputPort).sendNotification(notification);
    }

    @Test
    @DisplayName("Should get notification status successfully")
    void shouldGetNotificationStatusSuccessfully() {
        Long notificationId = 1L;
        NotificationStatus expectedStatus = NotificationStatus.SENT;

        when(notificationStatusInputPort.getNotificationStatus(notificationId)).thenReturn(expectedStatus);

        ResponseEntity<NotificationStatus> response = notificationAPI.getNotificationStatus(notificationId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals(expectedStatus, response.getBody());
        verify(notificationStatusInputPort).getNotificationStatus(notificationId);
    }

    @Test
    @DisplayName("Should handle notification send exception")
    void shouldHandleNotificationSendException() {
        Notification notification = Notification.builder()
                .recipient("test@example.com")
                .message("Test message")
                .channel(NotificationChannel.EMAIL)
                .build();

        when(notificationInputPort.sendNotification(notification))
                .thenThrow(new NotificationSendException("Email service unavailable"));

        assertThrows(NotificationSendException.class, () -> {
            notificationAPI.sendNotification(notification);
        });

        verify(notificationInputPort).sendNotification(notification);
    }

    @Test
    @DisplayName("Should handle notification not found exception")
    void shouldHandleNotificationNotFoundException() {
        Long notificationId = 999L;

        when(notificationStatusInputPort.getNotificationStatus(notificationId))
                .thenThrow(new NotificationNotFoundException(notificationId));

        assertThrows(NotificationNotFoundException.class, () -> {
            notificationAPI.getNotificationStatus(notificationId);
        });

        verify(notificationStatusInputPort).getNotificationStatus(notificationId);
    }

    @Test
    @DisplayName("Should handle different notification channels")
    void shouldHandleDifferentNotificationChannels() {
        // Test SMS
        Notification smsNotification = Notification.builder()
                .recipient("+1234567890")
                .message("SMS test")
                .channel(NotificationChannel.SMS)
                .build();

        when(notificationInputPort.sendNotification(smsNotification))
                .thenReturn(NotificationResult.successSent());

        ResponseEntity<NotificationResult> smsResponse = notificationAPI.sendNotification(smsNotification);
        assertTrue(smsResponse.getBody().isSuccess());

        // Test Push
        Notification pushNotification = Notification.builder()
                .recipient("fcm-token-123")
                .message("Push test")
                .channel(NotificationChannel.PUSH)
                .build();

        when(notificationInputPort.sendNotification(pushNotification))
                .thenReturn(NotificationResult.successSent());

        ResponseEntity<NotificationResult> pushResponse = notificationAPI.sendNotification(pushNotification);
        assertTrue(pushResponse.getBody().isSuccess());
    }
} 