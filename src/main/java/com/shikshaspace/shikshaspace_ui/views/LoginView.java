package com.shikshaspace.shikshaspace_ui.views;

import com.shikshaspace.shikshaspace_ui.dto.AuthResponse;
import com.shikshaspace.shikshaspace_ui.dto.LoginRequest;
import com.shikshaspace.shikshaspace_ui.security.SecurityUtils;
import com.shikshaspace.shikshaspace_ui.service.UserServiceClient;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;

/**
 * @author ShikshaSpace Engineering Team
 * @version 2.1 (Cleaned + All Fixes Applied)
 */
@Slf4j
@Route("login")
@PageTitle("Login | ShikshaSpace")
@AnonymousAllowed
public class LoginView extends VerticalLayout implements BeforeEnterObserver {

  private final UserServiceClient userServiceClient;
  private TextField usernameField;
  private PasswordField passwordField;
  private Button loginButton;
  private Div errorMessage;

  public LoginView(UserServiceClient userServiceClient) {
    this.userServiceClient = userServiceClient;

    UI.getCurrent()
        .getPage()
        .fetchCurrentURL(
            currentUrl -> {
              if (currentUrl.getQuery() != null && currentUrl.getQuery().contains("logout")) {
                // Remove logout parameter from URL without reload
                UI.getCurrent()
                    .getPage()
                    .executeJs("window.history.replaceState({}, '', '/login');");
              }
            });

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
        .set("overflow-y", "auto")
        .set("cursor", "default"); // ✅ FIX: Remove pointer cursor

    // Login card container
    VerticalLayout loginCard = createLoginCard();
    add(loginCard);
  }

  /** ✅ FIX: Prevent logged-in users from accessing login page */
  @Override
  public void beforeEnter(BeforeEnterEvent event) {
    if (SecurityUtils.isUserLoggedIn()) {
      event.forwardTo("home");
    }
  }

  /** Create responsive login card with premium design */
  private VerticalLayout createLoginCard() {
    VerticalLayout loginCard = new VerticalLayout();
    loginCard.setPadding(true);
    loginCard.setSpacing(true);
    loginCard.addClassName("login-card-responsive");

    // Responsive width using clamp for fluid sizing
    loginCard
        .getStyle()
        .set("width", "100%")
        .set("max-width", "clamp(320px, 90vw, 450px)")
        .set("background", "rgba(255, 255, 255, 0.98)")
        .set("border-radius", "clamp(12px, 2vw, 20px)")
        .set("box-shadow", "0 8px 32px rgba(0, 0, 0, 0.15), 0 20px 60px rgba(102, 126, 234, 0.2)")
        .set("padding", "clamp(24px, 5vw, 48px)")
        .set("backdrop-filter", "blur(10px)")
        .set("border", "1px solid rgba(255, 255, 255, 0.3)")
        .set("transition", "all 0.3s cubic-bezier(0.4, 0, 0.2, 1)");

    // Create all form elements
    H1 logo = createLogo();
    H2 welcomeText = createWelcomeText();
    errorMessage = createErrorMessage();
    usernameField = createUsernameField();
    passwordField = createPasswordField();
    loginButton = createLoginButton();
    Div registerContainer = createRegisterContainer();

    // Assemble login card
    loginCard.add(
        logo,
        welcomeText,
        errorMessage,
        usernameField,
        passwordField,
        loginButton,
        registerContainer);

    return loginCard;
  }

  /** Create responsive logo with solid color (visible on white background) */
  private H1 createLogo() {
    H1 logo = new H1("ShikshaSpace");
    logo.addClassNames(LumoUtility.Margin.NONE, LumoUtility.Margin.Bottom.MEDIUM, "login-logo");

    logo.getStyle()
        .set("color", "#667eea") // ✅ Solid color (visible on white)
        .set("font-size", "clamp(1.75rem, 6vw, 2.5rem)")
        .set("font-weight", "700")
        .set("text-align", "center")
        .set("letter-spacing", "0.5px")
        .set("margin-bottom", "8px")
        .set("text-shadow", "0 2px 4px rgba(102, 126, 234, 0.2)");

    return logo;
  }

