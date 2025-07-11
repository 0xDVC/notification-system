package com.dvc.notifications.adapter.input.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.model.NotificationResult;
import com.dvc.notifications.domain.model.NotificationStatus;
import com.dvc.notifications.domain.port.input.NotificationInputPort;
import com.dvc.notifications.domain.port.input.NotificationStatusInputPort;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/notifications")
@Tag(name = "Notification API v1", description = "Notification service endpoints for sending and querying notifications")
public class NotificationAPI {
    
    private final NotificationInputPort notificationInputPort;
    private final NotificationStatusInputPort notificationStatusQueryPort;

    @Autowired
    public NotificationAPI(NotificationInputPort notificationInputPort, 
                           NotificationStatusInputPort notificationStatusQueryPort) {
        this.notificationInputPort = notificationInputPort;
        this.notificationStatusQueryPort = notificationStatusQueryPort;
    }

    @PostMapping("/send")
    @Operation(
        summary = "Send notification",
        description = "Send a notification via email, SMS, or push notification",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Notification details",
            required = true,
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Notification.class),
                examples = {
                    @ExampleObject(
                        name = "Email notification",
                        value = """
                        {
                            "recipient": "user@example.com",
                            "message": "Welcome to our service!",
                            "channel": "EMAIL"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "SMS notification",
                        value = """
                        {
                            "recipient": "+1234567890",
                            "message": "Your OTP is 123456",
                            "channel": "SMS"
                        }
                        """
                    ),
                    @ExampleObject(
                        name = "Push notification",
                        value = """
                        {
                            "recipient": "fcm-token-123",
                            "message": "You have a new message",
                            "channel": "PUSH"
                        }
                        """
                    )
                }
            )
        )
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Notification sent successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = NotificationResult.class),
                examples = {
                    @ExampleObject(
                        name = "Success response",
                        value = """
                        {
                            "success": true,
                            "status": "SENT",
                            "messageId": "msg-123456",
                            "errorMessage": null
                        }
                        """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid notification data",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Validation error",
                        value = """
                        {
                            "timestamp": "2024-01-01T12:00:00Z",
                            "status": 400,
                            "error": "Bad Request",
                            "message": "Recipient cannot be null or empty"
                        }
                        """
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "500",
            description = "Internal server error",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Server error",
                        value = """
                        {
                            "timestamp": "2024-01-01T12:00:00Z",
                            "status": 500,
                            "error": "Internal Server Error",
                            "message": "Failed to send notification"
                        }
                        """
                    )
                }
            )
        )
    })
    public ResponseEntity<NotificationResult> sendNotification(
            @Valid @RequestBody Notification notification) {
        NotificationResult result = notificationInputPort.sendNotification(notification);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}/status")
    @Operation(
        summary = "Get notification status",
        description = "Retrieve the status of a notification by its ID"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Notification status retrieved successfully",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = NotificationStatus.class),
                examples = {
                    @ExampleObject(
                        name = "Status response",
                        value = "SENT"
                    )
                }
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Notification not found",
            content = @Content(
                mediaType = "application/json",
                examples = {
                    @ExampleObject(
                        name = "Not found",
                        value = """
                        {
                            "timestamp": "2024-01-01T12:00:00Z",
                            "status": 404,
                            "error": "Not Found",
                            "message": "Notification not found with id: 999"
                        }
                        """
                    )
                }
            )
        )
    })
    public ResponseEntity<NotificationStatus> getNotificationStatus(
            @Parameter(description = "Notification ID", example = "1")
            @PathVariable Long id) {
        NotificationStatus status = notificationStatusQueryPort.getNotificationStatus(id);
        return ResponseEntity.ok(status);
    }

    @GetMapping("/health")
    @Operation(
        summary = "Health check",
        description = "Check if the notification service is healthy"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Service is healthy",
        content = @Content(
            mediaType = "application/json",
            examples = {
                @ExampleObject(
                    name = "Health response",
                    value = """
                    {
                        "status": "UP",
                        "timestamp": "2024-01-01T12:00:00Z"
                    }
                    """
                )
            }
        )
    )
    public ResponseEntity<Object> health() {
        return ResponseEntity.ok(new Object() {
            public final String status = "UP";
            public final String timestamp = java.time.Instant.now().toString();
        });
    }
} 