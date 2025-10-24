package com.shikshaspace.shikshaspace_ui.views;

import com.shikshaspace.shikshaspace_ui.components.MainLayout;
import com.shikshaspace.shikshaspace_ui.dto.UserResponse;
import com.shikshaspace.shikshaspace_ui.security.SecurityUtils;
import com.shikshaspace.shikshaspace_ui.service.UserServiceClient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;

/**
 * Production-grade profile view with responsive design. Displays user information and allows
 * profile editing.
 */
@Slf4j
@Route(value = "profile", layout = MainLayout.class)
@PageTitle("Profile - ShikshaSpace")
@PermitAll
public class ProfileView extends VerticalLayout {

  private final UserServiceClient userServiceClient;

  private Div profileCard;
  private UserResponse currentUser;

  public ProfileView(UserServiceClient userServiceClient) {
    this.userServiceClient = userServiceClient;

    setSizeFull();
    setPadding(true);
    setSpacing(true);
    setAlignItems(Alignment.CENTER);
    addClassName("profile-view-container");

    getStyle()
        .set("background", "linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)")
        .set("min-height", "100vh")
        .set("padding", "clamp(16px, 4vw, 40px)");

    loadUserProfile();
  }

  /** Load user profile from backend service. */
  private void loadUserProfile() {
    String token = SecurityUtils.getToken();

    if (token == null) {
      showError("Authentication required. Please login again.");
      return;
    }

    try {
      currentUser = userServiceClient.getCurrentUserProfile(token).block(Duration.ofSeconds(10));

      if (currentUser != null) {
        profileCard = createProfileCard(currentUser);
        add(profileCard);
      } else {
        showError("Failed to load profile");
      }
    } catch (Exception e) {
      log.error("Error loading profile", e);
      showError("Error loading profile. Please try again.");
    }
  }

  /** Create profile card with user information. */
  private Div createProfileCard(UserResponse user) {
    Div card = new Div();
    card.addClassName("profile-card-responsive");
    card.getStyle()
        .set("background", "white")
        .set("padding", "clamp(24px, 5vw, 48px)")
        .set("border-radius", "clamp(12px, 2vw, 20px)")
        .set("box-shadow", "0 8px 32px rgba(0, 0, 0, 0.1)")
        .set("max-width", "clamp(320px, 90vw, 700px)")
        .set("width", "100%");

    // Header with title and edit button
    HorizontalLayout header = createHeader();

    // Profile content
    VerticalLayout content = createProfileContent(user);

    card.add(header, content);
    return card;
  }

  /** Create header with title and actions. */
  private HorizontalLayout createHeader() {
    H2 title = new H2("My Profile");
    title.addClassName("gradient-text");
    title.getStyle().set("font-size", "clamp(1.5rem, 4vw, 2rem)").set("margin", "0");

    Button editButton = new Button("Edit Profile", VaadinIcon.EDIT.create());
    editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    editButton
        .getStyle()
        .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
        .set("border", "none");
    editButton.addClickListener(e -> openEditDialog());

    HorizontalLayout header = new HorizontalLayout(title, editButton);
    header.setWidthFull();
    header.setAlignItems(Alignment.CENTER);
    header.setJustifyContentMode(JustifyContentMode.BETWEEN);
    header.getStyle().set("margin-bottom", "clamp(16px, 3vw, 24px)");

    return header;
  }

  /** Create profile content section with user details. */
  private VerticalLayout createProfileContent(UserResponse user) {
    VerticalLayout content = new VerticalLayout();
    content.setSpacing(true);
    content.setPadding(false);
    content.setWidthFull();

    content.add(
        createInfoRow(VaadinIcon.USER, "Username", user.getUsername()),
        createInfoRow(VaadinIcon.ENVELOPE, "Email", user.getEmail()),
        createInfoRow(VaadinIcon.USER_CARD, "First Name", user.getFirstName()),
        createInfoRow(VaadinIcon.USER_CARD, "Last Name", user.getLastName()));

    if (user.getAge() != null) {
      content.add(createInfoRow(VaadinIcon.CALENDAR, "Age", String.valueOf(user.getAge())));
    }

    if (user.getBio() != null && !user.getBio().isEmpty()) {
      content.add(createInfoRow(VaadinIcon.FILE_TEXT, "Bio", user.getBio()));
    }

    if (user.getExperience() != null) {
      content.add(
          createInfoRow(VaadinIcon.BRIEFCASE, "Experience", user.getExperience() + " years"));
    }

    return content;
  }

  /** Create info row with icon, label, and value. */
  private Div createInfoRow(VaadinIcon iconType, String label, String value) {
    Div row = new Div();
    row.addClassName("profile-info-row");
    row.getStyle()
        .set("display", "flex")
        .set("align-items", "flex-start")
        .set("gap", "12px")
        .set("padding", "12px 0")
        .set("border-bottom", "1px solid #f0f0f0");

    Icon icon = iconType.create();
    icon.setSize("20px");
    icon.getStyle().set("color", "#667eea");

    Div textContainer = new Div();
    textContainer.getStyle().set("flex", "1").set("min-width", "0");

    Span labelSpan = new Span(label);
    labelSpan
        .getStyle()
        .set("display", "block")
        .set("font-size", "12px")
        .set("color", "#999")
        .set("font-weight", "500")
        .set("margin-bottom", "4px");

    Span valueSpan = new Span(value != null ? value : "Not provided");
    valueSpan
        .getStyle()
        .set("display", "block")
        .set("font-size", "clamp(14px, 3vw, 16px)")
        .set("color", "#333")
        .set("word-break", "break-word");

    textContainer.add(labelSpan, valueSpan);
    row.add(icon, textContainer);

    return row;
  }

  /** Open edit profile dialog. */
  private void openEditDialog() {
    Notification.show("Edit functionality coming soon!", 3000, Notification.Position.MIDDLE)
        .addThemeVariants(NotificationVariant.LUMO_PRIMARY);
  }

  /** Show error notification. */
  private void showError(String message) {
    Notification.show(message, 5000, Notification.Position.MIDDLE)
        .addThemeVariants(NotificationVariant.LUMO_ERROR);
  }
}
