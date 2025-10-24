package com.shikshaspace.shikshaspace_ui.views;

import com.shikshaspace.shikshaspace_ui.config.OAuth2Config;
import com.shikshaspace.shikshaspace_ui.dto.AuthResponse;
import com.shikshaspace.shikshaspace_ui.dto.LoginRequest;
import com.shikshaspace.shikshaspace_ui.security.SecurityUtils;
import com.shikshaspace.shikshaspace_ui.service.GoogleSignInService;
import com.shikshaspace.shikshaspace_ui.service.UserServiceClient;
import com.vaadin.flow.component.ClientCallable;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.JavaScript;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;

/**
 * Production-grade responsive LoginView with Google Sign-In. Features mobile-first design and
 * OAuth2 integration.
 */
@Slf4j
@Route("login")
@PageTitle("Login - ShikshaSpace")
@AnonymousAllowed
@JavaScript("./google-signin.js")
public class LoginView extends VerticalLayout {

  private final UserServiceClient userServiceClient;
  private final GoogleSignInService googleSignInService;
  private final OAuth2Config oAuth2Config;

  // Removed 'final' - initialized in helper methods
  private TextField usernameField;
  private PasswordField passwordField;
  private Button loginButton;
  private Div errorMessage;
  private Div googleButtonContainer;

  public LoginView(
      UserServiceClient userServiceClient,
      GoogleSignInService googleSignInService,
      OAuth2Config oAuth2Config) {
    this.userServiceClient = userServiceClient;
    this.googleSignInService = googleSignInService;
    this.oAuth2Config = oAuth2Config;

    setSizeFull();
    setAlignItems(Alignment.CENTER);
    setJustifyContentMode(JustifyContentMode.CENTER);
    addClassName("login-view-container");

    getStyle()
        .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
        .set("padding", "clamp(16px, 4vw, 40px)")
        .set("min-height", "100vh");

    VerticalLayout loginCard = createLoginCard();
    add(loginCard);
  }

  private VerticalLayout createLoginCard() {
    VerticalLayout loginCard = new VerticalLayout();
    loginCard.setPadding(true);
    loginCard.setSpacing(true);
    loginCard.addClassName("login-card-responsive");

    loginCard
        .getStyle()
        .set("width", "100%")
        .set("max-width", "clamp(320px, 90vw, 450px)")
        .set("background", "rgba(255, 255, 255, 0.98)")
        .set("border-radius", "clamp(12px, 2vw, 20px)")
        .set("box-shadow", "0 8px 32px rgba(0, 0, 0, 0.15)")
        .set("padding", "clamp(24px, 5vw, 48px)");

    H1 logo = createLogo();
    H2 welcomeText = createWelcomeText();
    errorMessage = createErrorMessage();
    usernameField = createUsernameField();
    passwordField = createPasswordField();
    loginButton = createLoginButton();
    Div divider = createDivider();
    googleButtonContainer = createGoogleSignInButton();
    Div registerContainer = createRegisterContainer();

    loginCard.add(
        logo,
        welcomeText,
        errorMessage,
        usernameField,
        passwordField,
        loginButton,
        divider,
        googleButtonContainer,
        registerContainer);

    return loginCard;
  }

  private Div createDivider() {
    Div dividerContainer = new Div();
    dividerContainer.addClassName("login-divider");
    dividerContainer
        .getStyle()
        .set("display", "flex")
        .set("align-items", "center")
        .set("margin", "clamp(16px, 3vw, 24px) 0")
        .set("color", "#999")
        .set("font-size", "14px");

    Hr leftLine = new Hr();
    leftLine.getStyle().set("flex", "1").set("border", "none").set("border-top", "1px solid #ddd");

    Span orText = new Span("OR");
    orText.getStyle().set("padding", "0 12px").set("font-weight", "500");

    Hr rightLine = new Hr();
    rightLine.getStyle().set("flex", "1").set("border", "none").set("border-top", "1px solid #ddd");

    dividerContainer.add(leftLine, orText, rightLine);
    return dividerContainer;
  }

  private Div createGoogleSignInButton() {
    Div container = new Div();
    container.setId("google-signin-button");
    container.addClassName("google-signin-container");
    container
        .getStyle()
        .set("width", "100%")
        .set("display", "flex")
        .set("justify-content", "center")
        .set("margin-bottom", "clamp(12px, 2vw, 16px)");

    container.addAttachListener(
        event -> {
          String clientId = oAuth2Config.getGoogleClientId();

          UI ui = event.getUI();
          ui.getPage()
              .executeJs(
                  "window.initGoogleSignIn($0, (response) => {"
                      + "  $1.$server.handleGoogleCallback(response.credential);"
                      + "});",
                  clientId,
                  getElement());

          ui.getPage()
              .executeJs("window.renderGoogleButton('google-signin-button', 'outline', 'large');");
        });

    return container;
  }

  /**
   * Handle Google Sign-In callback from JavaScript. Uses @ClientCallable to expose method to
   * client-side.
   */
  @ClientCallable
  public void handleGoogleCallback(String googleIdToken) {
    log.info("Received Google ID token, authenticating...");

    googleSignInService
        .authenticateWithGoogle(googleIdToken)
        .subscribe(this::handleGoogleLoginSuccess, this::handleGoogleLoginError);
  }

  private void handleGoogleLoginSuccess(AuthResponse response) {
    UI ui = UI.getCurrent();
    if (ui != null) {
      ui.access(
          () -> {
            SecurityUtils.authenticateUser(
                response.getUsername(),
                response.getUserId(),
                response.getToken(),
                response.getEmail());

            VaadinSession.getCurrent().setAttribute("jwttoken", response.getToken());
            VaadinSession.getCurrent().setAttribute("refreshtoken", response.getRefreshToken());

            Notification.show(
                    "Welcome, " + response.getUsername() + "!",
                    3000,
                    Notification.Position.TOP_CENTER)
                .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            UI.getCurrent().navigate("");
          });
    }
  }

