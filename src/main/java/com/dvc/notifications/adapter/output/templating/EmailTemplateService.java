package com.dvc.notifications.adapter.output.templating;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dvc.notifications.domain.model.Notification;

@Service
public class EmailTemplateService {
    
    private final ThymeleafTemplateEngineAdapter templateEngine;
    
    @Autowired
    public EmailTemplateService(ThymeleafTemplateEngineAdapter templateEngine) {
        this.templateEngine = templateEngine;
    }
    
    public String renderEmailTemplate(Notification notification) {
        String templateName = determineTemplateName(notification);
        Map<String, Object> context = buildTemplateContext(notification);
        return templateEngine.render(templateName, context);
    }
    
    private String determineTemplateName(Notification notification) {
        String message = notification.getMessage().toLowerCase();
        
        if (message.contains("otp") || message.contains("verification")) {
            return "email/otp";
        } else if (message.contains("welcome") || message.contains("account")) {
            return "email/welcome";
        } else if (message.contains("alert") || message.contains("security")) {
            return "email/alert";
        } else {
            return "email/general";
        }
    }
    
    private Map<String, Object> buildTemplateContext(Notification notification) {
        Map<String, Object> context = new HashMap<>();
        
        // Basic notification data
        context.put("recipientName", extractRecipientName(notification.getRecipient()));
        context.put("notificationMessage", notification.getMessage());
        context.put("notificationType", notification.getChannel().toString());
        
        // Template-specific data
        String message = notification.getMessage().toLowerCase();
        
        if (message.contains("otp")) {
            context.put("otpCode", generateOTP());
            context.put("expiryMinutes", 10);
        } else if (message.contains("welcome")) {
            context.put("recipientName", extractRecipientName(notification.getRecipient()));
        } else if (message.contains("alert")) {
            context.put("alertType", determineAlertType(message));
            context.put("alertMessage", notification.getMessage());
            context.put("priority", "High");
        } else {
            context.put("notificationTitle", "New Notification");
            context.put("notificationMessage", notification.getMessage());
            context.put("senderName", "System");
        }
        
        return context;
    }
    
    private String extractRecipientName(String recipient) {
        if (recipient.contains("@")) {
            // Email address - extract username
            String username = recipient.substring(0, recipient.indexOf("@"));
            return username.substring(0, 1).toUpperCase() + username.substring(1);
        }
        return "there";
    }
    
    private String generateOTP() {
        // Generate a 6-digit OTP
        return String.format("%06d", (int) (Math.random() * 1000000));
    }
    
    private String determineAlertType(String message) {
        if (message.contains("security")) {
            return "security";
        } else if (message.contains("system") || message.contains("update")) {
            return "system";
        } else {
            return "general";
        }
    }
} 