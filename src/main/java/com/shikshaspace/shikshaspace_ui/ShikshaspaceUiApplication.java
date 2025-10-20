package com.shikshaspace.shikshaspace_ui;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.server.AppShellSettings;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Production-grade ShikshaSpace UI Application
 * Modern responsive UI with premium design
 *
 * @author ShikshaSpace Engineering Team
 * @version 2.0 (Production Ready)
 */
@SpringBootApplication
@Theme(value = "shikshaspace")  // ✅ REQUIRED: Use custom theme
public class ShikshaspaceUiApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(ShikshaspaceUiApplication.class, args);
    }

    /**
     * Configure application shell for responsive design
     */
    @Override
    public void configurePage(AppShellSettings settings) {
        // ✅ ESSENTIAL: Responsive viewport for mobile
        settings.setViewport("width=device-width, initial-scale=1.0, maximum-scale=5.0, user-scalable=yes");

        // ✅ ESSENTIAL: Page title
        settings.setPageTitle("ShikshaSpace");

        // ✅ RECOMMENDED: SEO meta tags
        settings.addMetaTag("description", "ShikshaSpace - Modern Learning Platform");
        settings.addMetaTag("author", "ShikshaSpace Engineering Team");

        // ✨ OPTIONAL: Add these when you have icons
        // settings.addFavIcon("icon", "/icons/favicon.ico", "256x256");
        // settings.addLink("apple-touch-icon", "/icons/apple-touch-icon.png");
    }
}
