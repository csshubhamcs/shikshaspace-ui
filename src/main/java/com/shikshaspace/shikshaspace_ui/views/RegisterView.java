package com.shikshaspace.shikshaspace_ui.views;

import com.shikshaspace.shikshaspace_ui.dto.AuthResponse;
import com.shikshaspace.shikshaspace_ui.dto.RegisterRequest;
import com.shikshaspace.shikshaspace_ui.security.SecurityUtils;
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
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

/**
 * Production-grade responsive RegisterView with modern premium design
 * Supports mobile (320px+), tablet (768px+), and desktop (1024px+)
 *
 * Features:
 * - Mobile-first responsive design
 * - Real-time password strength indicator
 * - Modern glassmorphism effects
 * - Smooth animations and transitions
 * - Touch-friendly form elements
 * - Fluid typography
 * - Input validation with visual feedback
 *
 * @author ShikshaSpace Engineering Team
 * @version 2.0 (Responsive + Premium UI)
 */
@Slf4j
@Route("register")
@PageTitle("Register | ShikshaSpace")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

    private final UserServiceClient userServiceClient;

    private TextField firstNameField;
    private TextField lastNameField;
    private TextField usernameField;
    private EmailField emailField;
    private PasswordField passwordField;
    private PasswordField confirmPasswordField;
    private Button registerButton;
    private Div errorMessage;
    private Div passwordStrength;

    public RegisterView(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;

        // Full-viewport responsive container with gradient background
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("register-view-container");

        // Modern gradient background with responsive padding
        getStyle()
                .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
                .set("padding", "clamp(16px, 4vw, 40px)")
                .set("min-height", "100vh")
                .set("position", "relative")
                .set("overflow-y", "auto");

        // Register card container with responsive width
        VerticalLayout registerCard = createRegisterCard();

        add(registerCard);
    }

    /**
     * Create responsive register card with premium design
     */
    private VerticalLayout createRegisterCard() {
        VerticalLayout registerCard = new VerticalLayout();
        registerCard.setPadding(true);
        registerCard.setSpacing(true);
        registerCard.addClassName("register-card-responsive");

        // Responsive width using clamp for fluid sizing
        registerCard.getStyle()
                .set("width", "100%")
                .set("max-width", "clamp(320px, 90vw, 500px)")
                .set("background", "rgba(255, 255, 255, 0.98)")
                .set("border-radius", "clamp(12px, 2vw, 20px)")
                .set("box-shadow", "0 8px 32px rgba(0, 0, 0, 0.15), 0 20px 60px rgba(102, 126, 234, 0.2)")
                .set("padding", "clamp(24px, 5vw, 40px)")
                .set("backdrop-filter", "blur(10px)")
                .set("border", "1px solid rgba(255, 255, 255, 0.3)")
                .set("max-height", "95vh")
                .set("overflow-y", "auto")
                .set("transition", "all 0.3s cubic-bezier(0.4, 0, 0.2, 1)");

        // Logo with gradient text and fluid typography
        H1 logo = createLogo();

        // Welcome text with responsive sizing
        H2 welcomeText = createWelcomeText();

        // Error message container
        errorMessage = createErrorMessage();

        // Create form fields with responsive layout
        createFormFields();

        // Register button with premium design
        registerButton = createRegisterButton();

        // Login link container
        Div loginContainer = createLoginContainer();

        // Assemble register card
        registerCard.add(
                logo,
                welcomeText,
                errorMessage,
                createNameFieldsLayout(),
                usernameField,
                emailField,
                passwordField,
                passwordStrength,
                confirmPasswordField,
                registerButton,
                loginContainer
        );

        return registerCard;
    }

    /**
     * Create responsive logo with gradient text effect
     */
    private H1 createLogo() {
        H1 logo = new H1("ShikshaSpace");
        logo.addClassNames(
                LumoUtility.Margin.NONE,
                LumoUtility.Margin.Bottom.SMALL,
                "register-logo"
        );
        logo.getStyle()
                .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
                .set("-webkit-background-clip", "text")
                .set("-webkit-text-fill-color", "transparent")
                .set("background-clip", "text")
                .set("font-size", "clamp(1.75rem, 6vw, 2.5rem)")
                .set("font-weight", "700")
                .set("text-align", "center")
                .set("letter-spacing", "0.5px")
                .set("margin-bottom", "4px");

        return logo;
    }

    /**
     * Create welcome text with responsive sizing
     */
    private H2 createWelcomeText() {
        H2 welcomeText = new H2("Create Account");
        welcomeText.addClassNames(
                LumoUtility.Margin.Top.NONE,
                LumoUtility.Margin.Bottom.MEDIUM,
                "welcome-text"
        );
        welcomeText.getStyle()
                .set("color", "#333")
                .set("font-weight", "400")
                .set("text-align", "center")
                .set("font-size", "clamp(1rem, 3vw, 1.5rem)")
                .set("margin-bottom", "clamp(12px, 3vw, 20px)");

        return welcomeText;
    }

    /**
     * Create error message container with modern styling
     */
    private Div createErrorMessage() {
        Div error = new Div();
        error.setVisible(false);
        error.addClassName("error-message-box");
        error.getStyle()
                .set("color", "white")
                .set("background", "linear-gradient(135deg, #f44336 0%, #e91e63 100%)")
                .set("padding", "clamp(10px, 2.5vw, 14px)")
                .set("border-radius", "10px")
                .set("margin-bottom", "16px")
                .set("text-align", "center")
                .set("font-size", "clamp(12px, 2.5vw, 14px)")
                .set("font-weight", "500")
                .set("box-shadow", "0 4px 12px rgba(244, 67, 54, 0.3)")
                .set("animation", "slideDown 0.3s ease-out")
                .set("border", "1px solid rgba(255, 255, 255, 0.2)");

        return error;
    }

    /**
     * Create all form fields
     */
    private void createFormFields() {
        // First Name field
        firstNameField = new TextField("First Name");
        firstNameField.setPlaceholder("Enter your first name");
        firstNameField.setRequired(true);
        firstNameField.setClearButtonVisible(true);
        styleInputField(firstNameField);

        // Last Name field
        lastNameField = new TextField("Last Name");
        lastNameField.setPlaceholder("Enter your last name");
        lastNameField.setRequired(true);
        lastNameField.setClearButtonVisible(true);
        styleInputField(lastNameField);

        // Username field
        usernameField = new TextField("Username");
        usernameField.setWidthFull();
        usernameField.setPlaceholder("Choose a username");
        usernameField.setRequired(true);
        usernameField.setClearButtonVisible(true);
        usernameField.setMinLength(3);
        usernameField.setMaxLength(50);
        usernameField.setHelperText("Minimum 3 characters");
        styleInputField(usernameField);

        // Email field
        emailField = new EmailField("Email");
        emailField.setWidthFull();
        emailField.setPlaceholder("your.email@example.com");
        emailField.setRequired(true);
        emailField.setClearButtonVisible(true);
        emailField.setErrorMessage("Please enter a valid email address");
        styleInputField(emailField);

        // Password field
        passwordField = new PasswordField("Password");
        passwordField.setWidthFull();
        passwordField.setPlaceholder("Create a strong password");
        passwordField.setRequired(true);
        passwordField.setRevealButtonVisible(true);
        passwordField.setMinLength(6);
        passwordField.setHelperText("Minimum 6 characters");
        styleInputField(passwordField);

        // Password strength indicator
        passwordStrength = createPasswordStrengthIndicator();

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
        styleInputField(confirmPasswordField);

        // Enter key shortcut for last field
        confirmPasswordField.addKeyPressListener(event -> {
            if (event.getKey().equals("Enter")) {
                handleRegister();
            }
        });
    }

    /**
     * Create horizontal layout for name fields (side by side on desktop)
     */
    private HorizontalLayout createNameFieldsLayout() {
        HorizontalLayout nameLayout = new HorizontalLayout(firstNameField, lastNameField);
        nameLayout.setWidthFull();
        nameLayout.setSpacing(true);
        nameLayout.addClassName("name-fields-layout");

        // Responsive: stack on mobile, side-by-side on desktop
        nameLayout.getStyle()
                .set("gap", "clamp(8px, 2vw, 16px)")
                .set("flex-wrap", "wrap");

        // Make fields grow to fill space
        firstNameField.getStyle().set("flex", "1 1 200px");
        lastNameField.getStyle().set("flex", "1 1 200px");

        return nameLayout;
    }

    /**
     * Apply consistent styling to input fields
     */
    private void styleInputField(com.vaadin.flow.component.Component field) {
        field.getElement().getStyle()
                .set("--vaadin-input-field-border-radius", "10px")
                .set("--vaadin-input-field-background", "linear-gradient(145deg, #ffffff, #f8f9ff)")
                .set("--vaadin-input-field-border-width", "2px")
                .set("--vaadin-input-field-border-color", "transparent")
                .set("--vaadin-input-field-hover-highlight", "#667eea")
                .set("margin-bottom", "clamp(10px, 2vw, 14px)")
                .set("font-size", "clamp(14px, 2.5vw, 16px)")
                .set("transition", "all 0.3s ease");

        field.getElement().getClassList().add("premium-input-field");

        // Add focus enhancement
        if (field instanceof com.vaadin.flow.component.HasValue) {
            field.getElement().addEventListener("focus", e -> {
                field.getElement().getStyle()
                        .set("box-shadow", "0 0 0 4px rgba(102, 126, 234, 0.1)")
                        .set("transform", "translateY(-2px)");
            });

            field.getElement().addEventListener("blur", e -> {
                field.getElement().getStyle()
                        .set("box-shadow", "none")
                        .set("transform", "translateY(0)");
            });
        }
    }

    /**
     * Create password strength indicator
     */
    private Div createPasswordStrengthIndicator() {
        Div strengthDiv = new Div();
        strengthDiv.setVisible(false);
        strengthDiv.addClassName("password-strength-indicator");
        strengthDiv.getStyle()
                .set("padding", "clamp(6px, 1.5vw, 10px)")
                .set("border-radius", "8px")
                .set("margin-top", "-8px")
                .set("margin-bottom", "12px")
                .set("font-size", "clamp(11px, 2vw, 13px)")
                .set("font-weight", "600")
                .set("text-align", "center")
                .set("transition", "all 0.3s ease")
                .set("text-transform", "uppercase")
                .set("letter-spacing", "0.5px");

        return strengthDiv;
    }

    /**
     * Update password strength indicator with animations
     */
    private void updatePasswordStrength(String password) {
        if (password == null || password.isEmpty()) {
            passwordStrength.setVisible(false);
            return;
        }

        passwordStrength.setVisible(true);

        int strength = calculatePasswordStrength(password);

        if (strength < 30) {
            passwordStrength.setText("⚠ Weak");
            passwordStrength.getStyle()
                    .set("background", "linear-gradient(135deg, #f44336, #e91e63)")
                    .set("color", "white")
                    .set("box-shadow", "0 2px 8px rgba(244, 67, 54, 0.3)");
        } else if (strength < 60) {
            passwordStrength.setText("⚡ Medium");
            passwordStrength.getStyle()
                    .set("background", "linear-gradient(135deg, #ff9800, #ff5722)")
                    .set("color", "white")
                    .set("box-shadow", "0 2px 8px rgba(255, 152, 0, 0.3)");
        } else {
            passwordStrength.setText("✓ Strong");
            passwordStrength.getStyle()
                    .set("background", "linear-gradient(135deg, #4caf50, #8bc34a)")
                    .set("color", "white")
                    .set("box-shadow", "0 2px 8px rgba(76, 175, 80, 0.3)");
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
     * Create register button with modern premium design and animations
     */
    private Button createRegisterButton() {
        Button button = new Button("Create Account");
        button.setWidthFull();
        button.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_LARGE
        );
        button.addClassName("premium-register-button");

        // Modern button styling with responsive sizing
        button.getStyle()
                .set("margin-top", "clamp(8px, 2vw, 12px)")
                .set("padding", "clamp(14px, 3vw, 18px)")
                .set("border-radius", "10px")
                .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
                .set("font-weight", "600")
                .set("font-size", "clamp(15px, 2.5vw, 16px)")
                .set("letter-spacing", "0.5px")
                .set("box-shadow", "0 4px 15px rgba(102, 126, 234, 0.4)")
                .set("transition", "all 0.3s cubic-bezier(0.4, 0, 0.2, 1)")
                .set("cursor", "pointer")
                .set("position", "relative")
                .set("overflow", "hidden")
                .set("min-height", "44px");

        // Hover effect
        button.getElement().addEventListener("mouseenter", e -> {
            button.getStyle()
                    .set("transform", "translateY(-2px)")
                    .set("box-shadow", "0 6px 20px rgba(102, 126, 234, 0.5)");
        });

        button.getElement().addEventListener("mouseleave", e -> {
            button.getStyle()
                    .set("transform", "translateY(0)")
                    .set("box-shadow", "0 4px 15px rgba(102, 126, 234, 0.4)");
        });

        button.addClickListener(event -> handleRegister());

        return button;
    }

    /**
     * Create login link container with modern styling
     */
    private Div createLoginContainer() {
        Div loginContainer = new Div();
        loginContainer.addClassName("login-container");
        loginContainer.getStyle()
                .set("text-align", "center")
                .set("margin-top", "clamp(16px, 3vw, 24px)")
                .set("padding", "clamp(14px, 3vw, 18px)")
                .set("background", "linear-gradient(145deg, #f8f9ff, #ffffff)")
                .set("border-radius", "10px")
                .set("border", "2px solid rgba(102, 126, 234, 0.15)")
                .set("transition", "all 0.3s ease");

        Paragraph loginText = new Paragraph();
        loginText.addClassName("login-text");
        loginText.getStyle()
                .set("margin", "0")
                .set("color", "#555")
                .set("font-size", "clamp(13px, 2.5vw, 15px)")
                .set("line-height", "1.6");

        loginText.setText("Already have an account? ");

        RouterLink loginLink = new RouterLink("Login here", LoginView.class);
        loginLink.addClassName("login-link");
        loginLink.getStyle()
                .set("color", "#667eea")
                .set("font-weight", "600")
                .set("text-decoration", "none")
                .set("font-size", "clamp(14px, 2.5vw, 16px)")
                .set("transition", "all 0.2s ease")
                .set("border-bottom", "2px solid transparent");

        // Hover effect for login link
        loginLink.getElement().addEventListener("mouseenter", e -> {
            loginLink.getStyle()
                    .set("color", "#764ba2")
                    .set("border-bottom-color", "#764ba2");
        });

        loginLink.getElement().addEventListener("mouseleave", e -> {
            loginLink.getStyle()
                    .set("color", "#667eea")
                    .set("border-bottom-color", "transparent");
        });

        loginText.add(loginLink);
        loginContainer.add(loginText);

        return loginContainer;
    }

    /**
     * Handle registration form submission
     * ALL ORIGINAL LOGIC PRESERVED
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

        // Show loading state with modern animation
        registerButton.setEnabled(false);
        registerButton.setText("Creating Account...");
        registerButton.getStyle().set("opacity", "0.7");
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
                    .block(Duration.ofSeconds(15));

            if (response != null && response.getToken() != null) {
                // Authenticate user in Spring Security
                SecurityUtils.authenticateUser(
                        response.getUsername(),
                        response.getUserId(),
                        response.getToken(),
                        response.getEmail()
                );

                // Store refresh token separately
                VaadinSession.getCurrent().setAttribute("refresh_token", response.getRefreshToken());

                // Show success with modern notification
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
            registerButton.getStyle().set("opacity", "1");

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
     * ALL REMAINING METHODS PRESERVED FROM ORIGINAL
     */
    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    private void handleRegisterSuccess(AuthResponse response) {
        log.info("Registration successful for user: {}", response.getUsername());

        UI ui = UI.getCurrent();
        if (ui != null) {
            ui.access(() -> {
                VaadinSession.getCurrent().setAttribute("jwt_token", response.getToken());
                VaadinSession.getCurrent().setAttribute("refresh_token", response.getRefreshToken());
                VaadinSession.getCurrent().setAttribute("username", response.getUsername());
                VaadinSession.getCurrent().setAttribute("user_id", response.getUserId());

                Notification.show("Account created! Welcome, " + response.getUsername() + "!",
                                3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                UI.getCurrent().navigate("");
            });
        }
    }

    private void handleRegisterError(Throwable error) {
        log.error("Registration failed", error);

        UI ui = UI.getCurrent();
        if (ui != null) {
            ui.access(() -> {
                registerButton.setEnabled(true);
                registerButton.setText("Create Account");
                registerButton.getElement().setProperty("loading", false);
                registerButton.getStyle().set("opacity", "1");

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
     * Show error message with animation
     */
    private void showError(String message) {
        errorMessage.setText(message);
        errorMessage.setVisible(true);

        // Add shake animation
        errorMessage.getStyle()
                .set("animation", "shake 0.5s ease-in-out");
    }
}
