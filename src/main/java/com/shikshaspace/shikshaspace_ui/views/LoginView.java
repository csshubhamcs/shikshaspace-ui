package com.shikshaspace.shikshaspace_ui.views;

import com.shikshaspace.shikshaspace_ui.dto.AuthResponse;
import com.shikshaspace.shikshaspace_ui.dto.GoogleSignInRequest;
import com.shikshaspace.shikshaspace_ui.dto.LoginRequest;
import com.shikshaspace.shikshaspace_ui.security.SecurityUtils;
import com.shikshaspace.shikshaspace_ui.service.GoogleSignInService;
import com.shikshaspace.shikshaspace_ui.service.UserServiceClient;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.extern.slf4j.Slf4j;
import reactor.core.scheduler.Schedulers;

/** Production-grade standalone login view. */
@Slf4j
@Route(value = "login") // NO layout - standalone page
@PageTitle("Login - ShikshaSpace")
@AnonymousAllowed
public class LoginView extends VerticalLayout {

  private final UserServiceClient userServiceClient;
  private final GoogleSignInService googleSignInService;

  private TextField usernameField;
  private PasswordField passwordField;
  private Button loginButton;
  private Div errorMessage;
  private Div googleButtonContainer;

  public LoginView(UserServiceClient userServiceClient, GoogleSignInService googleSignInService) {
    this.userServiceClient = userServiceClient;
    this.googleSignInService = googleSignInService;

    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);
    getStyle()
        .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
        .set("min-height", "100vh");

    add(createLoginCard());
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    log.info("🔵 LoginView attached, initializing Google Sign-In");

    // Wait a bit for DOM to be ready, then initialize Google
    UI ui = attachEvent.getUI();
    ui.getPage().executeJs("console.log('LoginView attached to DOM');");