  private void handleGoogleLoginError(Throwable error) {
    log.error("Google Sign-In failed", error);

    UI ui = UI.getCurrent();
    if (ui != null) {
      ui.access(
          () -> {
            String errorMsg = "Google Sign-In failed. Please try again.";
            if (error.getMessage() != null && error.getMessage().contains("timeout")) {
              errorMsg = "Connection timeout. Please check your internet.";
            }
            showError(errorMsg);
          });
    }
  }

  // Helper methods
  private H1 createLogo() {
    H1 logo = new H1("ShikshaSpace");
    logo.addClassName("login-logo");
    logo.getStyle()
        .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
        .set("-webkit-background-clip", "text")
        .set("-webkit-text-fill-color", "transparent")
        .set("background-clip", "text")
        .set("font-size", "clamp(1.75rem, 6vw, 2.5rem)")
        .set("font-weight", "700")
        .set("text-align", "center")
        .set("margin-bottom", "8px");
    return logo;
  }

  private H2 createWelcomeText() {
    H2 welcomeText = new H2("Welcome Back!");
    welcomeText.addClassName("welcome-text");
    welcomeText
        .getStyle()
        .set("color", "#333")
        .set("font-weight", "400")
        .set("text-align", "center")
        .set("font-size", "clamp(1rem, 3vw, 1.5rem)")
        .set("margin-bottom", "clamp(16px, 4vw, 24px)");
    return welcomeText;
  }

  private Div createErrorMessage() {
    Div errorMsg = new Div();
    errorMsg.setVisible(false);
    errorMsg.addClassName("error-message");
    errorMsg
        .getStyle()
        .set("color", "white")
        .set("background", "#f44336")
        .set("padding", "10px")
        .set("border-radius", "6px")
        .set("margin-bottom", "15px")
        .set("text-align", "center");
    return errorMsg;
  }

  private TextField createUsernameField() {
    TextField field = new TextField("Username or Email");
    field.setWidthFull();
    field.setPlaceholder("Enter your username");
    field.setRequired(true);
    field.setClearButtonVisible(true);
    field.addClassName("premium-input-field");
    field.getStyle().set("margin-bottom", "clamp(12px, 2.5vw, 16px)");
    return field;
  }

  private PasswordField createPasswordField() {
    PasswordField field = new PasswordField("Password");
    field.setWidthFull();
    field.setPlaceholder("Enter your password");
    field.setRequired(true);
    field.setRevealButtonVisible(true);
    field.addClassName("premium-input-field");
    field.getStyle().set("margin-bottom", "clamp(12px, 2.5vw, 16px)");

    field.addKeyPressListener(
        event -> {
          if (event.getKey().equals("Enter")) {
            handleLogin();
          }
        });

    return field;
  }

  private Button createLoginButton() {
    Button button = new Button("Login");
    button.setWidthFull();
    button.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_LARGE);
    button.addClassName("premium-login-button");
    button
        .getStyle()
        .set("margin-top", "clamp(8px, 2vw, 12px)")
        .set("margin-bottom", "clamp(8px, 2vw, 12px)")
        .set("border-radius", "10px")
        .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
        .set("min-height", "44px");

    button.addClickListener(event -> handleLogin());
    return button;
  }

  private Div createRegisterContainer() {
    Div registerContainer = new Div();
    registerContainer.addClassName("register-container");
    registerContainer
        .getStyle()
        .set("text-align", "center")
        .set("margin-top", "clamp(20px, 4vw, 28px)")
        .set("padding", "clamp(16px, 3vw, 20px)")
        .set("background", "linear-gradient(145deg, #f8f9ff, #ffffff)")
        .set("border-radius", "10px");

    Paragraph registerText = new Paragraph();
    registerText.setText("Don't have an account? ");

    RouterLink registerLink = new RouterLink("Register now", RegisterView.class);
    registerLink.addClassName("register-link");
    registerLink.getStyle().set("color", "#667eea").set("font-weight", "600");

    registerText.add(registerLink);
    registerContainer.add(registerText);
    return registerContainer;
  }

  private void handleLogin() {
    String username = usernameField.getValue();
    String password = passwordField.getValue();

    if (username.isBlank() || password.isBlank()) {
      showError("Please enter both username and password");
      return;
    }

    loginButton.setEnabled(false);
    loginButton.setText("Logging in...");
    errorMessage.setVisible(false);

    try {
      AuthResponse response =
          userServiceClient
              .login(new LoginRequest(username, password))
              .block(Duration.ofSeconds(10));

      if (response != null && response.getToken() != null) {
        SecurityUtils.authenticateUser(
            response.getUsername(), response.getUserId(), response.getToken(), response.getEmail());

        VaadinSession.getCurrent().setAttribute("jwttoken", response.getToken());
        VaadinSession.getCurrent().setAttribute("refreshtoken", response.getRefreshToken());

        Notification.show(
                "Welcome back, " + response.getUsername() + "!",
                3000,
                Notification.Position.TOP_CENTER)
            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

        UI.getCurrent().navigate("");
      }
    } catch (Exception e) {
      log.error("Login failed", e);
      loginButton.setEnabled(false);
      loginButton.setText("Login");

      String errorMsg = "Login failed. Please try again.";
      if (e.getMessage() != null && e.getMessage().contains("401")) {
        errorMsg = "Invalid username or password.";
      }
      showError(errorMsg);
    }
  }

  private void showError(String message) {
    errorMessage.setText(message);
    errorMessage.setVisible(true);
  }
}
