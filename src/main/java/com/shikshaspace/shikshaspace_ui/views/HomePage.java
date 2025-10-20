package com.shikshaspace.shikshaspace_ui.views;

import com.shikshaspace.shikshaspace_ui.components.MainLayout;
import com.shikshaspace.shikshaspace_ui.security.SecurityUtils;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;

/**
 * Production-grade responsive HomePage with modern dashboard design
 * Supports mobile (320px+), tablet (768px+), and desktop (1024px+)
 *
 * Features:
 * - Mobile-first responsive dashboard
 * - Modern card-based layout
 * - Premium glassmorphism effects
 * - Smooth animations and transitions
 * - Responsive grid system
 * - Interactive stat cards
 * - Touch-friendly design
 *
 * @author ShikshaSpace Engineering Team
 * @version 2.0 (Responsive + Premium UI)
 */
@Route(value = "", layout = MainLayout.class)
@PageTitle("Home | ShikshaSpace")
@PermitAll
public class HomePage extends VerticalLayout {

    public HomePage() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        addClassName("home-page-container");

        // Responsive background with gradient
        getStyle()
                .set("background", "linear-gradient(180deg, #f8f9ff 0%, #ffffff 100%)")
                .set("min-height", "100vh")
                .set("overflow-y", "auto");

        // Main content container with responsive padding
        VerticalLayout mainContent = createMainContent();

