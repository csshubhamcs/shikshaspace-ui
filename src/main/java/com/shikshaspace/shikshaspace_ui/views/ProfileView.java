package com.shikshaspace.shikshaspace_ui.views;

import com.shikshaspace.shikshaspace_ui.components.MainLayout;
import com.shikshaspace.shikshaspace_ui.components.ProfileEditDialog;
import com.shikshaspace.shikshaspace_ui.dto.UpdateProfileRequest;
import com.shikshaspace.shikshaspace_ui.dto.UserResponse;
import com.shikshaspace.shikshaspace_ui.security.SecurityUtils;
import com.shikshaspace.shikshaspace_ui.service.UserServiceClient;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

/**
 * Production-grade responsive ProfileView with modern premium design
 * Supports mobile (320px+), tablet (768px+), and desktop (1024px+)
 *
 * Features:
 * - Mobile-first responsive design
 * - Modern profile card with avatar
 * - Info cards with icons
 * - Premium glassmorphism effects
 * - Smooth animations and transitions
 * - Touch-friendly edit dialog
 * - Responsive grid layout
 *
 * @author ShikshaSpace Engineering Team
 * @version 2.0 (Responsive + Premium UI)
 */
@Slf4j
@Route(value = "profile", layout = MainLayout.class)
@PageTitle("Profile | ShikshaSpace")
@PermitAll
public class ProfileView extends VerticalLayout {

    private final UserServiceClient userServiceClient;
    private UserResponse currentUser;

    // UI Components
    private Avatar userAvatar;
    private H2 nameHeader;
    private Span usernameLabel;
    private Span emailLabel;
    private Div profileCard;
    private Div infoGrid;
    private Button editButton;

    public ProfileView(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;

        setSizeFull();
        setPadding(false);
        setSpacing(false);
        addClassName("profile-view-container");

        // Responsive background with gradient
        getStyle()
                .set("background", "linear-gradient(180deg, #f8f9ff 0%, #ffffff 100%)")
                .set("min-height", "100vh")
                .set("overflow-y", "auto");

        // Main content container
        VerticalLayout mainContent = createMainContent();

        add(mainContent);

        // Load profile data
        loadProfile();
    }

    /**
     * Create main content container with responsive layout
     */
    private VerticalLayout createMainContent() {
        VerticalLayout content = new VerticalLayout();
        content.setWidthFull();
        content.setPadding(true);
        content.setSpacing(true);
        content.addClassName("profile-content-wrapper");

        // Responsive container with max-width
        content.getStyle()
                .set("max-width", "1200px")
                .set("margin", "0 auto")
                .set("padding", "clamp(16px, 4vw, 40px)");

        // Create profile header card
        profileCard = createProfileCard();

        // Create info grid
        infoGrid = createInfoGrid();

        content.add(profileCard, infoGrid);

        return content;
    }

    /**
     * Create main profile card with avatar and basic info
     */
    private Div createProfileCard() {
        Div card = new Div();
        card.addClassName("profile-header-card");

        // Modern card styling with glassmorphism
        card.getStyle()
                .set("background", "white")
                .set("border-radius", "clamp(14px, 2vw, 20px)")
                .set("padding", "clamp(28px, 5vw, 48px)")
                .set("box-shadow", "0 8px 24px rgba(102, 126, 234, 0.12)")
                .set("margin-bottom", "clamp(24px, 4vw, 32px)")
                .set("position", "relative")
                .set("overflow", "hidden")
                .set("border", "1px solid rgba(102, 126, 234, 0.1)")
                .set("animation", "fadeInUp 0.6s ease-out");

        // Gradient accent bar
        Div accentBar = new Div();
        accentBar.getStyle()
                .set("position", "absolute")
                .set("top", "0")
                .set("left", "0")
                .set("right", "0")
                .set("height", "6px")
                .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)");

        card.add(accentBar);

        // Content layout (avatar + info + edit button)
        HorizontalLayout contentLayout = new HorizontalLayout();
        contentLayout.setWidthFull();
        contentLayout.setSpacing(true);
        contentLayout.setAlignItems(FlexComponent.Alignment.START);
        contentLayout.addClassName("profile-card-content");

        contentLayout.getStyle()
                .set("flex-wrap", "wrap")
                .set("gap", "clamp(16px, 3vw, 24px)")
                .set("margin-top", "20px");

        // Avatar section
        VerticalLayout avatarSection = createAvatarSection();

        // Info section
        VerticalLayout infoSection = createInfoSection();

