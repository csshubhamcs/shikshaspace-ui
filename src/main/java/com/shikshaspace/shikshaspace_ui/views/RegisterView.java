package com.shikshaspace.shikshaspace_ui.views;

import com.shikshaspace.shikshaspace_ui.components.MainLayout;
import com.shikshaspace.shikshaspace_ui.dto.AuthResponse;
import com.shikshaspace.shikshaspace_ui.dto.RegisterRequest;
import com.shikshaspace.shikshaspace_ui.security.SecurityUtils;
import com.shikshaspace.shikshaspace_ui.service.UserServiceClient;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import lombok.extern.slf4j.Slf4j;

/** Production-grade registration view with validation. */
@Slf4j
@Route(value = "register", layout = MainLayout.class)
@PageTitle("Register - ShikshaSpace")
@AnonymousAllowed
public class RegisterView extends VerticalLayout {

  private final UserServiceClient userServiceClient;

  // REMOVED 'final' keyword from all fields
  private TextField usernameField;
  private EmailField emailField;
  private PasswordField passwordField;
  private PasswordField confirmPasswordField;
  private TextField firstNameField;
  private TextField lastNameField;
  private Button registerButton;
  private Div errorMessage;

  public RegisterView(UserServiceClient userServiceClient) {
    this.userServiceClient = userServiceClient;

    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);
    addClassName("register-view-container");

    add(createRegisterCard());
  }

  private Div createRegisterCard() {
    Div card = new Div();
    card.addClassName("register-card-responsive");

    H2 title = new H2("Create Account");
    title.addClassName("gradient-text");

    Paragraph subtitle = new Paragraph("Join ShikshaSpace today");
    subtitle.addClassName("register-subtitle");

    usernameField = new TextField("Username");
    usernameField.setPlaceholder("Choose a username");
    usernameField.setRequiredIndicatorVisible(true);
    usernameField.setClearButtonVisible(true);
    usernameField.setWidthFull();

    emailField = new EmailField("Email");
    emailField.setPlaceholder("your.email@example.com");
    emailField.setRequiredIndicatorVisible(true);
    emailField.setClearButtonVisible(true);
    emailField.setWidthFull();

    firstNameField = new TextField("First Name");
    firstNameField.setPlaceholder("Your first name");
    firstNameField.setClearButtonVisible(true);
    firstNameField.setWidthFull();

    lastNameField = new TextField("Last Name");
    lastNameField.setPlaceholder("Your last name");
    lastNameField.setClearButtonVisible(true);
    lastNameField.setWidthFull();

    passwordField = new PasswordField("Password");
    passwordField.setPlaceholder("Create a password");
    passwordField.setRequiredIndicatorVisible(true);
    passwordField.setClearButtonVisible(true);
    passwordField.setWidthFull();

    confirmPasswordField = new PasswordField("Confirm Password");
    confirmPasswordField.setPlaceholder("Confirm your password");
    confirmPasswordField.setRequiredIndicatorVisible(true);
    confirmPasswordField.setClearButtonVisible(true);
    confirmPasswordField.setWidthFull();

    errorMessage = new Div();
    errorMessage.addClassName("error-message");
    errorMessage.setVisible(false);

    registerButton = new Button("Create Account", e -> handleRegister());
    registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    registerButton.addClassName("premium-register-button");
    registerButton.setWidthFull();

    Div loginLink = createLoginLink();

    card.add(
        title,
        subtitle,
        usernameField,
        emailField,
        firstNameField,
        lastNameField,
        passwordField,
        confirmPasswordField,
        errorMessage,
        registerButton,
        loginLink);

    return card;
  }

  private Div createLoginLink() {
    Div loginContainer = new Div();
    loginContainer.addClassName("login-link-container");

    Span text = new Span("Already have an account? ");
    Anchor loginLink = new Anchor("/login", "Sign In");
    loginLink.addClassName("login-link");

    loginContainer.add(text, loginLink);
    return loginContainer;
  }

  private void handleRegister() {
    String username = usernameField.getValue().trim();
    String email = emailField.getValue().trim();
    String firstName = firstNameField.getValue().trim();
    String lastName = lastNameField.getValue().trim();
    String password = passwordField.getValue();
    String confirmPassword = confirmPasswordField.getValue();

    // Validation
    if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
      showError("Please fill in all required fields");
      return;
    }

    if (!password.equals(confirmPassword)) {
      showError("Passwords do not match");
      return;
    }

    if (password.length() < 6) {
      showError("Password must be at least 6 characters");
      return;
    }

    registerButton.setEnabled(false);
    registerButton.setText("Creating account...");

    RegisterRequest request =
        RegisterRequest.builder()
            .username(username)
            .email(email)
            .password(password)
            .firstName(firstName)
            .lastName(lastName)
            .build();

    log.info("🔵 Attempting registration for: {}", username);

    userServiceClient
        .register(request)
        .subscribe(this::handleSuccessfulRegistration, this::handleRegistrationError);
  }

  private void handleSuccessfulRegistration(AuthResponse response) {
    log.info("✅ Registration successful for: {}", response.getUsername());

    // Auto-login after registration
    SecurityUtils.authenticateUser(
        response.getUsername(), response.getUserId(), response.getToken(), response.getEmail());

    log.info("✅ Auto-login successful after registration");

    Notification.show(
            "Welcome to ShikshaSpace, " + response.getUsername() + "!",
            3000,
            Notification.Position.TOP_CENTER)
        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

    UI.getCurrent().navigate("");
    UI.getCurrent().getPage().reload();
  }

  private void handleRegistrationError(Throwable error) {
    log.error("❌ Registration failed: {}", error.getMessage());

    registerButton.setEnabled(true);
    registerButton.setText("Create Account");

    String errorMsg = "Registration failed. Please try again.";
    if (error.getMessage().contains("already exists")) {
      errorMsg = "Username or email already exists";
    } else if (error.getMessage().contains("timeout")) {
      errorMsg = "Connection timeout. Please try again.";
    }

    showError(errorMsg);
  }

  private void showError(String message) {
    errorMessage.setText(message);
    errorMessage.setVisible(true);
    errorMessage.getStyle().set("animation", "shake 0.5s ease-in-out");
  }
}
