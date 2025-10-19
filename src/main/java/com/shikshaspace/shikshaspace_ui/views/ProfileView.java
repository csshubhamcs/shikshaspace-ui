package com.shikshaspace.shikshaspace_ui.views;

import com.shikshaspace.shikshaspace_ui.components.MainLayout;
import com.shikshaspace.shikshaspace_ui.dto.UserProfileDTO;
import com.shikshaspace.shikshaspace_ui.service.UserServiceClient;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import jakarta.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Profile view for editing user profile with automatic token refresh
 */
@Slf4j
@Route(value = "profile", layout = MainLayout.class)
@PageTitle("Edit Profile | ShikshaSpace")
@PermitAll
public class ProfileView extends VerticalLayout {

    private final UserServiceClient userServiceClient;

    private final TextField firstNameField;
    private final TextField lastNameField;
    private final TextField usernameField;
    private final EmailField emailField;
    private final TextField mobileNumberField;
    private final NumberField ageField;
    private final TextField bioField;
    private final NumberField experienceField;
    private final TextField linkedinUrlField;
    private final TextField githubUrlField;
    private final TextField profileImageUrlField;

    private final Button saveButton;
    private final Button cancelButton;

    private UserProfileDTO currentProfile;

    public ProfileView(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        // Page title
        H2 title = new H2("Edit Profile");
        title.getStyle()
                .set("color", "#667eea")
                .set("margin-bottom", "20px");

        // Form container
        VerticalLayout formContainer = new VerticalLayout();
        formContainer.setMaxWidth("800px");
        formContainer.setPadding(true);
        formContainer.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)");

