package com.dvc.notifications.application;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dvc.notifications.domain.exception.NotificationSendException;
import com.dvc.notifications.domain.exception.NotificationValidationException;
import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.model.NotificationChannel;
import com.dvc.notifications.domain.model.NotificationResult;
import com.dvc.notifications.domain.model.NotificationStatus;
import com.dvc.notifications.domain.port.output.EmailSenderPort;
import com.dvc.notifications.domain.port.output.NotificationRepositoryPort;
import com.dvc.notifications.domain.port.output.PushNotificationSenderPort;
import com.dvc.notifications.domain.port.output.SmsSenderPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("SendNotificationUseCase Tests")
class SendNotificationUseCaseTest {

    @Mock
    private NotificationRepositoryPort notificationRepository;

    @Mock
    private EmailSenderPort emailSender;

    @Mock
    private SmsSenderPort smsSender;

    @Mock
    private PushNotificationSenderPort pushSender;

    private SendNotificationUseCase sendNotificationUseCase;

    @BeforeEach
    void init() {
        sendNotificationUseCase = new SendNotificationUseCase(
                notificationRepository, emailSender, smsSender, pushSender);
    }

    @Test
    @DisplayName("Should validate notification input")
    void shouldValidateNotificationInput() {
        // Test null notification
        NotificationValidationException exception = assertThrows(NotificationValidationException.class, () -> {
            sendNotificationUseCase.sendNotification(null);
        });
        assertEquals("Notification cannot be null", exception.getMessage());

        // Test empty recipient
        Notification notification = Notification.builder()
                .recipient("")
                .message("Test message")
                .channel(NotificationChannel.EMAIL)
                .build();

        exception = assertThrows(NotificationValidationException.class, () -> {
            sendNotificationUseCase.sendNotification(notification);
        });
        assertEquals("Recipient cannot be null or empty", exception.getMessage());
    }

    @Test
    @DisplayName("Should send email notification successfully")
    void shouldSendEmailNotificationSuccessfully() {
        Notification notification = Notification.builder()
                .recipient("test@example.com")
                .message("Test message")
                .channel(NotificationChannel.EMAIL)
                .build();

        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        when(emailSender.sendEmail(any(Notification.class)))
                .thenReturn(NotificationResult.successSent());

        NotificationResult result = sendNotificationUseCase.sendNotification(notification);

        assertTrue(result.isSuccess());
        assertEquals(NotificationStatus.SENT, result.getStatus());
        verify(notificationRepository, times(2)).save(any(Notification.class));
        verify(emailSender).sendEmail(notification);
    }

    @Test
    @DisplayName("Should send SMS notification successfully")
    void shouldSendSmsNotificationSuccessfully() {
        Notification notification = Notification.builder()
                .recipient("+1234567890")
                .message("Test message")
                .channel(NotificationChannel.SMS)
                .build();

        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        when(smsSender.sendSms(any(Notification.class)))
                .thenReturn(NotificationResult.successSent());

        NotificationResult result = sendNotificationUseCase.sendNotification(notification);

        assertTrue(result.isSuccess());
        assertEquals(NotificationStatus.SENT, result.getStatus());
        verify(smsSender).sendSms(notification);
    }

    @Test
    @DisplayName("Should fallback to SMS when email fails for OTP message")
    void shouldFallbackToSmsWhenEmailFailsForOtpMessage() {
        Notification notification = Notification.builder()
                .recipient("test@example.com")
                .message("OTP")
                .channel(NotificationChannel.EMAIL)
                .build();

        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        when(emailSender.sendEmail(any(Notification.class)))
                .thenReturn(NotificationResult.failure("Email failed"));
        when(smsSender.sendSms(any(Notification.class)))
                .thenReturn(NotificationResult.successSent());

        NotificationResult result = sendNotificationUseCase.sendNotification(notification);

        assertTrue(result.isSuccess());
        assertEquals(NotificationStatus.SENT, result.getStatus());
        verify(emailSender).sendEmail(notification);
        verify(smsSender).sendSms(notification);
    }

    @Test
    @DisplayName("Should throw exception when sending fails")
    void shouldThrowExceptionWhenSendingFails() {
        Notification notification = Notification.builder()
                .recipient("test@example.com")
                .message("Test message")
                .channel(NotificationChannel.EMAIL)
                .build();

        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);
        when(emailSender.sendEmail(any(Notification.class)))
                .thenReturn(NotificationResult.failure("Email service unavailable"));

        assertThrows(NotificationSendException.class, () -> {
            sendNotificationUseCase.sendNotification(notification);
        });
    }
} 