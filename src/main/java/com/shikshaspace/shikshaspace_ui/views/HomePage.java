package com.shikshaspace.shikshaspace_ui.views;

import com.shikshaspace.shikshaspace_ui.components.MainLayout;
import com.shikshaspace.shikshaspace_ui.security.SecurityUtils;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;

/**
 * Production-grade home page with responsive design. Welcome page after successful authentication.
 */
@Slf4j
@Route(value = "", layout = MainLayout.class)
@PageTitle("Home - ShikshaSpace")
@PermitAll
public class HomePage extends VerticalLayout {

  public HomePage() {
    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);
    addClassName("home-page-container");

    String username = SecurityUtils.getUsername();
    log.info("Loading home page for user: {}", username);

    Div welcomeCard = createWelcomeCard(username);
    add(welcomeCard);
  }

  /** Create welcome card with user greeting. */
  private Div createWelcomeCard(String username) {
    Div card = new Div();
    card.addClassName("welcome-card");
    card.getStyle()
        .set("background", "white")
        .set("padding", "clamp(24px, 5vw, 48px)")
        .set("border-radius", "clamp(12px, 2vw, 20px)")
        .set("box-shadow", "0 8px 32px rgba(0, 0, 0, 0.1)")
        .set("max-width", "clamp(320px, 90vw, 600px)")
        .set("width", "100%")
        .set("text-align", "center");

    Icon checkIcon = VaadinIcon.CHECK_CIRCLE.create();
    checkIcon.setSize("64px");
    checkIcon.getStyle().set("color", "#4caf50").set("margin-bottom", "16px");

    H1 title = new H1("Welcome to ShikshaSpace!");
    title.addClassName("gradient-text");
    title.getStyle().set("font-size", "clamp(1.5rem, 5vw, 2.5rem)").set("margin-bottom", "16px");

    H2 greeting = new H2("Hello, " + (username != null ? username : "User") + "!");
    greeting
        .getStyle()
        .set("color", "#333")
        .set("font-size", "clamp(1.2rem, 4vw, 1.8rem)")
        .set("font-weight", "400")
        .set("margin-bottom", "16px");

    Paragraph description =
        new Paragraph(
            "You have successfully signed in. Explore your profile and manage your account.");
    description
        .getStyle()
        .set("color", "#666")
        .set("font-size", "clamp(14px, 3vw, 16px)")
        .set("line-height", "1.6")
        .set("margin", "0");

    card.add(checkIcon, title, greeting, description);
    return card;
  }
}