        // Form layout
        FormLayout formLayout = new FormLayout();
        formLayout.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("500px", 2)
        );

        // First Name field
        firstNameField = new TextField("First Name");
        firstNameField.setPlaceholder("Enter your first name");
        firstNameField.setRequired(true);

        // Last Name field
        lastNameField = new TextField("Last Name");
        lastNameField.setPlaceholder("Enter your last name");
        lastNameField.setRequired(true);

        // Username field (read-only)
        usernameField = new TextField("Username");
        usernameField.setReadOnly(true);
        usernameField.getStyle().set("color", "var(--lumo-secondary-text-color)");

        // Email field
        emailField = new EmailField("Email");
        emailField.setPlaceholder("your.email@example.com");
        emailField.setRequired(true);
        emailField.setErrorMessage("Please enter a valid email");

        // Mobile Number field
        mobileNumberField = new TextField("Mobile Number");
        mobileNumberField.setPlaceholder("+91 1234567890");
        mobileNumberField.setClearButtonVisible(true);

        // Age field
        ageField = new NumberField("Age");
        ageField.setPlaceholder("Your age");
        ageField.setMin(0);
        ageField.setMax(150);
        ageField.setStep(1);

        // Bio field
        bioField = new TextField("Bio");
        bioField.setPlaceholder("Tell us about yourself");
        bioField.setClearButtonVisible(true);

        // Experience field
        experienceField = new NumberField("Experience (Years)");
        experienceField.setPlaceholder("Years of experience");
        experienceField.setMin(0);
        experienceField.setMax(50);
        experienceField.setStep(0.5);

        // LinkedIn URL field
        linkedinUrlField = new TextField("LinkedIn Profile");
        linkedinUrlField.setPlaceholder("https://linkedin.com/in/yourprofile");
        linkedinUrlField.setClearButtonVisible(true);

        // GitHub URL field
        githubUrlField = new TextField("GitHub Profile");
        githubUrlField.setPlaceholder("https://github.com/yourusername");
        githubUrlField.setClearButtonVisible(true);

        // Profile Image URL field
        profileImageUrlField = new TextField("Profile Image URL");
        profileImageUrlField.setPlaceholder("https://example.com/image.jpg");
        profileImageUrlField.setClearButtonVisible(true);

        // Add fields to form
        formLayout.add(
                firstNameField, lastNameField,
                usernameField, ageField,
                emailField, mobileNumberField,
                experienceField, linkedinUrlField,
                githubUrlField, profileImageUrlField
        );
        formLayout.setColspan(bioField, 2);
        formLayout.setColspan(emailField, 2);
        formLayout.setColspan(linkedinUrlField, 2);
        formLayout.setColspan(githubUrlField, 2);
        formLayout.setColspan(profileImageUrlField, 2);

        formLayout.add(bioField);

        // Buttons
        saveButton = new Button("Save Changes");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(e -> saveProfile());

        cancelButton = new Button("Cancel");
        cancelButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("")));

        HorizontalLayout buttonLayout = new HorizontalLayout(saveButton, cancelButton);
        buttonLayout.setSpacing(true);
        buttonLayout.getStyle().set("margin-top", "20px");

        // Assemble form
        formContainer.add(formLayout, buttonLayout);

        add(title, formContainer);
        setAlignItems(Alignment.CENTER);

        // Load user profile
        loadProfile();
    }

    /**
     * Load current user profile with automatic token refresh
     */
    private void loadProfile() {
        String token = (String) VaadinSession.getCurrent().getAttribute("jwt_token");

        if (token == null) {
            Notification.show("Session expired. Please login again.",
                            3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            getUI().ifPresent(ui -> ui.navigate("login"));
            return;
        }

        setFormEnabled(false);
        saveButton.setText("Loading...");

        userServiceClient.getUserProfile(token)
                .onErrorResume(error -> {
                    if (error.getMessage() != null && error.getMessage().contains("401")) {
                        log.info("Token expired, attempting refresh");
                        return attemptTokenRefreshAndRetry();
                    }
                    return Mono.error(error);
                })
                .subscribe(
                        this::displayProfile,
                        this::handleLoadError
                );
    }

    /**
     * Attempt to refresh token and retry loading profile
     */
    private Mono<UserProfileDTO> attemptTokenRefreshAndRetry() {
        String refreshToken = (String) VaadinSession.getCurrent().getAttribute("refresh_token");

        if (refreshToken == null) {
            return Mono.error(new RuntimeException("No refresh token available"));
        }

        return userServiceClient.refreshToken(refreshToken)
                .flatMap(authResponse -> {
                    VaadinSession.getCurrent().setAttribute("jwt_token", authResponse.getToken());
                    VaadinSession.getCurrent().setAttribute("refresh_token", authResponse.getRefreshToken());

                    log.info("Token refreshed, retrying profile load");
                    return userServiceClient.getUserProfile(authResponse.getToken());
                })
                .onErrorResume(error -> {
                    log.error("Token refresh failed", error);
                    return Mono.error(new RuntimeException("Session expired. Please login again."));
                });
    }

    /**
     * Display loaded profile in form
     */
    private void displayProfile(UserProfileDTO profile) {
        this.currentProfile = profile;

        firstNameField.setValue(profile.getFirstName() != null ? profile.getFirstName() : "");
        lastNameField.setValue(profile.getLastName() != null ? profile.getLastName() : "");
        usernameField.setValue(profile.getUsername() != null ? profile.getUsername() : "");
        emailField.setValue(profile.getEmail() != null ? profile.getEmail() : "");
        mobileNumberField.setValue(profile.getMobileNumber() != null ? profile.getMobileNumber() : "");
        ageField.setValue(profile.getAge() != null ? profile.getAge().doubleValue() : null);
        bioField.setValue(profile.getBio() != null ? profile.getBio() : "");
        experienceField.setValue(profile.getExperience() != null ? profile.getExperience() : 0.0);
        linkedinUrlField.setValue(profile.getLinkedinUrl() != null ? profile.getLinkedinUrl() : "");
        githubUrlField.setValue(profile.getGithubUrl() != null ? profile.getGithubUrl() : "");
        profileImageUrlField.setValue(profile.getProfileImageUrl() != null ? profile.getProfileImageUrl() : "");

        setFormEnabled(true);
        saveButton.setText("Save Changes");
    }

    /**
     * Handle profile load error
     */
    private void handleLoadError(Throwable error) {
        log.error("Failed to load profile", error);

        Notification.show("Failed to load profile. Please try again.",
                        3000, Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);

        setFormEnabled(true);
        saveButton.setText("Save Changes");
    }

    /**
     * Save updated profile with automatic token refresh
     */
    private void saveProfile() {
        if (firstNameField.isEmpty()) {
            Notification.show("First name is required",
                            3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            firstNameField.focus();
            return;
        }

        if (lastNameField.isEmpty()) {
            Notification.show("Last name is required",
                            3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            lastNameField.focus();
            return;
        }

        if (emailField.isEmpty() || emailField.isInvalid()) {
            Notification.show("Valid email is required",
                            3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            emailField.focus();
            return;
        }

        UserProfileDTO updatedProfile = new UserProfileDTO();
        updatedProfile.setId(currentProfile.getId());
        updatedProfile.setUsername(currentProfile.getUsername());
        updatedProfile.setFirstName(firstNameField.getValue().trim());
        updatedProfile.setLastName(lastNameField.getValue().trim());
        updatedProfile.setEmail(emailField.getValue().trim());
        updatedProfile.setMobileNumber(mobileNumberField.getValue().trim());
        updatedProfile.setAge(ageField.getValue() != null ? ageField.getValue().intValue() : null);
        updatedProfile.setBio(bioField.getValue().trim());
        updatedProfile.setExperience(experienceField.getValue());
        updatedProfile.setLinkedinUrl(linkedinUrlField.getValue().trim());
        updatedProfile.setGithubUrl(githubUrlField.getValue().trim());
        updatedProfile.setProfileImageUrl(profileImageUrlField.getValue().trim());

        setFormEnabled(false);
        saveButton.setText("Saving...");

        String token = (String) VaadinSession.getCurrent().getAttribute("jwt_token");

        userServiceClient.updateProfile(token, updatedProfile)
                .onErrorResume(error -> {
                    if (error.getMessage() != null && error.getMessage().contains("401")) {
                        log.info("Token expired during save, attempting refresh");
                        return attemptTokenRefreshAndSave(updatedProfile);
                    }
                    return Mono.error(error);
                })
                .subscribe(
                        this::handleSaveSuccess,
                        this::handleSaveError
                );
    }

    /**
     * Attempt to refresh token and retry saving profile
     */
    private Mono<UserProfileDTO> attemptTokenRefreshAndSave(UserProfileDTO profile) {
        String refreshToken = (String) VaadinSession.getCurrent().getAttribute("refresh_token");

        if (refreshToken == null) {
            return Mono.error(new RuntimeException("No refresh token available"));
        }

        return userServiceClient.refreshToken(refreshToken)
                .flatMap(authResponse -> {
                    VaadinSession.getCurrent().setAttribute("jwt_token", authResponse.getToken());
                    VaadinSession.getCurrent().setAttribute("refresh_token", authResponse.getRefreshToken());

                    log.info("Token refreshed, retrying profile save");
                    return userServiceClient.updateProfile(authResponse.getToken(), profile);
                });
    }

    /**
     * Handle successful save
     */
    private void handleSaveSuccess(UserProfileDTO savedProfile) {
        log.info("Profile updated successfully for user: {}", savedProfile.getUsername());

        this.currentProfile = savedProfile;
        setFormEnabled(true);
        saveButton.setText("Save Changes");

        Notification.show("Profile updated successfully!",
                        3000, Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    /**
     * Handle save error
     */
    private void handleSaveError(Throwable error) {
        log.error("Failed to save profile", error);

        setFormEnabled(true);
        saveButton.setText("Save Changes");

        String errorMsg = "Failed to update profile. Please try again.";
        if (error.getMessage() != null && error.getMessage().contains("409")) {
            errorMsg = "Email already in use by another account";
        }

        Notification.show(errorMsg,
                        3000, Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    /**
     * Enable/disable form fields
     */
    private void setFormEnabled(boolean enabled) {
        firstNameField.setEnabled(enabled);
        lastNameField.setEnabled(enabled);
        emailField.setEnabled(enabled);
        mobileNumberField.setEnabled(enabled);
        ageField.setEnabled(enabled);
        bioField.setEnabled(enabled);
        experienceField.setEnabled(enabled);
        linkedinUrlField.setEnabled(enabled);
        githubUrlField.setEnabled(enabled);
        profileImageUrlField.setEnabled(enabled);
        saveButton.setEnabled(enabled);
        cancelButton.setEnabled(enabled);
    }
}
