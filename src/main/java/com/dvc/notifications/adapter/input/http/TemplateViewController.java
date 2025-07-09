package com.dvc.notifications.adapter.input.http;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.dvc.notifications.domain.model.Notification;
import com.dvc.notifications.domain.model.NotificationChannel;

@Controller
public class TemplateViewController {

    @GetMapping("/templates")
    public String templatesIndex() {
        return "templates/index";
    }

    @GetMapping("/templates/list")
    public String templatesList(Model model) {
        model.addAttribute("templates", new String[]{
            "general", "welcome", "otp", "alert", "base"
        });
        return "templates/list";
    }

    @GetMapping("/templates/email/{templateName}")
    public String previewEmailTemplate(@PathVariable String templateName, Model model) {
        // Create a sample notification for preview
        Notification sampleNotification = Notification.builder()
                .recipient("preview@example.com")
                .message("This is a sample message for template preview")
                .channel(NotificationChannel.EMAIL)
                .build();

        // Add common model attributes
        model.addAttribute("notification", sampleNotification);
        model.addAttribute("title", "Sample Notification");
        model.addAttribute("subject", "Sample Subject");

        // For base template, provide default content
        if ("base".equals(templateName)) {
            model.addAttribute("content", "<p>This is sample content for the base template.</p>");
            return "email/base";
        }

        // For other templates, provide sample content based on template type
        String sampleContent = getSampleContent(templateName);
        model.addAttribute("content", sampleContent);
        return "email/base";
    }

    private String getSampleContent(String templateName) {
        switch (templateName) {
            case "general":
                return """
                    <div class="text-center">
                        <h2 class="text-xl font-semibold text-gray-800 mb-4">General Notification</h2>
                        <p class="text-gray-600 mb-4">This is a sample general notification message.</p>
                        <p class="text-sm text-gray-500">Sent at: <span th:text="${#dates.format(#dates.createNow(), 'dd MMM yyyy HH:mm')}">Date</span></p>
                    </div>
                    """;
            case "welcome":
                return """
                    <div class="text-center">
                        <h2 class="text-xl font-semibold text-green-800 mb-4">Welcome!</h2>
                        <p class="text-gray-600 mb-4">Welcome to our platform! We're excited to have you on board.</p>
                        <div class="bg-green-50 p-4 rounded-lg">
                            <p class="text-green-800">Your account has been successfully created.</p>
                        </div>
                    </div>
                    """;
            case "otp":
                return """
                    <div class="text-center">
                        <h2 class="text-xl font-semibold text-blue-800 mb-4">Verification Code</h2>
                        <p class="text-gray-600 mb-4">Your verification code is:</p>
                        <div class="bg-blue-50 p-6 rounded-lg mb-4">
                            <span class="text-3xl font-bold text-blue-800">123456</span>
                        </div>
                        <p class="text-sm text-gray-500">This code will expire in 10 minutes.</p>
                    </div>
                    """;
            case "alert":
                return """
                    <div class="text-center">
                        <h2 class="text-xl font-semibold text-red-800 mb-4">Security Alert</h2>
                        <div class="bg-red-50 p-4 rounded-lg mb-4">
                            <p class="text-red-800 font-semibold">Important: Security notification</p>
                            <p class="text-red-700">This is a sample alert message.</p>
                        </div>
                        <p class="text-sm text-gray-500">Please review this information carefully.</p>
                    </div>
                    """;
            default:
                return "<p>Template content not available.</p>";
        }
    }
} 