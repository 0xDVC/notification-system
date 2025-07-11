package com.dvc.notifications.adapter.output.push;

import org.springframework.beans.factory.annotation.Autowired;

import com.dvc.notifications.domain.exception.NotificationSendException;
import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.model.NotificationResult;
import com.dvc.notifications.domain.port.output.PushNotificationSenderPort;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.ApnsConfig;
import com.google.firebase.messaging.WebpushConfig;

public class FcmPushNotificationSenderAdapter implements PushNotificationSenderPort {
    private final FirebaseMessaging firebaseMessaging;

    @Autowired
    public FcmPushNotificationSenderAdapter(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    @Override
    public NotificationResult sendPush(Notification notification) {
        if (firebaseMessaging == null) {
            throw new NotificationSendException("Firebase Messaging is not available");
        }
        
        try {
            String recipient = notification.getRecipient();
            Message.Builder messageBuilder = Message.builder();
            
            if (recipient.startsWith("/topics/")) {
                messageBuilder.setTopic(recipient.substring(8)); // Remove "/topics/" prefix
            } else {
                messageBuilder.setToken(recipient);
            }

            com.google.firebase.messaging.Notification.Builder notificationBuilder = 
                com.google.firebase.messaging.Notification.builder()
                    .setTitle("Notification") 
                    .setBody(notification.getMessage());

            messageBuilder.setNotification(notificationBuilder.build());

            messageBuilder.putData("message", notification.getMessage());
            messageBuilder.putData("channel", notification.getChannel().toString());

            AndroidConfig androidConfig = AndroidConfig.builder()
                    .setPriority(AndroidConfig.Priority.HIGH)
                    .putData("click_action", "FLUTTER_NOTIFICATION_CLICK")
                    .build();
            messageBuilder.setAndroidConfig(androidConfig);

            ApnsConfig apnsConfig = ApnsConfig.builder()
                    .putHeader("apns-priority", "10")
                    .putHeader("apns-push-type", "alert")
                    .setAps(com.google.firebase.messaging.Aps.builder()
                        .setAlert(notification.getMessage())
                        .setSound("default")
                        .build())
                    .build();
            messageBuilder.setApnsConfig(apnsConfig);

            WebpushConfig webpushConfig = WebpushConfig.builder()
                    .putHeader("Urgency", "high")
                    .build();
            messageBuilder.setWebpushConfig(webpushConfig);

            Message message = messageBuilder.build();
            String response = firebaseMessaging.send(message);
            
            System.out.println("Push notification sent successfully: " + response);
            return NotificationResult.successSent();
            
        } catch (FirebaseMessagingException e) {
            String errorMessage = "Push notification send failed: " + e.getMessage();
            System.err.println(errorMessage);
            
            var errorCode = e.getMessagingErrorCode();
            if (errorCode == null) {
                throw new NotificationSendException(errorMessage, e);
            }
            switch (errorCode) {
                case INVALID_ARGUMENT -> throw new NotificationSendException("Invalid FCM token or topic: " + errorMessage, e);
                case UNREGISTERED -> throw new NotificationSendException("FCM token is unregistered: " + errorMessage, e);
                case SENDER_ID_MISMATCH -> throw new NotificationSendException("Sender ID mismatch: " + errorMessage, e);
                case QUOTA_EXCEEDED -> throw new NotificationSendException("FCM quota exceeded: " + errorMessage, e);
                case UNAVAILABLE -> throw new NotificationSendException("FCM service unavailable: " + errorMessage, e);
                case INTERNAL -> throw new NotificationSendException("FCM internal error: " + errorMessage, e);
                default -> throw new NotificationSendException(errorMessage, e);
            }
        } catch (IllegalArgumentException e) {
            String errorMessage = "Invalid FCM message configuration: " + e.getMessage();
            System.err.println(errorMessage);
            throw new NotificationSendException(errorMessage, e);
        }
    }
} 