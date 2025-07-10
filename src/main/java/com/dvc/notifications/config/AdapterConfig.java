package com.dvc.notifications.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;

import java.io.IOException;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.dvc.notifications.adapter.output.email.SesEmailSenderAdapter;
import com.dvc.notifications.adapter.output.persistence.PostgresNotificationRepositoryAdapter;
import com.dvc.notifications.adapter.output.persistence.PostgresUserPreferencesRepositoryAdapter;
import com.dvc.notifications.adapter.output.persistence.jpa.NotificationJpaRepository;
import com.dvc.notifications.adapter.output.persistence.jpa.UserPreferencesJpaRepository;
import com.dvc.notifications.adapter.output.push.FcmPushNotificationSenderAdapter;
import com.dvc.notifications.adapter.output.templating.EmailTemplateService;
import com.dvc.notifications.adapter.output.templating.ThymeleafTemplateEngineAdapter;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;

@Configuration
@Profile("prod")
public class AdapterConfig {
    
    @Value("${aws.ses.access-key}")
    private String awsAccessKey;
    
    @Value("${aws.ses.secret-key}")
    private String awsSecretKey;
    
    @Value("${aws.ses.region}")
    private String awsRegion;

    @Bean
    public AmazonSimpleEmailService amazonSimpleEmailService() {
        BasicAWSCredentials credentials = new BasicAWSCredentials(awsAccessKey, awsSecretKey);
        return AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(awsRegion)
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .build();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public FirebaseMessaging firebaseMessaging() throws Exception {
        if (System.getProperty("spring.profiles.active", "").contains("dev")) {
            return null; 
        }
        
        if (FirebaseApp.getApps().isEmpty()) {
            try {
                GoogleCredentials credentials = GoogleCredentials.getApplicationDefault();
                FirebaseOptions options = FirebaseOptions.builder()
                        .setCredentials(credentials)
                        .build();
                FirebaseApp.initializeApp(options);
            } catch (IOException | SecurityException e) {
                System.out.println("Firebase credentials not available, skipping FCM initialization");
                return null;
            }
        }
        return FirebaseMessaging.getInstance();
    }

    @Bean
    public PostgresNotificationRepositoryAdapter postgresNotificationRepositoryAdapter(NotificationJpaRepository repo) {
        return new PostgresNotificationRepositoryAdapter(repo);
    }

    @Bean
    public PostgresUserPreferencesRepositoryAdapter postgresUserPreferencesRepositoryAdapter(UserPreferencesJpaRepository repo) {
        return new PostgresUserPreferencesRepositoryAdapter(repo);
    }

    @Bean
    public SesEmailSenderAdapter sesEmailSenderAdapter(AmazonSimpleEmailService sesClient, EmailTemplateService emailTemplateService) {
        return new SesEmailSenderAdapter(sesClient, emailTemplateService);
    }

    @Bean
    @Conditional(FirebaseMessagingCondition.class)
    public FcmPushNotificationSenderAdapter fcmPushNotificationSenderAdapter() {
        // This will only be called if the condition is met (FirebaseMessaging is available)
        try {
            FirebaseMessaging firebaseMessaging = firebaseMessaging();
            if (firebaseMessaging == null) {
                return null;
            }
            return new FcmPushNotificationSenderAdapter(firebaseMessaging);
        } catch (Exception e) {
            System.out.println("Failed to create FCM adapter: " + e.getMessage());
            return null;
        }
    }

    @Bean
    public ThymeleafTemplateEngineAdapter thymeleafTemplateEngineAdapter(TemplateEngine templateEngine) {
        return new ThymeleafTemplateEngineAdapter(templateEngine);
    }
} 