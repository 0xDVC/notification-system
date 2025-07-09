package com.dvc.notifications.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("NotificationResult Domain Model Tests")
class NotificationResultTest {

    @Test
    @DisplayName("Should create success result with factory method")
    void shouldCreateSuccessResultWithFactoryMethod() {
        NotificationResult result = NotificationResult.successSent();

        assertTrue(result.isSuccess());
        assertEquals(NotificationStatus.SENT, result.getStatus());
        assertNull(result.getErrorMessage());
    }

    @Test
    @DisplayName("Should create failure result with factory method")
    void shouldCreateFailureResultWithFactoryMethod() {
        String errorMessage = "Email send failed";
        NotificationResult result = NotificationResult.failure(errorMessage);

        assertFalse(result.isSuccess());
        assertEquals(NotificationStatus.FAILED, result.getStatus());
        assertEquals(errorMessage, result.getErrorMessage());
    }

    @Test
    @DisplayName("Should create notification result with builder")
    void shouldCreateNotificationResultWithBuilder() {
        NotificationResult result = NotificationResult.builder()
                .success(true)
                .status(NotificationStatus.DELIVERED)
                .errorMessage(null)
                .build();

        assertTrue(result.isSuccess());
        assertEquals(NotificationStatus.DELIVERED, result.getStatus());
        assertNull(result.getErrorMessage());
    }

    @Test
    @DisplayName("Should create notification result with no args constructor")
    void shouldCreateNotificationResultWithNoArgsConstructor() {
        NotificationResult result = new NotificationResult();
        assertNotNull(result);
    }

    @Test
    @DisplayName("Should set and get notification result properties")
    void shouldSetAndGetNotificationResultProperties() {
        NotificationResult result = new NotificationResult();
        String errorMessage = "Test error";

        result.setSuccess(false);
        result.setStatus(NotificationStatus.FAILED);
        result.setErrorMessage(errorMessage);

        assertFalse(result.isSuccess());
        assertEquals(NotificationStatus.FAILED, result.getStatus());
        assertEquals(errorMessage, result.getErrorMessage());
    }
} 