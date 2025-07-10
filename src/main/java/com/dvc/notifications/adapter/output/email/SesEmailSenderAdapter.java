package com.dvc.notifications.adapter.output.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendEmailResult;
import com.dvc.notifications.adapter.output.templating.EmailTemplateService;
import com.dvc.notifications.domain.exception.NotificationSendException;
import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.model.NotificationResult;
import com.dvc.notifications.domain.port.output.EmailSenderPort;

// @Component
public class SesEmailSenderAdapter implements EmailSenderPort {
    private final AmazonSimpleEmailService sesClient;
    private final EmailTemplateService templateService;
    
    @Value("${aws.ses.from-address}")
    private String fromAddress;

    @Autowired
    public SesEmailSenderAdapter(AmazonSimpleEmailService sesClient, EmailTemplateService templateService) {
        this.sesClient = sesClient;
        this.templateService = templateService;
    }

    @Override
    public NotificationResult sendEmail(Notification notification) {
        try {
            // Render HTML template
            String htmlContent = templateService.renderEmailTemplate(notification);
            
            // Determine subject based on notification type
            String subject = determineEmailSubject(notification);
            
            SendEmailRequest request = new SendEmailRequest()
                    .withSource(fromAddress)
                    .withDestination(new Destination().withToAddresses(notification.getRecipient()))
                    .withMessage(new Message()
                            .withSubject(new Content().withData(subject))
                            .withBody(new Body()
                                    .withHtml(new Content().withData(htmlContent))
                                    .withText(new Content().withData(notification.getMessage()))));

            SendEmailResult result = sesClient.sendEmail(request);
            System.out.println("Email sent successfully: " + result.getMessageId());
            return NotificationResult.successSent();
        } catch (Exception e) {
            System.err.println("Email send failed: " + e.getMessage());
            throw new NotificationSendException("Email send failed: " + e.getMessage(), e);
        }
    }
    
    private String determineEmailSubject(Notification notification) {
        String message = notification.getMessage().toLowerCase();
        
        if (message.contains("otp")) {
            return "🔐 Your OTP Code";
        } else if (message.contains("welcome")) {
            return "🎉 Welcome to Our Platform!";
        } else if (message.contains("alert")) {
            return "⚠️ Important Alert";
        } else {
            return "📧 New Notification";
        }
    }
} 