package com.shikshaspace.shikshaspace_ui.views;

import com.shikshaspace.shikshaspace_ui.components.MainLayout;
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

@Slf4j
@Route(value = "login", layout = MainLayout.class)
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
    addClassName("login-view-container");

    add(createLoginCard());
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    super.onAttach(attachEvent);

    // Initialize Google Sign-In AFTER component is attached to DOM
    UI.getCurrent()
        .access(
            () -> {
              try {
                googleSignInService.initializeGoogleSignIn(this::handleGoogleCallback);
                log.info("✅ Google Sign-In button initialized");
              } catch (Exception e) {
                log.error("❌ Failed to initialize Google Sign-In: {}", e.getMessage());
              }
            });
  }

  private Div createLoginCard() {
    Div card = new Div();
    card.addClassName("login-card-responsive");

    H2 title = new H2("Sign in to your account");
    title.addClassName("gradient-text");

    usernameField = new TextField("Username");
    usernameField.setPlaceholder("Enter your username");
    usernameField.setClearButtonVisible(true);
    usernameField.setWidthFull();

    passwordField = new PasswordField("Password");
    passwordField.setPlaceholder("Enter your password");
    passwordField.setClearButtonVisible(true);
    passwordField.setWidthFull();

    errorMessage = new Div();
    errorMessage.addClassName("error-message");
    errorMessage.setVisible(false);

    loginButton = new Button("Sign In", e -> handleLogin());
    loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    loginButton.addClassName("premium-login-button");
    loginButton.setWidthFull();

    Div divider = new Div(new Span("OR"));
    divider.addClassName("divider-container");

    // Google button container
    googleButtonContainer = new Div();
    googleButtonContainer.setId("google-signin-button");
    googleButtonContainer.addClassName("google-button-container");
    googleButtonContainer.setWidthFull();

    Div registerLink = createRegisterLink();

    card.add(
        title,
        usernameField,
        passwordField,
        errorMessage,
        loginButton,
        divider,
        googleButtonContainer, // This is where Google button will render
        registerLink);

    return card;
  }

  private Div createRegisterLink() {
    Div registerContainer = new Div();
    registerContainer.addClassName("register-link-container");

    Span text = new Span("Don't have an account? ");
    Anchor registerLink = new Anchor("/register", "Sign Up");
    registerLink.addClassName("register-link");

    registerContainer.add(text, registerLink);
    return registerContainer;
  }

  private void handleLogin() {
    String username = usernameField.getValue().trim();
    String password = passwordField.getValue();

    if (username.isEmpty() || password.isEmpty()) {
      showError("Please fill in all fields");
      return;
    }

    loginButton.setEnabled(false);
    loginButton.setText("Signing in...");
    hideError();

    LoginRequest request = new LoginRequest(username, password);

    userServiceClient.login(request).subscribe(this::handleSuccessfulLogin, this::handleLoginError);
  }

  private void handleSuccessfulLogin(AuthResponse response) {
    log.info("✅ Login successful for user: {}", response.getUsername());

    // Authenticate user
    SecurityUtils.authenticateUser(
        response.getUsername(), response.getUserId(), response.getToken(), response.getEmail());

    // Verify authentication
    log.info("✅ Authentication status: {}", SecurityUtils.isAuthenticated());

    // Show notification
    Notification.show(
            "Welcome, " + response.getUsername() + "!", 3000, Notification.Position.TOP_CENTER)
        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

    // Navigate to home
    UI.getCurrent().navigate("");
    UI.getCurrent().getPage().reload();
  }

  private void handleLoginError(Throwable error) {
    log.error("❌ Login failed: {}", error.getMessage());

    loginButton.setEnabled(true);
    loginButton.setText("Sign In");

    showError("Invalid username or password");
    passwordField.clear();
    passwordField.focus();
  }

  @ClientCallable
  public void handleGoogleCallback(String idToken) {
    log.info("🔵 Google callback received with token");

    GoogleSignInRequest request = new GoogleSignInRequest(idToken);

    userServiceClient
        .googleSignIn(request)
        .subscribe(this::handleSuccessfulLogin, this::handleGoogleError);
  }

  private void handleGoogleError(Throwable error) {
    log.error("❌ Google Sign-In failed: {}", error.getMessage());
    showError("Google Sign-In failed. Please try again.");
  }

  private void showError(String message) {
    errorMessage.setText(message);
    errorMessage.setVisible(true);
  }

  private void hideError() {
    errorMessage.setVisible(false);
  }
}
