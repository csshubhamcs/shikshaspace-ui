package com.shikshaspace.shikshaspace_ui.views;

import com.shikshaspace.shikshaspace_ui.components.MainLayout;
import com.shikshaspace.shikshaspace_ui.components.ProfileEditDialog;
import com.shikshaspace.shikshaspace_ui.dto.UpdateProfileRequest;
import com.shikshaspace.shikshaspace_ui.dto.UserResponse;
import com.shikshaspace.shikshaspace_ui.security.SecurityUtils;
import com.shikshaspace.shikshaspace_ui.service.UserServiceClient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
@Route(value = "profile", layout = MainLayout.class)
@PageTitle("Profile | ShikshaSpace")
@PermitAll
public class ProfileView extends VerticalLayout {

    private final UserServiceClient userServiceClient;
    private UserResponse currentUser;

    // UI Components
    private final Div profileCard;
    private final H2 nameHeader;
    private final Paragraph emailText;
    private final Paragraph usernameText;
    private final Paragraph ageText;
    private final Paragraph experienceText;
    private final Paragraph bioText;
    private final Paragraph linkedinText;
    private final Paragraph githubText;
    private final Button editButton;

    public ProfileView(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;

        setSizeFull();
        setPadding(true);
        setAlignItems(Alignment.CENTER);

        // Profile Card
        profileCard = new Div();
        profileCard.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("box-shadow", "0 4px 6px rgba(0,0,0,0.1)")
                .set("padding", "30px")
                .set("max-width", "700px")
                .set("width", "100%");

        // Header with edit button
        nameHeader = new H2();
        nameHeader.getStyle().set("color", "#667eea").set("margin", "0");

        editButton = new Button("Edit Profile", e -> openEditDialog());
        editButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout header = new HorizontalLayout(nameHeader, editButton);
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.CENTER);

        // Profile details
        usernameText = new Paragraph();
        emailText = new Paragraph();
        ageText = new Paragraph();
        experienceText = new Paragraph();
        bioText = new Paragraph();
        linkedinText = new Paragraph();
        githubText = new Paragraph();

        profileCard.add(header, usernameText, emailText, ageText,
                experienceText, bioText, linkedinText, githubText);

        add(profileCard);

        // Load profile data
        loadProfile();
    }

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

    private void updateUI() {
        nameHeader.setText(currentUser.getFirstName() + " " + currentUser.getLastName());
        usernameText.setText("Username: " + currentUser.getUsername());
        emailText.setText("Email: " + currentUser.getEmail());

        if (currentUser.getAge() != null && currentUser.getAge() > 0) {
            ageText.setText("Age: " + currentUser.getAge());
            ageText.setVisible(true);
        } else {
            ageText.setVisible(false);
        }

        if (currentUser.getExperience() != null && currentUser.getExperience() > 0) {
            experienceText.setText("Experience: " + currentUser.getExperience() + " years");
            experienceText.setVisible(true);
        } else {
            experienceText.setVisible(false);
        }

        if (currentUser.getBio() != null && !currentUser.getBio().isBlank()) {
            bioText.setText("Bio: " + currentUser.getBio());
            bioText.setVisible(true);
        } else {
            bioText.setVisible(false);
        }

        if (currentUser.getLinkedinUrl() != null && !currentUser.getLinkedinUrl().isBlank()) {
            linkedinText.setText("LinkedIn: " + currentUser.getLinkedinUrl());
            linkedinText.setVisible(true);
        } else {
            linkedinText.setVisible(false);
        }

        if (currentUser.getGithubUrl() != null && !currentUser.getGithubUrl().isBlank()) {
            githubText.setText("GitHub: " + currentUser.getGithubUrl());
            githubText.setVisible(true);
        } else {
            githubText.setVisible(false);
        }
    }

    private void openEditDialog() {
        ProfileEditDialog dialog = new ProfileEditDialog(currentUser, this::handleSave);
        dialog.open();
    }

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

    private void showError(String message) {
        Notification.show(message,
                        5000, Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
}