        add(mainContent);
    }

    /**
     * Create main content container with responsive layout
     */
    private VerticalLayout createMainContent() {
        VerticalLayout content = new VerticalLayout();
        content.setWidthFull();
        content.setPadding(true);
        content.setSpacing(true);
        content.addClassName("main-content-wrapper");

        // Responsive container with max-width
        content.getStyle()
                .set("max-width", "1400px")
                .set("margin", "0 auto")
                .set("padding", "clamp(16px, 4vw, 40px)");

        // Welcome header section
        VerticalLayout welcomeSection = createWelcomeSection();

        // Stats cards section
        HorizontalLayout statsSection = createStatsSection();

        // Feature cards section
        Div featureSection = createFeatureSection();

        // Quick actions section
        Div quickActionsSection = createQuickActionsSection();

        content.add(
                welcomeSection,
                statsSection,
                featureSection,
                quickActionsSection
        );

        return content;
    }

    /**
     * Create welcome section with personalized greeting
     */
    private VerticalLayout createWelcomeSection() {
        VerticalLayout welcomeSection = new VerticalLayout();
        welcomeSection.setWidthFull();
        welcomeSection.setPadding(false);
        welcomeSection.setSpacing(false);
        welcomeSection.addClassName("welcome-section");

        welcomeSection.getStyle()
                .set("margin-bottom", "clamp(20px, 4vw, 40px)");

        // Get username from security context
        String username = SecurityUtils.getUsername();

        // Welcome title with gradient text
        H1 welcomeTitle = new H1("Welcome back, " + username + "!");
        welcomeTitle.addClassNames(
                LumoUtility.Margin.NONE,
                LumoUtility.Margin.Bottom.SMALL,
                "welcome-title"
        );
        welcomeTitle.getStyle()
                .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
                .set("-webkit-background-clip", "text")
                .set("-webkit-text-fill-color", "transparent")
                .set("background-clip", "text")
                .set("font-size", "clamp(1.75rem, 5vw, 2.5rem)")
                .set("font-weight", "700")
                .set("letter-spacing", "0.5px")
                .set("animation", "fadeInDown 0.6s ease-out");

        // Subtitle
        Paragraph subtitle = new Paragraph("Explore your learning dashboard and track your progress");
        subtitle.addClassNames(
                LumoUtility.Margin.NONE,
                "welcome-subtitle"
        );
        subtitle.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("font-size", "clamp(0.9rem, 2.5vw, 1.1rem)")
                .set("margin-top", "8px")
                .set("animation", "fadeInUp 0.6s ease-out 0.2s both");

        welcomeSection.add(welcomeTitle, subtitle);

        return welcomeSection;
    }

    /**
     * Create stats cards section with responsive grid
     */
    private HorizontalLayout createStatsSection() {
        HorizontalLayout statsLayout = new HorizontalLayout();
        statsLayout.setWidthFull();
        statsLayout.setSpacing(true);
        statsLayout.addClassName("stats-section");

        // Responsive grid behavior
        statsLayout.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(250px, 1fr))")
                .set("gap", "clamp(16px, 3vw, 24px)")
                .set("margin-bottom", "clamp(24px, 4vw, 40px)");

        // Create stat cards
        Div coursesCard = createStatCard(
                VaadinIcon.BOOK,
                "12",
                "Courses Enrolled",
                "#667eea",
                "0.1s"
        );

        Div achievementsCard = createStatCard(
                VaadinIcon.TROPHY,
                "8",
                "Achievements",
                "#f093fb",
                "0.2s"
        );

        Div progressCard = createStatCard(
                VaadinIcon.CHART,
                "75%",
                "Overall Progress",
                "#4fd1c5",
                "0.3s"
        );

        Div certificatesCard = createStatCard(
                VaadinIcon.DIPLOMA,
                "5",
                "Certificates",
                "#f6ad55",
                "0.4s"
        );

        statsLayout.add(coursesCard, achievementsCard, progressCard, certificatesCard);

        return statsLayout;
    }

    /**
     * Create individual stat card with icon and number
     */
    private Div createStatCard(VaadinIcon iconType, String value, String label, String color, String animationDelay) {
        Div card = new Div();
        card.addClassName("stat-card");

        // Modern card styling
        card.getStyle()
                .set("background", "white")
                .set("border-radius", "clamp(12px, 2vw, 16px)")
                .set("padding", "clamp(20px, 4vw, 28px)")
                .set("box-shadow", "0 4px 12px rgba(102, 126, 234, 0.1)")
                .set("transition", "all 0.3s cubic-bezier(0.4, 0, 0.2, 1)")
                .set("cursor", "pointer")
                .set("position", "relative")
                .set("overflow", "hidden")
                .set("border", "1px solid rgba(102, 126, 234, 0.1)")
                .set("animation", "fadeInUp 0.6s ease-out " + animationDelay + " both");

        // Icon container with background
        Div iconContainer = new Div();
        iconContainer.addClassName("stat-icon-container");
        iconContainer.getStyle()
                .set("width", "clamp(50px, 10vw, 60px)")
                .set("height", "clamp(50px, 10vw, 60px)")
                .set("border-radius", "12px")
                .set("background", "linear-gradient(135deg, " + color + " 0%, " + adjustColorBrightness(color, -20) + " 100%)")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("margin-bottom", "16px")
                .set("box-shadow", "0 4px 12px " + hexToRgba(color, 0.3));

        Icon icon = iconType.create();
        icon.getStyle()
                .set("color", "white")
                .set("width", "clamp(24px, 5vw, 28px)")
                .set("height", "clamp(24px, 5vw, 28px)");

        iconContainer.add(icon);

        // Value (number)
        H2 valueSpan = new H2(value);
        valueSpan.addClassNames(LumoUtility.Margin.NONE, "stat-value");
        valueSpan.getStyle()
                .set("font-size", "clamp(1.75rem, 5vw, 2.25rem)")
                .set("font-weight", "700")
                .set("color", "#333")
                .set("margin-bottom", "4px")
                .set("line-height", "1");

        // Label
        Span labelSpan = new Span(label);
        labelSpan.addClassName("stat-label");
        labelSpan.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("font-size", "clamp(0.85rem, 2vw, 0.95rem)")
                .set("font-weight", "500");

        card.add(iconContainer, valueSpan, labelSpan);

        // Hover effect
        card.getElement().addEventListener("mouseenter", e -> {
            card.getStyle()
                    .set("transform", "translateY(-8px)")
                    .set("box-shadow", "0 12px 24px rgba(102, 126, 234, 0.15)");
        });

        card.getElement().addEventListener("mouseleave", e -> {
            card.getStyle()
                    .set("transform", "translateY(0)")
                    .set("box-shadow", "0 4px 12px rgba(102, 126, 234, 0.1)");
        });

        return card;
    }

    /**
     * Create feature cards section with modern design
     */
    private Div createFeatureSection() {
        Div featureSection = new Div();
        featureSection.addClassName("feature-section");

        featureSection.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(300px, 1fr))")
                .set("gap", "clamp(20px, 3vw, 32px)")
                .set("margin-bottom", "clamp(24px, 4vw, 40px)");

        // Feature cards
        Div coursesFeature = createFeatureCard(
                VaadinIcon.ACADEMY_CAP,
                "My Courses",
                "View and manage your enrolled courses",
                "linear-gradient(135deg, #667eea, #764ba2)",
                "0.5s"
        );

        Div assignmentsFeature = createFeatureCard(
                VaadinIcon.CLIPBOARD_TEXT,
                "Assignments",
                "Track your assignments and submissions",
                "linear-gradient(135deg, #f093fb, #f5576c)",
                "0.6s"
        );

        Div resourcesFeature = createFeatureCard(
                VaadinIcon.FILE_TEXT,
                "Resources",
                "Access learning materials and documents",
                "linear-gradient(135deg, #4fd1c5, #63b3ed)",
                "0.7s"
        );

        featureSection.add(coursesFeature, assignmentsFeature, resourcesFeature);

        return featureSection;
    }

    /**
     * Create individual feature card
     */
    private Div createFeatureCard(VaadinIcon iconType, String title, String description, String gradient, String animationDelay) {
        Div card = new Div();
        card.addClassName("feature-card");

        // Premium card styling with glassmorphism
        card.getStyle()
                .set("background", "white")
                .set("border-radius", "clamp(14px, 2vw, 18px)")
                .set("padding", "clamp(24px, 5vw, 32px)")
                .set("box-shadow", "0 8px 20px rgba(102, 126, 234, 0.12)")
                .set("transition", "all 0.4s cubic-bezier(0.4, 0, 0.2, 1)")
                .set("cursor", "pointer")
                .set("position", "relative")
                .set("overflow", "hidden")
                .set("border", "2px solid transparent")
                .set("animation", "fadeInUp 0.6s ease-out " + animationDelay + " both");

        // Gradient border effect on hover
        Div borderGlow = new Div();
        borderGlow.getStyle()
                .set("position", "absolute")
                .set("top", "0")
                .set("left", "0")
                .set("right", "0")
                .set("height", "4px")
                .set("background", gradient)
                .set("border-radius", "clamp(14px, 2vw, 18px) clamp(14px, 2vw, 18px) 0 0");

        card.add(borderGlow);

        // Icon with gradient background
        Div iconWrapper = new Div();
        iconWrapper.getStyle()
                .set("width", "clamp(60px, 12vw, 70px)")
                .set("height", "clamp(60px, 12vw, 70px)")
                .set("border-radius", "14px")
                .set("background", gradient)
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("margin-bottom", "20px")
                .set("box-shadow", "0 8px 16px rgba(102, 126, 234, 0.2)");

        Icon icon = iconType.create();
        icon.getStyle()
                .set("color", "white")
                .set("width", "clamp(28px, 6vw, 32px)")
                .set("height", "clamp(28px, 6vw, 32px)");

        iconWrapper.add(icon);

        // Title
        H3 titleHeading = new H3(title);
        titleHeading.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Margin.Bottom.SMALL);
        titleHeading.getStyle()
                .set("color", "#333")
                .set("font-size", "clamp(1.1rem, 3vw, 1.3rem)")
                .set("font-weight", "600")
                .set("margin-bottom", "8px");

        // Description
        Paragraph desc = new Paragraph(description);
        desc.addClassNames(LumoUtility.Margin.NONE);
        desc.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("font-size", "clamp(0.85rem, 2vw, 0.95rem)")
                .set("line-height", "1.6");

        card.add(iconWrapper, titleHeading, desc);

        // Hover effects
        card.getElement().addEventListener("mouseenter", e -> {
            card.getStyle()
                    .set("transform", "translateY(-8px) scale(1.02)")
                    .set("box-shadow", "0 16px 32px rgba(102, 126, 234, 0.2)")
                    .set("border-color", "rgba(102, 126, 234, 0.3)");
        });

        card.getElement().addEventListener("mouseleave", e -> {
            card.getStyle()
                    .set("transform", "translateY(0) scale(1)")
                    .set("box-shadow", "0 8px 20px rgba(102, 126, 234, 0.12)")
                    .set("border-color", "transparent");
        });

        return card;
    }

    /**
     * Create quick actions section
     */
    private Div createQuickActionsSection() {
        Div quickActions = new Div();
        quickActions.addClassName("quick-actions-section");

        quickActions.getStyle()
                .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
                .set("border-radius", "clamp(14px, 2vw, 20px)")
                .set("padding", "clamp(28px, 5vw, 40px)")
                .set("box-shadow", "0 12px 28px rgba(102, 126, 234, 0.3)")
                .set("color", "white")
                .set("animation", "fadeInUp 0.6s ease-out 0.8s both");

        H2 title = new H2("Quick Actions");
        title.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Margin.Bottom.MEDIUM);
        title.getStyle()
                .set("color", "white")
                .set("font-size", "clamp(1.3rem, 4vw, 1.75rem)")
                .set("font-weight", "600");

        Paragraph message = new Paragraph(
                "Start exploring ShikshaSpace features. More content will be available soon as we integrate our learning platform modules."
        );
        message.addClassNames(LumoUtility.Margin.NONE);
        message.getStyle()
                .set("color", "rgba(255, 255, 255, 0.9)")
                .set("font-size", "clamp(0.9rem, 2.5vw, 1rem)")
                .set("line-height", "1.7")
                .set("max-width", "800px");

        quickActions.add(title, message);

        return quickActions;
    }

    /**
     * Utility: Convert hex color to rgba
     */
    private String hexToRgba(String hex, double alpha) {
        hex = hex.replace("#", "");
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return String.format("rgba(%d, %d, %d, %.2f)", r, g, b, alpha);
    }

    /**
     * Utility: Adjust color brightness
     */
    private String adjustColorBrightness(String hex, int percent) {
        hex = hex.replace("#", "");
        int r = Math.max(0, Math.min(255, Integer.parseInt(hex.substring(0, 2), 16) + percent));
        int g = Math.max(0, Math.min(255, Integer.parseInt(hex.substring(2, 4), 16) + percent));
        int b = Math.max(0, Math.min(255, Integer.parseInt(hex.substring(4, 6), 16) + percent));
        return String.format("#%02x%02x%02x", r, g, b);
    }
}
