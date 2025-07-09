package com.dvc.notifications.adapter.output.sms;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.dvc.notifications.domain.exception.NotificationSendException;
import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.model.NotificationChannel;
import com.dvc.notifications.domain.model.NotificationResult;
import com.dvc.notifications.domain.model.NotificationStatus;

@ExtendWith(MockitoExtension.class)
@DisplayName("MNotifySmsSenderAdapter Tests")
class MNotifySmsSenderAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    private MNotifySmsSenderAdapter mNotifySmsSenderAdapter;

    @BeforeEach
    void setUp() {
        mNotifySmsSenderAdapter = new MNotifySmsSenderAdapter(restTemplate);
        ReflectionTestUtils.setField(mNotifySmsSenderAdapter, "apiKey", "test-api-key");
        ReflectionTestUtils.setField(mNotifySmsSenderAdapter, "apiUrl", "https://api.mnotify.com/api");
        ReflectionTestUtils.setField(mNotifySmsSenderAdapter, "sender", "TestSender");
    }

    @Test
    @DisplayName("Should send SMS successfully")
    void shouldSendSmsSuccessfully() {
        Notification notification = Notification.builder()
                .recipient("+1234567890")
                .message("Test SMS message")
                .channel(NotificationChannel.SMS)
                .build();

        Map<String, Object> successResponse = new HashMap<>();
        successResponse.put("status", "success");
        successResponse.put("message", "messages sent successfully");

        when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(successResponse);

        NotificationResult result = mNotifySmsSenderAdapter.sendSms(notification);

        assertTrue(result.isSuccess());
        assertEquals(NotificationStatus.SENT, result.getStatus());
        assertNull(result.getErrorMessage());

        verify(restTemplate).postForObject(
                eq("https://api.mnotify.com/api/sms/quick?key=test-api-key"),
                any(HttpEntity.class),
                eq(Map.class)
        );
    }

    @Test
    @DisplayName("Should handle SMS failures")
    void shouldHandleSmsFailures() {
        Notification notification = Notification.builder()
                .recipient("+1234567890")
                .message("Test SMS message")
                .channel(NotificationChannel.SMS)
                .build();

        // Test API error response
        Map<String, Object> failureResponse = new HashMap<>();
        failureResponse.put("status", "error");
        failureResponse.put("message", "Invalid API key");

        when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), eq(Map.class)))
                .thenReturn(failureResponse);

        assertThrows(NotificationSendException.class, () -> mNotifySmsSenderAdapter.sendSms(notification));

        // Test network exception
        when(restTemplate.postForObject(any(String.class), any(HttpEntity.class), eq(Map.class)))
                .thenThrow(new RestClientException("Network error"));

        assertThrows(NotificationSendException.class, () -> mNotifySmsSenderAdapter.sendSms(notification));
    }
} 