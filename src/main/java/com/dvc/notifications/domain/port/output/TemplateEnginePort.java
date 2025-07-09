package com.dvc.notifications.domain.port.output;

public interface TemplateEnginePort {
    String render(String templateName, Object context);
} 