  /** Create welcome text - "Welcome" (not "Welcome Back!") */
  private H2 createWelcomeText() {
    H2 welcomeText = new H2("Welcome");
    welcomeText.addClassNames(
        LumoUtility.Margin.Top.NONE, LumoUtility.Margin.Bottom.LARGE, "welcome-text");
    welcomeText
        .getStyle()
        .set("color", "#333")
        .set("font-weight", "400")
        .set("text-align", "center") // ✅ Centered
        .set("font-size", "clamp(1rem, 3vw, 1.5rem)")
        .set("margin-bottom", "clamp(16px, 4vw, 24px)");

    return welcomeText;
  }

  /** Create error message container with modern styling */
  private Div createErrorMessage() {
    Div error = new Div();
    error.setVisible(false);
    error.addClassName("error-message-box");
    error
        .getStyle()
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

  /** Create username field with premium styling */
  private TextField createUsernameField() {
    TextField field = new TextField("Username or Email");
    field.setWidthFull();
    field.setPlaceholder("Enter your username");
    field.setRequired(true);
    field.setClearButtonVisible(true);
    field.addClassName("premium-input-field");

    field
        .getStyle()
        .set("--vaadin-input-field-border-radius", "10px")
        .set("--vaadin-input-field-background", "linear-gradient(145deg, #ffffff, #f8f9ff)")
        .set("--vaadin-input-field-border-width", "2px")
        .set("--vaadin-input-field-border-color", "transparent")
        .set("--vaadin-input-field-hover-highlight", "#667eea")
        .set("margin-bottom", "clamp(12px, 2.5vw, 16px)")
        .set("font-size", "clamp(14px, 2.5vw, 16px)")
        .set("transition", "all 0.3s ease")
        .set("cursor", "text"); // ✅ Text cursor for input

    // Focus enhancement
    field.addFocusListener(
        e -> {
          field
              .getStyle()
              .set("box-shadow", "0 0 0 4px rgba(102, 126, 234, 0.1)")
              .set("transform", "translateY(-2px)");
        });

    field.addBlurListener(
        e -> {
          field.getStyle().set("box-shadow", "none").set("transform", "translateY(0)");
        });

    return field;
  }

  /** Create password field with premium styling */
  private PasswordField createPasswordField() {
    PasswordField field = new PasswordField("Password");
    field.setWidthFull();
    field.setPlaceholder("Enter your password");
    field.setRequired(true);
    field.setRevealButtonVisible(true);
    field.addClassName("premium-input-field");

    field
        .getStyle()
        .set("--vaadin-input-field-border-radius", "10px")
        .set("--vaadin-input-field-background", "linear-gradient(145deg, #ffffff, #f8f9ff)")
        .set("--vaadin-input-field-border-width", "2px")
        .set("--vaadin-input-field-border-color", "transparent")
        .set("--vaadin-input-field-hover-highlight", "#667eea")
        .set("margin-bottom", "clamp(12px, 2.5vw, 16px)")
        .set("font-size", "clamp(14px, 2.5vw, 16px)")
        .set("transition", "all 0.3s ease")
        .set("cursor", "text"); // ✅ Text cursor for input

    // Focus enhancement
    field.addFocusListener(
        e -> {
          field
              .getStyle()
              .set("box-shadow", "0 0 0 4px rgba(102, 126, 234, 0.1)")
              .set("transform", "translateY(-2px)");
        });

    field.addBlurListener(
        e -> {
          field.getStyle().set("box-shadow", "none").set("transform", "translateY(0)");
        });

    // Enter key shortcut
    field.addKeyPressListener(
        event -> {
          if ("Enter".equals(event.getKey())) {
            handleLogin();
          }
        });

    return field;
  }

  /** Create login button with modern premium design */
  private Button createLoginButton() {
    Button button = new Button("Login");
    button.setWidthFull();
    button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
    button.addClassName("premium-login-button");

    button
        .getStyle()
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
        .set("min-height", "44px")
        .set("text-align", "center")
        .set("display", "flex")
        .set("align-items", "center")
        .set("justify-content", "center");

    button.addClickListener(event -> handleLogin());
    return button;
  }

  /** Create register container (no border) */
  private Div createRegisterContainer() {
    Div registerContainer = new Div();
    registerContainer.addClassName("register-container");
    registerContainer
        .getStyle()
        .set("text-align", "center")
        .set("margin-top", "clamp(20px, 4vw, 28px)")
        .set("padding", "clamp(16px, 3vw, 20px)")
        .set("background", "linear-gradient(145deg, #f8f9ff, #ffffff)")
        .set("border-radius", "10px")
        .set("border", "none") // ✅ No border
        .set("transition", "all 0.3s ease");

    Paragraph registerText = new Paragraph();
    registerText.addClassName("register-text");
    registerText
        .getStyle()
        .set("margin", "0")
        .set("color", "#555")
        .set("font-size", "clamp(13px, 2.5vw, 15px)")
        .set("line-height", "1.6");

    registerText.setText("Don't have an account? ");

    RouterLink registerLink = new RouterLink("Register now", RegisterView.class);
    registerLink.addClassName("register-link");
    registerLink
        .getStyle()
        .set("color", "#667eea")
        .set("font-weight", "600")
        .set("text-decoration", "none")
        .set("font-size", "clamp(14px, 2.5vw, 16px)")
        .set("transition", "all 0.2s ease")
        .set("border-bottom", "2px solid transparent")
        .set("cursor", "pointer");

    registerText.add(registerLink);
    registerContainer.add(registerText);

    return registerContainer;
  }

  /** Handle login form submission ✅ ALL FIXES APPLIED */
  private void handleLogin() {
    String username = usernameField.getValue();
    String password = passwordField.getValue();

    if (username.isBlank() || password.isBlank()) {
      showError("Please enter both username and password");
      return;
    }

    loginButton.setEnabled(false);
    loginButton.setText("Logging in...");
    loginButton.getStyle().set("opacity", "0.7");
    errorMessage.setVisible(false);

    try {
      AuthResponse response =
          userServiceClient
              .login(new LoginRequest(username, password))
              .block(Duration.ofSeconds(10));

      if (response != null && response.getToken() != null) {
        SecurityUtils.authenticateUser(
            response.getUsername(), response.getUserId(), response.getToken(), response.getEmail());

        VaadinSession.getCurrent().setAttribute("refresh_token", response.getRefreshToken());

        // ✅ FIX: Show notification with proper auto-close
        Notification notification =
            Notification.show(
                "Welcome back, " + response.getUsername() + "!",
                3000, // 3 seconds
                Notification.Position.TOP_CENTER);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        // ✅ FIX: Navigate after short delay to ensure notification shows
        UI.getCurrent()
            .getPage()
            .executeJs("setTimeout(function() { window.location.href = '/'; }, 100);");

      } else {
        throw new RuntimeException("Invalid response from server");
      }

    } catch (Exception e) {
      log.error("Login failed for user: {}", username, e);

      loginButton.setEnabled(false);
      loginButton.setText("Login");
      loginButton.getStyle().set("opacity", "1");

      String errorMsg = "Login failed. Please try again.";
      if (e.getMessage() != null && e.getMessage().contains("401")) {
        errorMsg = "Invalid username or password. Please try again or register.";
        showInfoNotification("Don't have an account? Click Register below.");
      } else if (e.getMessage() != null && e.getMessage().contains("timeout")) {
        errorMsg = "Connection timeout. Check your internet connection.";
      }

      showError(errorMsg);
      passwordField.clear();
      passwordField.focus();
    }
  }

  /** Show error message with animation */
  private void showError(String message) {
    errorMessage.setText(message);
    errorMessage.setVisible(true);
    errorMessage.getStyle().set("animation", "shake 0.5s ease-in-out");
  }

  /** ✅ NEW: Show success notification with auto-dismiss */
  private void showSuccessNotification(String message) {
    Notification notification = new Notification();
    notification.setText(message);
    notification.setDuration(3000); // 3 seconds auto-dismiss
    notification.setPosition(Notification.Position.TOP_CENTER);
    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    notification.open();
  }

  /** ✅ NEW: Show info notification with auto-dismiss */
  private void showInfoNotification(String message) {
    Notification notification = new Notification();
    notification.setText(message);
    notification.setDuration(5000); // 5 seconds auto-dismiss
    notification.setPosition(Notification.Position.TOP_CENTER);
    notification.addThemeVariants(NotificationVariant.LUMO_PRIMARY);
    notification.open();
  }
}