    // Schedule Google initialization after a short delay
    new Thread(
            () -> {
              try {
                Thread.sleep(500); // Wait 500ms for DOM
                ui.access(
                    () -> {
                      try {
                        googleSignInService.initializeGoogleSignIn(this::handleGoogleCallback);
                        log.info("✅ Google Sign-In initialized successfully");

                        // Verify button container exists
                        ui.getPage()
                            .executeJs(
                                "console.log('Google button container:', document.getElementById('google-signin-button'));");
                      } catch (Exception e) {
                        log.error("❌ Failed to initialize Google Sign-In: {}", e.getMessage(), e);
                      }
                    });
              } catch (InterruptedException e) {
                log.error("Thread interrupted: {}", e.getMessage());
              }
            })
        .start();
  }

  private Div createLoginCard() {
    Div card = new Div();
    card.getStyle()
        .set("background", "white")
        .set("border-radius", "16px")
        .set("box-shadow", "0 10px 40px rgba(0,0,0,0.15)")
        .set("padding", "48px")
        .set("max-width", "420px")
        .set("width", "100%");

    // Logo
    H1 logo = new H1("ShikshaSpace");
    logo.getStyle()
        .set("text-align", "center")
        .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
        .set("-webkit-background-clip", "text")
        .set("-webkit-text-fill-color", "transparent")
        .set("margin", "0 0 8px 0")
        .set("font-size", "32px");

    // Title
    H2 title = new H2("Sign in to your account");
    title
        .getStyle()
        .set("text-align", "center")
        .set("color", "#333")
        .set("font-weight", "500")
        .set("font-size", "20px")
        .set("margin", "0 0 32px 0");

    // Username field
    usernameField = new TextField("Username");
    usernameField.setPlaceholder("Enter your username");
    usernameField.setClearButtonVisible(true);
    usernameField.setWidthFull();
    usernameField.getStyle().set("margin-bottom", "16px");

    // Password field
    passwordField = new PasswordField("Password");
    passwordField.setPlaceholder("Enter your password");
    passwordField.setClearButtonVisible(true);
    passwordField.setWidthFull();
    passwordField.getStyle().set("margin-bottom", "8px");

    // Error message
    errorMessage = new Div();
    errorMessage
        .getStyle()
        .set("color", "#dc2626")
        .set("background", "#fee2e2")
        .set("padding", "12px")
        .set("border-radius", "8px")
        .set("margin-bottom", "16px")
        .set("font-size", "14px")
        .set("display", "none");

    // Login button
    loginButton = new Button("Sign In", e -> handleLogin());
    loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    loginButton.setWidthFull();
    loginButton
        .getStyle()
        .set("margin-bottom", "24px")
        .set("height", "48px")
        .set("font-size", "16px")
        .set("font-weight", "500");

    // Divider
    Div divider = new Div();
    divider
        .getStyle()
        .set("display", "flex")
        .set("align-items", "center")
        .set("margin", "24px 0")
        .set("color", "#999");

    Div line1 = new Div();
    line1.getStyle().set("flex", "1").set("height", "1px").set("background", "#ddd");

    Span orText = new Span("OR");
    orText.getStyle().set("padding", "0 16px").set("font-size", "14px");

    Div line2 = new Div();
    line2.getStyle().set("flex", "1").set("height", "1px").set("background", "#ddd");

    divider.add(line1, orText, line2);

    // Google button container
    googleButtonContainer = new Div();
    googleButtonContainer.setId("google-signin-button");
    googleButtonContainer.setWidthFull();
    googleButtonContainer
        .getStyle()
        .set("margin-bottom", "24px")
        .set("display", "flex")
        .set("justify-content", "center");

    // Register link
    Div registerLink = new Div();
    registerLink
        .getStyle()
        .set("text-align", "center")
        .set("color", "#666")
        .set("font-size", "14px");

    Span text = new Span("Don't have an account? ");
    Anchor link = new Anchor("/register", "Sign Up");
    link.getStyle()
        .set("color", "#667eea")
        .set("font-weight", "500")
        .set("text-decoration", "none");
    registerLink.add(text, link);

    card.add(
        logo,
        title,
        usernameField,
        passwordField,
        errorMessage,
        loginButton,
        divider,
        googleButtonContainer,
        registerLink);

    return card;
  }

  private void handleLogin() {
    String username = usernameField.getValue().trim();
    String password = passwordField.getValue();

    log.info("🔵 Login attempt for username: {}", username);

    if (username.isEmpty() || password.isEmpty()) {
      showError("Please fill in all fields");
      return;
    }

    // Disable button and show loading
    loginButton.setEnabled(false);
    loginButton.setText("Signing in...");
    hideError();

    LoginRequest request = new LoginRequest(username, password);

    // CRITICAL: Subscribe on different scheduler to avoid blocking
    userServiceClient
        .login(request)
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe(
            response -> getUI().ifPresent(ui -> ui.access(() -> handleSuccessfulLogin(response))),
            error -> getUI().ifPresent(ui -> ui.access(() -> handleLoginError(error))));
  }

  private void handleSuccessfulLogin(AuthResponse response) {
    log.info("✅ Login successful for user: {}", response.getUsername());

    // Authenticate user
    SecurityUtils.authenticateUser(
        response.getUsername(), response.getUserId(), response.getToken(), response.getEmail());

    // Verify authentication
    boolean isAuth = SecurityUtils.isAuthenticated();
    log.info("✅ Authentication status after login: {}", isAuth);

    if (!isAuth) {
      log.error("❌ Authentication failed to set properly!");
      showError("Authentication failed. Please try again.");
      loginButton.setEnabled(true);
      loginButton.setText("Sign In");
      return;
    }

    // Show success notification
    Notification notification =
        Notification.show(
            "Welcome, " + response.getUsername() + "!", 3000, Notification.Position.TOP_CENTER);
    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);

    // Navigate to home
    log.info("🔵 Navigating to home page");
    UI.getCurrent().navigate("");
    UI.getCurrent().getPage().reload();
  }

  private void handleLoginError(Throwable error) {
    log.error("❌ Login failed: {}", error.getMessage(), error);

    loginButton.setEnabled(true);
    loginButton.setText("Sign In");

    String errorMsg = "Invalid username or password";
    if (error.getMessage() != null) {
      if (error.getMessage().contains("timeout")) {
        errorMsg = "Connection timeout. Please check your internet connection.";
      } else if (error.getMessage().contains("Connection refused")) {
        errorMsg = "Cannot connect to server. Please try again later.";
      } else if (error.getMessage().contains("401")) {
        errorMsg = "Invalid username or password.";
      }
    }

    showError(errorMsg);
    passwordField.clear();
    passwordField.focus();
  }

  @ClientCallable
  public void handleGoogleCallback(String idToken) {
    log.info("🔵 Google callback received with token");

    GoogleSignInRequest request = new GoogleSignInRequest(idToken);

    userServiceClient
        .googleSignIn(request)
        .subscribeOn(Schedulers.boundedElastic())
        .subscribe(
            response -> getUI().ifPresent(ui -> ui.access(() -> handleSuccessfulLogin(response))),
            error -> getUI().ifPresent(ui -> ui.access(() -> handleGoogleError(error))));
  }

  private void handleGoogleError(Throwable error) {
    log.error("❌ Google Sign-In failed: {}", error.getMessage(), error);
    showError("Google Sign-In failed. Please try again.");
  }

  private void showError(String message) {
    errorMessage.setText(message);
    errorMessage.getStyle().set("display", "block");
  }

  private void hideError() {
    errorMessage.getStyle().set("display", "none");
  }
}
