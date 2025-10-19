package com.shikshaspace.shikshaspace_ui.views;

import com.shikshaspace.shikshaspace_ui.dto.AuthResponse;
import com.shikshaspace.shikshaspace_ui.dto.LoginRequest;
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
import lombok.extern.slf4j.Slf4j;

/**
 * Login view with username/password authentication
 * Integrates with User Service for authentication
 */
@Slf4j
@Route("login")
@PageTitle("Login | ShikshaSpace")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

    private final UserServiceClient userServiceClient;
    
    private final TextField usernameField;
    private final PasswordField passwordField;
    private final Button loginButton;
    private final Div errorMessage;

    public LoginView(UserServiceClient userServiceClient) {
        this.userServiceClient = userServiceClient;
        
        // Page styling
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);
        getStyle()
                .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
                .set("padding", "20px");

        // Login card container
        VerticalLayout loginCard = new VerticalLayout();
        loginCard.setWidth("400px");
        loginCard.setPadding(true);
        loginCard.getStyle()
                .set("background", "white")
                .set("border-radius", "12px")
                .set("box-shadow", "0 8px 32px rgba(0,0,0,0.1)");

        // Logo/Title
        H1 logo = new H1("ShikshaSpace");
        logo.getStyle()
                .set("color", "#667eea")
                .set("margin", "0")
                .set("text-align", "center");

        H2 welcomeText = new H2("Welcome Back!");
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

        // Username field
        usernameField = new TextField("Username or Email");
        usernameField.setWidthFull();
        usernameField.setPlaceholder("Enter your username");
        usernameField.setRequired(true);
        usernameField.setClearButtonVisible(true);

        // Password field
        passwordField = new PasswordField("Password");
        passwordField.setWidthFull();
        passwordField.setPlaceholder("Enter your password");
        passwordField.setRequired(true);
        passwordField.setRevealButtonVisible(true);

        // Login button
        loginButton = new Button("Login");
        loginButton.setWidthFull();
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
        loginButton.getStyle().set("margin-top", "10px");
        loginButton.addClickListener(event -> handleLogin());

        // Enter key shortcut
        passwordField.addKeyPressListener(event -> {
            if (event.getKey().equals("Enter")) {
                handleLogin();
            }
        });

        // Register link - More prominent
        Div registerContainer = new Div();
        registerContainer.getStyle()
                .set("text-align", "center")
                .set("margin-top", "20px")
                .set("padding", "15px")
                .set("background", "#f5f7ff")
                .set("border-radius", "8px")
                .set("border", "1px solid #667eea");

        Paragraph registerText = new Paragraph();
        registerText.getStyle().set("margin", "0").set("color", "#333");
        registerText.setText("Don't have an account? ");

        RouterLink registerLink = new RouterLink("Register now", RegisterView.class);
        registerLink.getStyle()
                .set("color", "#667eea")
                .set("font-weight", "600")
                .set("text-decoration", "none")
                .set("font-size", "16px");

        registerText.add(registerLink);
        registerContainer.add(registerText);


        // Assemble login card
        loginCard.add(
                logo,
                welcomeText,
                errorMessage,
                usernameField,
                passwordField,
                loginButton,
                registerContainer  // Changed from registerText
        );


        add(loginCard);
    }

    /**
     * Handle login form submission
     */
    private void handleLogin() {
        // Clear previous errors
        errorMessage.setVisible(false);
        
        // Validate inputs
        String username = usernameField.getValue().trim();
        String password = passwordField.getValue();

        if (username.isEmpty()) {
            showError("Please enter username or email");
            usernameField.focus();
            return;
        }

        if (password.isEmpty()) {
            showError("Please enter password");
            passwordField.focus();
            return;
        }

        // Disable button during processing
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");

        // Call User Service
        LoginRequest request = new LoginRequest(username, password);
        
        userServiceClient.login(request)
                .subscribe(
                        this::handleLoginSuccess,
                        this::handleLoginError
                );
    }

    /**
     * Handle successful login
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
     * Show error message
     */
    private void showError(String message) {
        errorMessage.setText(message);
        errorMessage.setVisible(true);
    }
}
