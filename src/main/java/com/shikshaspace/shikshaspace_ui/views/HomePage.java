package com.shikshaspace.shikshaspace_ui.views;

import com.shikshaspace.shikshaspace_ui.components.MainLayout;
import com.shikshaspace.shikshaspace_ui.security.SecurityUtils;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

/** HomePage - Both root ("") and "home" routes */
@Route(value = "", layout = MainLayout.class) // ✅ FIX: Root route
@PageTitle("Home - ShikshaSpace")
@PermitAll
public class HomePage extends VerticalLayout {

  public HomePage() {
    setSizeFull();
    setPadding(false);
    setSpacing(false);
    addClassName("home-page-container");

    getStyle()
        .set("background", "linear-gradient(180deg, #f8f9ff 0%, #ffffff 100%)")
        .set("min-height", "100vh")
        .set("overflow-y", "auto")
        .set("cursor", "default"); // ✅ FIX: default cursor

    // Content container
    VerticalLayout content = new VerticalLayout();
    content.setMaxWidth("1200px");
    content.setWidthFull();
    content.setPadding(true);
    content.setSpacing(true);
    content.getStyle().set("margin", "0 auto").set("padding", "clamp(24px, 5vw, 60px)");

    // Welcome message (visible, centered)
    String username = SecurityUtils.getUsername();
    H2 welcomeTitle = new H2("Welcome, " + username + "!");
    welcomeTitle
        .getStyle()
        .set("color", "#333")
        .set("text-align", "center")
        .set("font-size", "clamp(1.5rem, 4vw, 2.5rem)")
        .set("margin", "0 0 16px 0")
        .set("font-weight", "600");

    Paragraph subtitle = new Paragraph("Host or join learning sessions with the community");
    subtitle
        .getStyle()
        .set("color", "#666")
        .set("text-align", "center")
        .set("font-size", "1.1rem")
        .set("margin", "0 0 40px 0");

    // Host Session button
    Button hostButton = new Button("Host a Session", VaadinIcon.PLUS_CIRCLE.create());
    hostButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
    hostButton
        .getStyle()
        .set("margin", "0 auto 48px")
        .set("cursor", "pointer")
        .set("padding", "16px 32px")
        .set("font-size", "1rem");

    // Sessions section
    H3 sessionsHeader = new H3("Upcoming Sessions");
    sessionsHeader
        .getStyle()
        .set("color", "#333")
        .set("margin", "0 0 24px 0")
        .set("text-align", "center")
        .set("font-size", "1.5rem");

    // Placeholder
    Div placeholder = createPlaceholder();

    content.add(welcomeTitle, subtitle, hostButton, sessionsHeader, placeholder);
    add(content);
  }

  private Div createPlaceholder() {
    Div placeholder = new Div();

    Icon icon = VaadinIcon.CALENDAR.create();
    icon.getStyle()
        .set("width", "64px")
        .set("height", "64px")
        .set("color", "#667eea")
        .set("margin-bottom", "16px");

    Paragraph text = new Paragraph("No sessions available yet. Be the first to host one!");
    text.getStyle().set("color", "#999").set("font-size", "1.1rem").set("margin", "0");

    placeholder.add(icon, text);
    placeholder
        .getStyle()
        .set("background", "white")
        .set("padding", "60px 40px")
        .set("border-radius", "12px")
        .set("box-shadow", "0 4px 12px rgba(102, 126, 234, 0.1)")
        .set("text-align", "center")
        .set("max-width", "600px")
        .set("margin", "0 auto")
        .set("display", "flex")
        .set("flex-direction", "column")
        .set("align-items", "center")
        .set("cursor", "default");

    return placeholder;
  }
}
