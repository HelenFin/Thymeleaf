package com.example;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.ITemplateResolver;
import org.thymeleaf.templatemode.TemplateMode;

import jakarta.servlet.ServletContext;

public class ThymeleafConfig {

    private TemplateEngine templateEngine;

    public ThymeleafConfig(ServletContext servletContext) {
        this.templateEngine = new TemplateEngine();
        this.templateEngine.setTemplateResolver(templateResolver());
    }

    private ITemplateResolver templateResolver() {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/templates/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(TemplateMode.HTML);
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        return templateResolver;
    }

    public TemplateEngine getTemplateEngine() {
        return this.templateEngine;
    }
}
