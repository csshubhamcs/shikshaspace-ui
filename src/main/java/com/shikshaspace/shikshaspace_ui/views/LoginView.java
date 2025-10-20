package com.shikshaspace.shikshaspace_ui.views;

import com.shikshaspace.shikshaspace_ui.dto.AuthResponse;
import com.shikshaspace.shikshaspace_ui.dto.LoginRequest;
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
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

/**
 * Production-grade responsive LoginView with modern premium design
 * Supports mobile (320px+), tablet (768px+), and desktop (1024px+)
 *
 * Features:
 * - Mobile-first responsive design
 * - Modern glassmorphism effects
 * - Smooth animations and transitions
 * - Touch-friendly buttons (min 44px height)
 * - Fluid typography using clamp()
 * - Premium gradient backgrounds
 * - Enhanced error handling UI
 *
 * @author ShikshaSpace Engineering Team
 * @version 2.0 (Responsive + Premium UI)
 */
@Slf4j
@Route("login")
@PageTitle("Login | ShikshaSpace")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    private final UserServiceClient userServiceClient;

    private  TextField usernameField;
    private  PasswordField passwordField;
    private  Button loginButton;
    private  Div errorMessage;

    public LoginView(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;

        // Full-viewport responsive container with gradient background
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        addClassName("login-view-container");

        // Modern gradient background with responsive padding
        getStyle()
                .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
                .set("padding", "clamp(16px, 4vw, 40px)")
                .set("min-height", "100vh")
                .set("position", "relative")
                .set("overflow-y", "auto");

        // Login card container with responsive width
        VerticalLayout loginCard = createLoginCard();

        add(loginCard);
    }

    /**
     * Create responsive login card with premium design
     */
    private VerticalLayout createLoginCard() {
        VerticalLayout loginCard = new VerticalLayout();
        loginCard.setPadding(true);
        loginCard.setSpacing(true);
        loginCard.addClassName("login-card-responsive");

        // Responsive width using clamp for fluid sizing
        loginCard.getStyle()
                .set("width", "100%")
                .set("max-width", "clamp(320px, 90vw, 450px)")
                .set("background", "rgba(255, 255, 255, 0.98)")
                .set("border-radius", "clamp(12px, 2vw, 20px)")
                .set("box-shadow", "0 8px 32px rgba(0, 0, 0, 0.15), 0 20px 60px rgba(102, 126, 234, 0.2)")
                .set("padding", "clamp(24px, 5vw, 48px)")
                .set("backdrop-filter", "blur(10px)")
                .set("border", "1px solid rgba(255, 255, 255, 0.3)")
                .set("transition", "all 0.3s cubic-bezier(0.4, 0, 0.2, 1)");

        // Logo with gradient text and fluid typography
        H1 logo = createLogo();

        // Welcome text with responsive sizing
        H2 welcomeText = createWelcomeText();

        // Error message container
        errorMessage = createErrorMessage();

        // Username field with modern styling
        usernameField = createUsernameField();

        // Password field with modern styling
        passwordField = createPasswordField();

        // Login button with premium design
        loginButton = createLoginButton();

        // Register link container
        Div registerContainer = createRegisterContainer();

        // Assemble login card
        loginCard.add(
                logo,
                welcomeText,
                errorMessage,
                usernameField,
                passwordField,
                loginButton,
                registerContainer
        );

        return loginCard;
    }

    /**
     * Create responsive logo with gradient text effect
     */
    private H1 createLogo() {
        H1 logo = new H1("ShikshaSpace");
        logo.addClassNames(
                LumoUtility.Margin.NONE,
                LumoUtility.Margin.Bottom.MEDIUM,
                "login-logo"
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
                .set("margin-bottom", "8px");

        return logo;
    }

    /**
     * Create welcome text with responsive sizing
     */
    private H2 createWelcomeText() {
        H2 welcomeText = new H2("Welcome Back!");
        welcomeText.addClassNames(
                LumoUtility.Margin.Top.NONE,
                LumoUtility.Margin.Bottom.LARGE,
                "welcome-text"
        );
        welcomeText.getStyle()
                .set("color", "#333")
                .set("font-weight", "400")
                .set("text-align", "center")
                .set("font-size", "clamp(1rem, 3vw, 1.5rem)")
                .set("margin-bottom", "clamp(16px, 4vw, 24px)");

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
                .set("padding", "clamp(12px, 3vw, 16px)")
                .set("border-radius", "10px")
                .set("margin-bottom", "20px")
                .set("text-align", "center")
                .set("font-size", "clamp(13px, 2.5vw, 14px)")
                .set("font-weight", "500")
                .set("box-shadow", "0 4px 12px rgba(244, 67, 54, 0.3)")
                .set("animation", "slideDown 0.3s ease-out")
                .set("border", "1px solid rgba(255, 255, 255, 0.2)");

        return error;
    }

    /**
     * Create username field with premium styling
     */
    private TextField createUsernameField() {
        TextField field = new TextField("Username or Email");
        field.setWidthFull();
        field.setPlaceholder("Enter your username");
        field.setRequired(true);
        field.setClearButtonVisible(true);
        field.addClassName("premium-input-field");

        // Modern input styling with responsive sizing
        field.getStyle()
                .set("--vaadin-input-field-border-radius", "10px")
                .set("--vaadin-input-field-background", "linear-gradient(145deg, #ffffff, #f8f9ff)")
                .set("--vaadin-input-field-border-width", "2px")
                .set("--vaadin-input-field-border-color", "transparent")
                .set("--vaadin-input-field-hover-highlight", "#667eea")
                .set("margin-bottom", "clamp(12px, 2.5vw, 16px)")
                .set("font-size", "clamp(14px, 2.5vw, 16px)")
                .set("transition", "all 0.3s ease");

        // Add focus enhancement
        field.addFocusListener(e -> {
            field.getStyle()
                    .set("box-shadow", "0 0 0 4px rgba(102, 126, 234, 0.1)")
                    .set("transform", "translateY(-2px)");
        });

        field.addBlurListener(e -> {
            field.getStyle()
                    .set("box-shadow", "none")
                    .set("transform", "translateY(0)");
        });

        return field;
    }

    /**
     * Create password field with premium styling
     */
    private PasswordField createPasswordField() {
        PasswordField field = new PasswordField("Password");
        field.setWidthFull();
        field.setPlaceholder("Enter your password");
        field.setRequired(true);
        field.setRevealButtonVisible(true);
        field.addClassName("premium-input-field");

        // Modern input styling
        field.getStyle()
                .set("--vaadin-input-field-border-radius", "10px")
                .set("--vaadin-input-field-background", "linear-gradient(145deg, #ffffff, #f8f9ff)")
                .set("--vaadin-input-field-border-width", "2px")
                .set("--vaadin-input-field-border-color", "transparent")
                .set("--vaadin-input-field-hover-highlight", "#667eea")
                .set("margin-bottom", "clamp(12px, 2.5vw, 16px)")
                .set("font-size", "clamp(14px, 2.5vw, 16px)")
                .set("transition", "all 0.3s ease");

        // Add focus enhancement
        field.addFocusListener(e -> {
            field.getStyle()
                    .set("box-shadow", "0 0 0 4px rgba(102, 126, 234, 0.1)")
                    .set("transform", "translateY(-2px)");
        });

        field.addBlurListener(e -> {
            field.getStyle()
                    .set("box-shadow", "none")
                    .set("transform", "translateY(0)");
        });

        // Enter key shortcut for login
        field.addKeyPressListener(event -> {
            if (event.getKey().equals("Enter")) {
                handleLogin();
            }
        });

        return field;
    }

    /**
     * Create login button with modern premium design and animations
     */
    private Button createLoginButton() {
        Button button = new Button("Login");
        button.setWidthFull();
        button.addThemeVariants(
                ButtonVariant.LUMO_PRIMARY,
                ButtonVariant.LUMO_LARGE
        );
        button.addClassName("premium-login-button");

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
                .set("min-height", "44px"); // Touch-friendly

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

        button.addClickListener(event -> handleLogin());

        return button;
    }

    /**
     * Create register container with modern styling
     */
    private Div createRegisterContainer() {
        Div registerContainer = new Div();
        registerContainer.addClassName("register-container");
        registerContainer.getStyle()
                .set("text-align", "center")
                .set("margin-top", "clamp(20px, 4vw, 28px)")
                .set("padding", "clamp(16px, 3vw, 20px)")
                .set("background", "linear-gradient(145deg, #f8f9ff, #ffffff)")
                .set("border-radius", "10px")
                .set("border", "2px solid rgba(102, 126, 234, 0.15)")
                .set("transition", "all 0.3s ease");

        Paragraph registerText = new Paragraph();
        registerText.addClassName("register-text");
        registerText.getStyle()
                .set("margin", "0")
                .set("color", "#555")
                .set("font-size", "clamp(13px, 2.5vw, 15px)")
                .set("line-height", "1.6");

        registerText.setText("Don't have an account? ");

        RouterLink registerLink = new RouterLink("Register now", RegisterView.class);
        registerLink.addClassName("register-link");
        registerLink.getStyle()
                .set("color", "#667eea")
                .set("font-weight", "600")
                .set("text-decoration", "none")
                .set("font-size", "clamp(14px, 2.5vw, 16px)")
                .set("transition", "all 0.2s ease")
                .set("border-bottom", "2px solid transparent");

        // Hover effect for register link
        registerLink.getElement().addEventListener("mouseenter", e -> {
            registerLink.getStyle()
                    .set("color", "#764ba2")
                    .set("border-bottom-color", "#764ba2");
        });

        registerLink.getElement().addEventListener("mouseleave", e -> {
            registerLink.getStyle()
                    .set("color", "#667eea")
                    .set("border-bottom-color", "transparent");
        });

        registerText.add(registerLink);
        registerContainer.add(registerText);

        return registerContainer;
    }

    /**
     * Handle login form submission
     * ALL ORIGINAL LOGIC PRESERVED
     */
    private void handleLogin() {
        String username = usernameField.getValue();
        String password = passwordField.getValue();

        if (username.isBlank() || password.isBlank()) {
            showError("Please enter both username and password");
            return;
        }

        // Show loading state with modern animation
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");
        loginButton.getStyle().set("opacity", "0.7");
        errorMessage.setVisible(false);

        try {
            // BLOCKING CALL - Appropriate for Vaadin UI
            AuthResponse response = userServiceClient
                    .login(new LoginRequest(username, password))
                    .block(Duration.ofSeconds(10));

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
                Notification.show("Welcome back, " + response.getUsername() + "!",
                                3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                // Navigate to home
                UI.getCurrent().navigate("");

            } else {
                throw new RuntimeException("Invalid response from server");
            }

        } catch (Exception e) {
            log.error("Login failed for user: {}", username, e);

            // Re-enable button
            loginButton.setEnabled(true);
            loginButton.setText("Login");
            loginButton.getStyle().set("opacity", "1");

            // Show error
            String errorMsg = "Login failed. Please try again.";
            if (e.getMessage() != null && e.getMessage().contains("401")) {
                errorMsg = "Invalid username or password. Please try again or register.";

                Notification.show(
                        "Don't have an account? Click Register below.",
                        5000,
                        Notification.Position.TOP_CENTER
                ).addThemeVariants(NotificationVariant.LUMO_PRIMARY);
            } else if (e.getMessage() != null && e.getMessage().contains("timeout")) {
                errorMsg = "Connection timeout. Check your internet connection.";
            }

            showError(errorMsg);
            passwordField.clear();
            passwordField.focus();
        }
    }

    /**
     * Handle successful login
     * PRESERVED FROM ORIGINAL
     */
    private void handleLoginSuccess(AuthResponse response) {
        log.info("Login successful for user: {}", response.getUsername());

        UI ui = UI.getCurrent();
        if (ui != null) {
            ui.access(() -> {
                // Store tokens in session
                VaadinSession.getCurrent().setAttribute("jwt_token", response.getToken());
                VaadinSession.getCurrent().setAttribute("refresh_token", response.getRefreshToken());
                VaadinSession.getCurrent().setAttribute("username", response.getUsername());
                VaadinSession.getCurrent().setAttribute("user_id", response.getUserId());

                // Show success notification
                Notification.show("Welcome back, " + response.getUsername() + "!",
                                3000, Notification.Position.TOP_CENTER)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

                // Redirect to home page
                UI.getCurrent().navigate("");
            });
        }
    }

    /**
     * Handle login error
     * PRESERVED FROM ORIGINAL
     */
    private void handleLoginError(Throwable error) {
        log.error("Login failed", error);

        // CRITICAL: Must use UI.access() when called from reactive thread
        UI ui = UI.getCurrent();
        if (ui != null) {
            ui.access(() -> {
                // Re-enable button
                loginButton.setEnabled(true);
                loginButton.setText("Login");
                loginButton.getElement().setProperty("loading", false);
                loginButton.getStyle().set("opacity", "1");

                // Show detailed error message
                String errorMsg = "Login failed. Please try again.";

                if (error.getMessage() != null) {
                    if (error.getMessage().contains("401")) {
                        errorMsg = "Account not found. Please register first or contact admin.";

                        // Show register link notification
                        Notification notification = Notification.show(
                                "Don't have an account? Click Register to create one.",
                                5000,
                                Notification.Position.TOP_CENTER
                        );
                        notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);

                    } else if (error.getMessage().contains("timeout")) {
                        errorMsg = "Connection timeout. Please check your internet and try again.";
                    } else if (error.getMessage().contains("400")) {
                        errorMsg = "Invalid request. Please check your credentials.";
                    } else {
                        errorMsg = "Login failed. Please check your connection.";
                    }
                }

                showError(errorMsg);
                passwordField.clear();
                passwordField.focus();
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
