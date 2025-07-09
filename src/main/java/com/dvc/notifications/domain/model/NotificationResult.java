package com.dvc.notifications.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationResult {
    private boolean success;
    private NotificationStatus status;
    private String errorMessage;

    public static NotificationResult successSent() {
        return NotificationResult.builder()
                .success(true)
                .status(NotificationStatus.SENT)
                .build();
    }

    public static NotificationResult failure(String errorMessage) {
        return NotificationResult.builder()
                .success(false)
                .status(NotificationStatus.FAILED)
                .errorMessage(errorMessage)
                .build();
    }
} 