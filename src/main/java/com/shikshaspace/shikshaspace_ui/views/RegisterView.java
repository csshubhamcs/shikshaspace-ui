package com.shikshaspace.shikshaspace_ui.views;

import com.shikshaspace.shikshaspace_ui.dto.AuthResponse;
import com.shikshaspace.shikshaspace_ui.dto.RegisterRequest;
import com.shikshaspace.shikshaspace_ui.service.SecurityUtils;
import com.shikshaspace.shikshaspace_ui.service.UserServiceClient;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.VaadinSession;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

/**
 * Registration view for new users
 * Includes form validation and User Service integration
 */
@Slf4j
@Route("register")
@PageTitle("Register | ShikshaSpace")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    private final UserServiceClient userServiceClient;

    private final TextField firstNameField;
    private final TextField lastNameField;
    private final TextField usernameField;
    private final EmailField emailField;
    private final PasswordField passwordField;
    private final PasswordField confirmPasswordField;
    private final Button registerButton;
    private final Div errorMessage;
    private final Div passwordStrength;

    public RegisterView(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
        
        // Page styling
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle()
                .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
                .set("padding", "20px");

        // Register card container
        VerticalLayout registerCard = new VerticalLayout();
        registerCard.setWidth("450px");
        registerCard.setPadding(true);
        registerCard.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("box-shadow", "0 8px 32px rgba(0,0,0,0.1)")
                .set("max-height", "90vh")
                .set("overflow-y", "auto");

        // Logo/Title
        H1 logo = new H1("ShikshaSpace");
        logo.getStyle()
                .set("color", "#667eea")
                .set("margin", "0")
                .set("text-align", "center");

        H2 welcomeText = new H2("Create Account");
        welcomeText.getStyle()
                .set("color", "#333")
                .set("margin-top", "0")
                .set("font-weight", "400")
                .set("text-align", "center");

        // Error message container
        errorMessage = new Div();
        errorMessage.setVisible(false);
        errorMessage.getStyle()
                .set("color", "white")
                .set("background", "#f44336")
                .set("padding", "10px")
                .set("border-radius", "6px")
                .set("margin-bottom", "15px")
                .set("text-align", "center");

        // First Name field
        firstNameField = new TextField("First Name");
        firstNameField.setPlaceholder("Enter your first name");
        firstNameField.setRequired(true);
        firstNameField.setClearButtonVisible(true);
        lastNameField = new TextField("Last Name");
        lastNameField.setPlaceholder("Enter your last name");
        lastNameField.setRequired(true);
        lastNameField.setClearButtonVisible(true);

        // Username field
        usernameField = new TextField("Username");
        usernameField.setWidthFull();
        usernameField.setPlaceholder("Choose a username");
        usernameField.setRequired(true);
        usernameField.setClearButtonVisible(true);
        usernameField.setMinLength(3);
        usernameField.setMaxLength(50);
        usernameField.setHelperText("Minimum 3 characters");

        // Email field
        emailField = new EmailField("Email");
        emailField.setWidthFull();
        emailField.setPlaceholder("your.email@example.com");
        emailField.setRequired(true);
        emailField.setClearButtonVisible(true);
        emailField.setErrorMessage("Please enter a valid email address");

        // Password field with strength indicator
        passwordField = new PasswordField("Password");
        passwordField.setWidthFull();
        passwordField.setPlaceholder("Create a strong password");
        passwordField.setRequired(true);
        passwordField.setRevealButtonVisible(true);
        passwordField.setMinLength(6);
        passwordField.setHelperText("Minimum 6 characters");
        
        // Password strength indicator
        passwordStrength = new Div();
        passwordStrength.setVisible(false);
        passwordStrength.getStyle()
                .set("padding", "5px 10px")
                .set("border-radius", "4px")
                .set("margin-top", "5px")
                .set("font-size", "12px")
                .set("text-align", "center");

        // Update strength on password change
        passwordField.setValueChangeMode(ValueChangeMode.EAGER);
        passwordField.addValueChangeListener(event -> 
            updatePasswordStrength(event.getValue())
        );

        // Confirm Password field
        confirmPasswordField = new PasswordField("Confirm Password");
        confirmPasswordField.setWidthFull();
        confirmPasswordField.setPlaceholder("Re-enter your password");
        confirmPasswordField.setRequired(true);
        confirmPasswordField.setRevealButtonVisible(true);
        confirmPasswordField.setHelperText("Must match password");

        // Register button
        registerButton = new Button("Create Account");
        registerButton.setWidthFull();
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        registerButton.getStyle().set("margin-top", "10px");
        registerButton.addClickListener(event -> handleRegister());

        // Enter key shortcut
        confirmPasswordField.addKeyPressListener(event -> {
            if (event.getKey().equals("Enter")) {
                handleRegister();
            }
        });

        // Login link
        Paragraph loginText = new Paragraph();
        loginText.getStyle().set("text-align", "center").set("margin-top", "15px");
        loginText.setText("Already have an account? ");
        
        RouterLink loginLink = new RouterLink("Login here", LoginView.class);
        loginLink.getStyle()
                .set("color", "#667eea")
                .set("font-weight", "500")
                .set("text-decoration", "none");
        
        loginText.add(loginLink);

        // Assemble register card
        registerCard.add(
                logo,
                welcomeText,
                errorMessage,
                firstNameField,
                lastNameField,
                usernameField,
                emailField,
                passwordField,
                passwordStrength,
                confirmPasswordField,
                registerButton,
                loginText
        );

        add(registerCard);
    }

    /**
     * Update password strength indicator
     */
    private void updatePasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            passwordStrength.setVisible(false);
            return;
        }

        passwordStrength.setVisible(true);
        
        int strength = calculatePasswordStrength(password);
        
        if (strength < 30) {
            passwordStrength.setText("Weak");
            passwordStrength.getStyle()
                    .set("background", "#f44336")
                    .set("color", "white");
        } else if (strength < 60) {
            passwordStrength.setText("Medium");
            passwordStrength.getStyle()
                    .set("background", "#ff9800")
                    .set("color", "white");
        } else {
            passwordStrength.setText("Strong");
            passwordStrength.getStyle()
                    .set("background", "#4caf50")
                    .set("color", "white");
        }
    }

    /**
     * Calculate password strength (0-100)
     */
    private int calculatePasswordStrength(String password) {
        int strength = 0;
        
        if (password.length() >= 6) strength += 20;
        if (password.length() >= 10) strength += 20;
        if (password.matches(".*[a-z].*")) strength += 15;
        if (password.matches(".*[A-Z].*")) strength += 15;
        if (password.matches(".*\\d.*")) strength += 15;
        if (password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>/?].*")) strength += 15;
        
        return Math.min(strength, 100);
    }

    /**
     * Handle registration form submission
     */
    private void handleRegister() {
        // Validate all fields
        if (firstNameField.getValue().isBlank() || lastNameField.getValue().isBlank() ||
                usernameField.getValue().isBlank() || emailField.getValue().isBlank() ||
                passwordField.getValue().isBlank() || confirmPasswordField.getValue().isBlank()) {

            showError("All fields are required");
            return;
        }

        if (!passwordField.getValue().equals(confirmPasswordField.getValue())) {
            showError("Passwords do not match");
            return;
        }

        // Show loading state
        registerButton.setEnabled(false);
        registerButton.setText("Creating Account...");
        errorMessage.setVisible(false);

        try {
            // BLOCKING CALL - Appropriate for Vaadin UI
            RegisterRequest request = new RegisterRequest(
                    firstNameField.getValue(),
                    lastNameField.getValue(),
                    usernameField.getValue(),
                    emailField.getValue(),
                    passwordField.getValue()
            );

            AuthResponse response = userServiceClient
                    .register(request)
                    .block(Duration.ofSeconds(15));  // ← BLOCK HERE (longer for registration)

            if (response != null && response.getToken() != null) {
                // Authenticate user in Spring Security
                SecurityUtils.authenticateUser(
                        response.getUsername(),
                        response.getUserId(),
                        response.getToken()
                );

                // Store refresh token separately
                VaadinSession.getCurrent().setAttribute("refresh_token", response.getRefreshToken());

                // Show success
                Notification.show("Account created! Welcome, " + response.getUsername() + "!",
                                3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                // Navigate to home
                UI.getCurrent().navigate("");

            } else {
                throw new RuntimeException("Invalid response from server");
            }

        } catch (Exception e) {
            log.error("Registration failed for user: {}", usernameField.getValue(), e);

            // Re-enable button
            registerButton.setEnabled(true);
            registerButton.setText("Create Account");

            // Show error
            String errorMsg = "Registration failed. Please try again.";
            if (e.getMessage() != null && e.getMessage().contains("409")) {
                errorMsg = "Username or email already exists. Please use different credentials.";
            } else if (e.getMessage() != null && e.getMessage().contains("timeout")) {
                errorMsg = "Connection timeout. Check your internet connection.";
            }

            showError(errorMsg);
        }
    }


    /**
     * Validate email format
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    /**
     * Handle successful registration
     */
    private void handleRegisterSuccess(AuthResponse response) {
        log.info("Registration successful for user: {}", response.getUsername());

        UI ui = UI.getCurrent();
        if (ui != null) {
            ui.access(() -> {
                // Store tokens (auto-login)
                VaadinSession.getCurrent().setAttribute("jwt_token", response.getToken());
                VaadinSession.getCurrent().setAttribute("refresh_token", response.getRefreshToken());
                VaadinSession.getCurrent().setAttribute("username", response.getUsername());
                VaadinSession.getCurrent().setAttribute("user_id", response.getUserId());

                // Show success
                Notification.show("Account created! Welcome, " + response.getUsername() + "!",
                                3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                // Redirect to home
                UI.getCurrent().navigate("");
            });
        }
    }


    /**
     * Handle registration error
     */
    private void handleRegisterError(Throwable error) {
        log.error("Registration failed", error);

        UI ui = UI.getCurrent();
        if (ui != null) {
            ui.access(() -> {
                // Re-enable button
                registerButton.setEnabled(true);
                registerButton.setText("Create Account");
                registerButton.getElement().setProperty("loading", false);

                // Show error message
                String errorMsg = "Registration failed. Please try again.";
                if (error.getMessage() != null && error.getMessage().contains("409")) {
                    errorMsg = "Username or email already exists";
                } else if (error.getMessage() != null && error.getMessage().contains("400")) {
                    errorMsg = "Invalid input. Please check your details.";
                } else if (error.getMessage() != null && error.getMessage().contains("timeout")) {
                    errorMsg = "Connection timeout. Please try again.";
                }

                showError(errorMsg);
            });
        }
    }


    /**
     * Show error message
     */
    private void showError(String message) {
        errorMessage.setText(message);
        errorMessage.setVisible(true);
    }
}
