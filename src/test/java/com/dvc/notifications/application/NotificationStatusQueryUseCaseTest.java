package com.dvc.notifications.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dvc.notifications.domain.exception.NotificationNotFoundException;
import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.model.NotificationChannel;
import com.dvc.notifications.domain.model.NotificationStatus;
import com.dvc.notifications.domain.port.output.NotificationRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificationStatusQueryUseCase Tests")
class NotificationStatusQueryUseCaseTest {

    @Mock
    private NotificationRepositoryPort notificationRepository;

    private NotificationStatusQueryUseCase notificationStatusQueryUseCase;

    @BeforeEach
    void init() {
        notificationStatusQueryUseCase = new NotificationStatusQueryUseCase(notificationRepository);
    }

    @Test
    @DisplayName("Should return notification status when notification exists")
    void shouldReturnNotificationStatusWhenNotificationExists() {
        Long notificationId = 1L;
        Notification notification = Notification.builder()
                .id(notificationId)
                .recipient("test@example.com")
                .channel(NotificationChannel.EMAIL)
                .message("Test message")
                .status(NotificationStatus.SENT)
                .build();

        when(notificationRepository.findById(notificationId)).thenReturn(notification);

        NotificationStatus status = notificationStatusQueryUseCase.getNotificationStatus(notificationId);

        assertEquals(NotificationStatus.SENT, status);
        verify(notificationRepository).findById(notificationId);
    }

    @Test
    @DisplayName("Should throw NotificationNotFoundException when notification not found")
    void shouldThrowNotificationNotFoundExceptionWhenNotificationNotFound() {
        Long notificationId = 999L;

        when(notificationRepository.findById(notificationId)).thenReturn(null);

        NotificationNotFoundException exception = assertThrows(NotificationNotFoundException.class, () -> {
            notificationStatusQueryUseCase.getNotificationStatus(notificationId);
        });
        
        assertEquals("Notification not found with id: " + notificationId, exception.getMessage());
        verify(notificationRepository).findById(notificationId);
    }

    @Test
    @DisplayName("Should handle different notification statuses")
    void shouldHandleDifferentNotificationStatuses() {
        Long notificationId = 1L;
        Notification notification = Notification.builder()
                .id(notificationId)
                .recipient("test@example.com")
                .channel(NotificationChannel.SMS)
                .message("Test message")
                .status(NotificationStatus.FAILED)
                .build();

        when(notificationRepository.findById(notificationId)).thenReturn(notification);

        NotificationStatus status = notificationStatusQueryUseCase.getNotificationStatus(notificationId);

        assertEquals(NotificationStatus.FAILED, status);
    }

    @Test
    @DisplayName("Should handle queued notification status")
    void shouldHandleQueuedNotificationStatus() {
        Long notificationId = 1L;
        Notification notification = Notification.builder()
                .id(notificationId)
                .recipient("test@example.com")
                .channel(NotificationChannel.PUSH)
                .message("Test message")
                .status(NotificationStatus.QUEUED)
                .build();

        when(notificationRepository.findById(notificationId)).thenReturn(notification);

        NotificationStatus status = notificationStatusQueryUseCase.getNotificationStatus(notificationId);

        assertEquals(NotificationStatus.QUEUED, status);
    }
} 