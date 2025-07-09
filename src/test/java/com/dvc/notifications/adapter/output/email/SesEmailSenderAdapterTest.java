package com.dvc.notifications.adapter.output.email;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.dvc.notifications.adapter.output.templating.EmailTemplateService;
import com.dvc.notifications.domain.exception.NotificationSendException;
import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.model.NotificationChannel;
import com.dvc.notifications.domain.model.NotificationResult;
import com.dvc.notifications.domain.model.NotificationStatus;

@ExtendWith(MockitoExtension.class)
@DisplayName("SesEmailSenderAdapter Tests")
class SesEmailSenderAdapterTest {

    @Mock
    private AmazonSimpleEmailService sesClient;

    @Mock
    private EmailTemplateService templateService;

    private SesEmailSenderAdapter sesEmailSenderAdapter;

    @BeforeEach
    void setUp() {
        sesEmailSenderAdapter = new SesEmailSenderAdapter(sesClient, templateService);
        ReflectionTestUtils.setField(sesEmailSenderAdapter, "fromAddress", "noreply@example.com");
    }

    @Test
    @DisplayName("Should send email successfully")
    void shouldSendEmailSuccessfully() {
        Notification notification = Notification.builder()
                .recipient("test@example.com")
                .message("Test email message")
                .channel(NotificationChannel.EMAIL)
                .build();

        when(templateService.renderEmailTemplate(notification)).thenReturn("<html>Test template</html>");

        SendEmailResult sendEmailResult = new SendEmailResult();
        sendEmailResult.setMessageId("test-message-id");

        when(sesClient.sendEmail(any(SendEmailRequest.class))).thenReturn(sendEmailResult);

        NotificationResult result = sesEmailSenderAdapter.sendEmail(notification);

        assertTrue(result.isSuccess());
        assertEquals(NotificationStatus.SENT, result.getStatus());
        assertNull(result.getErrorMessage());

        verify(sesClient).sendEmail(any(SendEmailRequest.class));
        verify(templateService).renderEmailTemplate(notification);
    }

    @Test
    @DisplayName("Should handle email send failures")
    void shouldHandleEmailSendFailures() {
        Notification notification = Notification.builder()
                .recipient("test@example.com")
                .message("Test email message")
                .channel(NotificationChannel.EMAIL)
                .build();

        when(templateService.renderEmailTemplate(notification)).thenReturn("<html>Test template</html>");
        when(sesClient.sendEmail(any(SendEmailRequest.class)))
                .thenThrow(new RuntimeException("SES service unavailable"));

        assertThrows(NotificationSendException.class, () -> {
            sesEmailSenderAdapter.sendEmail(notification);
        });

        verify(sesClient).sendEmail(any(SendEmailRequest.class));
        verify(templateService).renderEmailTemplate(notification);
    }
} 