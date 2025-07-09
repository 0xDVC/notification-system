package com.dvc.notifications.adapter.output.templating;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.dvc.notifications.domain.port.output.TemplateEnginePort;

@Component
public class ThymeleafTemplateEngineAdapter implements TemplateEnginePort {
    private final TemplateEngine templateEngine;

    @Autowired
    public ThymeleafTemplateEngineAdapter(TemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    @Override
    public String render(String templateName, Object contextObj) {
        Context context = new Context();
        if (contextObj instanceof java.util.Map<?, ?> map) {
            map.forEach((k, v) -> context.setVariable(String.valueOf(k), v));
        } else {
            context.setVariable("data", contextObj);
        }
        return templateEngine.process(templateName, context);
    }
} 