        // Edit button (desktop: top right, mobile: bottom)
        editButton = createEditButton();

        // Layout: avatar + info fills space, edit button on right (or below on mobile)
        contentLayout.add(avatarSection, infoSection);
        contentLayout.expand(infoSection);
        contentLayout.add(editButton);

        card.add(contentLayout);

        return card;
    }

    /**
     * Create avatar section with large circular avatar
     */
    private VerticalLayout createAvatarSection() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(false);
        section.setAlignItems(FlexComponent.Alignment.CENTER);
        section.addClassName("avatar-section");

        section.getStyle()
                .set("flex", "0 0 auto");

        // Large avatar with premium styling
        userAvatar = new Avatar();
        userAvatar.addClassName("profile-avatar");
        userAvatar.getStyle()
                .set("--vaadin-avatar-size", "clamp(80px, 15vw, 120px)")
                .set("border", "4px solid white")
                .set("box-shadow", "0 8px 20px rgba(102, 126, 234, 0.2)")
                .set("background", "linear-gradient(135deg, #667eea, #764ba2)")
                .set("color", "white")
                .set("font-weight", "600")
                .set("font-size", "clamp(2rem, 4vw, 3rem)");

        section.add(userAvatar);

        return section;
    }

    /**
     * Create info section with name, username, email
     */
    private VerticalLayout createInfoSection() {
        VerticalLayout section = new VerticalLayout();
        section.setPadding(false);
        section.setSpacing(false);
        section.addClassName("info-section");

        section.getStyle()
                .set("flex", "1 1 300px")
                .set("gap", "clamp(8px, 2vw, 12px)");

        // Name header
        nameHeader = new H2();
        nameHeader.addClassNames(
                LumoUtility.Margin.NONE,
                "profile-name"
        );
        nameHeader.getStyle()
                .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
                .set("-webkit-background-clip", "text")
                .set("-webkit-text-fill-color", "transparent")
                .set("background-clip", "text")
                .set("font-size", "clamp(1.5rem, 4vw, 2rem)")
                .set("font-weight", "700")
                .set("line-height", "1.2");

        // Username with icon
        HorizontalLayout usernameLayout = new HorizontalLayout();
        usernameLayout.setPadding(false);
        usernameLayout.setSpacing(true);
        usernameLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        usernameLayout.getStyle().set("gap", "8px");

        Icon usernameIcon = VaadinIcon.USER.create();
        usernameIcon.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("width", "18px")
                .set("height", "18px");

        usernameLabel = new Span();
        usernameLabel.addClassName("profile-username");
        usernameLabel.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("font-size", "clamp(0.9rem, 2.5vw, 1rem)")
                .set("font-weight", "500");

        usernameLayout.add(usernameIcon, usernameLabel);

        // Email with icon
        HorizontalLayout emailLayout = new HorizontalLayout();
        emailLayout.setPadding(false);
        emailLayout.setSpacing(true);
        emailLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        emailLayout.getStyle().set("gap", "8px");

        Icon emailIcon = VaadinIcon.ENVELOPE.create();
        emailIcon.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("width", "18px")
                .set("height", "18px");

        emailLabel = new Span();
        emailLabel.addClassName("profile-email");
        emailLabel.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("font-size", "clamp(0.85rem, 2.5vw, 0.95rem)")
                .set("word-break", "break-word");

        emailLayout.add(emailIcon, emailLabel);

        section.add(nameHeader, usernameLayout, emailLayout);

        return section;
    }

    /**
     * Create edit profile button with premium design
     */
    private Button createEditButton() {
        Button button = new Button("Edit Profile");
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.setIcon(VaadinIcon.EDIT.create());
        button.addClassName("profile-edit-button");

        button.getStyle()
                .set("border-radius", "10px")
                .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
                .set("font-weight", "600")
                .set("font-size", "clamp(14px, 2.5vw, 16px)")
                .set("padding", "clamp(10px, 2vw, 14px) clamp(16px, 3vw, 24px)")
                .set("box-shadow", "0 4px 12px rgba(102, 126, 234, 0.3)")
                .set("transition", "all 0.3s cubic-bezier(0.4, 0, 0.2, 1)")
                .set("cursor", "pointer")
                .set("white-space", "nowrap")
                .set("align-self", "flex-start");

        // Hover effects
        button.getElement().addEventListener("mouseenter", e -> {
            button.getStyle()
                    .set("transform", "translateY(-2px)")
                    .set("box-shadow", "0 6px 16px rgba(102, 126, 234, 0.4)");
        });

        button.getElement().addEventListener("mouseleave", e -> {
            button.getStyle()
                    .set("transform", "translateY(0)")
                    .set("box-shadow", "0 4px 12px rgba(102, 126, 234, 0.3)");
        });

        button.addClickListener(e -> openEditDialog());

        return button;
    }

    /**
     * Create info grid with detailed profile information
     */
    private Div createInfoGrid() {
        Div grid = new Div();
        grid.addClassName("profile-info-grid");

        // Responsive grid layout
        grid.getStyle()
                .set("display", "grid")
                .set("grid-template-columns", "repeat(auto-fit, minmax(280px, 1fr))")
                .set("gap", "clamp(16px, 3vw, 24px)")
                .set("animation", "fadeInUp 0.6s ease-out 0.2s both");

        // Info cards will be added dynamically
        return grid;
    }

    /**
     * Create individual info card
     */
    private Div createInfoCard(VaadinIcon iconType, String label, String value, String animationDelay) {
        Div card = new Div();
        card.addClassName("profile-info-card");

        // Modern card styling
        card.getStyle()
                .set("background", "white")
                .set("border-radius", "clamp(12px, 2vw, 16px)")
                .set("padding", "clamp(20px, 4vw, 24px)")
                .set("box-shadow", "0 4px 12px rgba(102, 126, 234, 0.1)")
                .set("transition", "all 0.3s cubic-bezier(0.4, 0, 0.2, 1)")
                .set("cursor", "default")
                .set("border", "1px solid rgba(102, 126, 234, 0.1)")
                .set("animation", "fadeInUp 0.6s ease-out " + animationDelay + " both");

        // Icon with background
        Div iconWrapper = new Div();
        iconWrapper.getStyle()
                .set("width", "clamp(45px, 8vw, 55px)")
                .set("height", "clamp(45px, 8vw, 55px)")
                .set("border-radius", "12px")
                .set("background", "linear-gradient(145deg, #f8f9ff, #ffffff)")
                .set("display", "flex")
                .set("align-items", "center")
                .set("justify-content", "center")
                .set("margin-bottom", "16px")
                .set("border", "2px solid rgba(102, 126, 234, 0.1)");

        Icon icon = iconType.create();
        icon.getStyle()
                .set("color", "#667eea")
                .set("width", "clamp(22px, 4vw, 26px)")
                .set("height", "clamp(22px, 4vw, 26px)");

        iconWrapper.add(icon);

        // Label
        Span labelSpan = new Span(label);
        labelSpan.addClassName("info-card-label");
        labelSpan.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("font-size", "clamp(0.8rem, 2vw, 0.9rem)")
                .set("font-weight", "500")
                .set("display", "block")
                .set("margin-bottom", "6px")
                .set("text-transform", "uppercase")
                .set("letter-spacing", "0.5px");

        // Value
        Span valueSpan = new Span(value != null && !value.isEmpty() ? value : "Not set");
        valueSpan.addClassName("info-card-value");
        valueSpan.getStyle()
                .set("color", value != null && !value.isEmpty() ? "#333" : "#999")
                .set("font-size", "clamp(1rem, 2.5vw, 1.1rem)")
                .set("font-weight", "600")
                .set("display", "block")
                .set("word-break", "break-word")
                .set("line-height", "1.4");

        card.add(iconWrapper, labelSpan, valueSpan);

        // Hover effect
        card.getElement().addEventListener("mouseenter", e -> {
            card.getStyle()
                    .set("transform", "translateY(-4px)")
                    .set("box-shadow", "0 8px 20px rgba(102, 126, 234, 0.15)");
        });

        card.getElement().addEventListener("mouseleave", e -> {
            card.getStyle()
                    .set("transform", "translateY(0)")
                    .set("box-shadow", "0 4px 12px rgba(102, 126, 234, 0.1)");
        });

        return card;
    }

    /**
     * Load profile data from backend
     */
    private void loadProfile() {
        try {
            String jwtToken = SecurityUtils.getJwtToken();

            // BLOCKING CALL - Correct for Vaadin
            currentUser = userServiceClient.getCurrentUserProfile(jwtToken)
                    .block(Duration.ofSeconds(10));

            if (currentUser != null) {
                updateUI();
            } else {
                showError("Failed to load profile");
            }

        } catch (Exception e) {
            log.error("Failed to load profile", e);
            showError("Failed to load profile: " + e.getMessage());
        }
    }

    /**
     * Update UI with profile data
     */
    private void updateUI() {
        // Update avatar
        String initials = "";
        if (currentUser.getFirstName() != null && !currentUser.getFirstName().isEmpty()) {
            initials += currentUser.getFirstName().charAt(0);
        }
        if (currentUser.getLastName() != null && !currentUser.getLastName().isEmpty()) {
            initials += currentUser.getLastName().charAt(0);
        }
        userAvatar.setName(initials.toUpperCase());

        // Update header info
        nameHeader.setText(
                (currentUser.getFirstName() != null ? currentUser.getFirstName() : "") + " " +
                        (currentUser.getLastName() != null ? currentUser.getLastName() : "")
        );
        usernameLabel.setText("@" + currentUser.getUsername());
        emailLabel.setText(currentUser.getEmail());

        // Clear and rebuild info grid
        infoGrid.removeAll();

        // Add info cards
        if (currentUser.getAge() != null && currentUser.getAge() > 0) {
            infoGrid.add(createInfoCard(
                    VaadinIcon.CALENDAR,
                    "Age",
                    currentUser.getAge() + " years",
                    "0.3s"
            ));
        }

        if (currentUser.getExperience() != null && currentUser.getExperience() > 0) {
            infoGrid.add(createInfoCard(
                    VaadinIcon.BRIEFCASE,
                    "Experience",
                    currentUser.getExperience() + " years",
                    "0.4s"
            ));
        }

        if (currentUser.getBio() != null && !currentUser.getBio().isBlank()) {
            infoGrid.add(createInfoCard(
                    VaadinIcon.FILE_TEXT,
                    "Bio",
                    currentUser.getBio(),
                    "0.5s"
            ));
        }

        if (currentUser.getLinkedinUrl() != null && !currentUser.getLinkedinUrl().isBlank()) {
            infoGrid.add(createInfoCard(
                    VaadinIcon.GLOBE,
                    "LinkedIn",
                    currentUser.getLinkedinUrl(),
                    "0.6s"
            ));
        }

        if (currentUser.getGithubUrl() != null && !currentUser.getGithubUrl().isBlank()) {
            infoGrid.add(createInfoCard(
                    VaadinIcon.CODE,
                    "GitHub",
                    currentUser.getGithubUrl(),
                    "0.7s"
            ));
        }

        // Add placeholder if no additional info
        if (infoGrid.getChildren().count() == 0) {
            Div placeholder = new Div();
            placeholder.setText("Complete your profile by adding more information!");
            placeholder.getStyle()
                    .set("grid-column", "1 / -1")
                    .set("text-align", "center")
                    .set("padding", "40px")
                    .set("color", "var(--lumo-secondary-text-color)")
                    .set("font-style", "italic")
                    .set("background", "linear-gradient(145deg, #ffffff, #f8f9ff)")
                    .set("border-radius", "12px")
                    .set("border", "2px dashed rgba(102, 126, 234, 0.2)");
            infoGrid.add(placeholder);
        }
    }

    /**
     * Open edit dialog
     */
    private void openEditDialog() {
        ProfileEditDialog dialog = new ProfileEditDialog(currentUser, this::handleSave);
        dialog.open();
    }

    /**
     * Handle profile update
     * PRESERVED FROM ORIGINAL
     */
    private void handleSave(UserResponse updatedUser) {
        try {
            String jwtToken = SecurityUtils.getJwtToken();

            UpdateProfileRequest request = new UpdateProfileRequest();
            request.setFirstName(updatedUser.getFirstName());
            request.setLastName(updatedUser.getLastName());
            request.setAge(updatedUser.getAge());
            request.setExperience(updatedUser.getExperience());
            request.setBio(updatedUser.getBio());
            request.setLinkedinUrl(updatedUser.getLinkedinUrl());
            request.setGithubUrl(updatedUser.getGithubUrl());
            request.setProfileImageUrl(updatedUser.getProfileImageUrl());

            // BLOCKING CALL
            UserResponse result = userServiceClient.updateProfile(
                            currentUser.getId(), request, jwtToken)
                    .block(Duration.ofSeconds(10));

            if (result != null) {
                currentUser = result;
                updateUI();

                Notification.show("Profile updated successfully!",
                                3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            }

        } catch (Exception e) {
            log.error("Failed to update profile", e);
            showError("Failed to update profile: " + e.getMessage());
        }
    }

    /**
     * Show error notification
     */
    private void showError(String message) {
        Notification.show(message,
                        5000, Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
