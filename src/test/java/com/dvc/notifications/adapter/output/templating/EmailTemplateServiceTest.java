package com.dvc.notifications.adapter.output.templating;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.model.NotificationChannel;

@ExtendWith(MockitoExtension.class)
@DisplayName("EmailTemplateService Tests")
class EmailTemplateServiceTest {

    @Mock
    private ThymeleafTemplateEngineAdapter templateEngine;

    private EmailTemplateService emailTemplateService;

    @BeforeEach
    void setUp() {
        emailTemplateService = new EmailTemplateService(templateEngine);
    }

    @Test
    @DisplayName("Should render OTP template for OTP messages")
    void shouldRenderOtpTemplateForOtpMessages() {
        Notification notification = Notification.builder()
                .recipient("user@example.com")
                .message("Your OTP is 123456")
                .channel(NotificationChannel.EMAIL)
                .build();

        when(templateEngine.render(eq("email/otp"), any())).thenReturn("<html>OTP Template</html>");

        String result = emailTemplateService.renderEmailTemplate(notification);

        assertNotNull(result);
        assertTrue(result.contains("OTP Template"));
    }

    @Test
    @DisplayName("Should render welcome template for welcome messages")
    void shouldRenderWelcomeTemplateForWelcomeMessages() {
        Notification notification = Notification.builder()
                .recipient("user@example.com")
                .message("Welcome to our platform")
                .channel(NotificationChannel.EMAIL)
                .build();

        when(templateEngine.render(eq("email/welcome"), any())).thenReturn("<html>Welcome Template</html>");

        String result = emailTemplateService.renderEmailTemplate(notification);

        assertNotNull(result);
        assertTrue(result.contains("Welcome Template"));
    }

    @Test
    @DisplayName("Should render alert template for alert messages")
    void shouldRenderAlertTemplateForAlertMessages() {
        Notification notification = Notification.builder()
                .recipient("user@example.com")
                .message("Security alert detected")
                .channel(NotificationChannel.EMAIL)
                .build();

        when(templateEngine.render(eq("email/alert"), any())).thenReturn("<html>Alert Template</html>");

        String result = emailTemplateService.renderEmailTemplate(notification);

        assertNotNull(result);
        assertTrue(result.contains("Alert Template"));
    }

    @Test
    @DisplayName("Should render general template for other messages")
    void shouldRenderGeneralTemplateForOtherMessages() {
        Notification notification = Notification.builder()
                .recipient("user@example.com")
                .message("Regular notification message")
                .channel(NotificationChannel.EMAIL)
                .build();

        when(templateEngine.render(eq("email/general"), any())).thenReturn("<html>General Template</html>");

        String result = emailTemplateService.renderEmailTemplate(notification);

        assertNotNull(result);
        assertTrue(result.contains("General Template"));
    }
} 