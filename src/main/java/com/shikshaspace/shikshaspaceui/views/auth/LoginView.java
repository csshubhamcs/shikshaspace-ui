package com.shikshaspace.shikshaspaceui.views.auth;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;

@Route("login")
public class LoginView extends Div {

  public LoginView() {
    addClassName("auth-page");

    VerticalLayout card = new VerticalLayout();
    card.addClassName("auth-card");
    card.setPadding(false);
    card.setSpacing(false);

    // Title
    H1 title = new H1("Login");

    // Form
    FormLayout form = new FormLayout();
    form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

    TextField username = new TextField();
    PasswordField password = new PasswordField();

    form.addFormItem(username, "Username");
    form.addFormItem(password, "Password");

    // Login button
    Button loginButton = new Button("Log in");
    loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    Div socialButtons = createSocialButtons();

    // Forgot password link
    //        Div forgotPassword = new Div();
    //        forgotPassword.addClassName("forgot-password");
    //        RouterLink forgotLink = new RouterLink("Forgot password", ForgotPasswordView.class);
    //        forgotPassword.add(forgotLink);

    // Footer with register link
    Div footer = createFooter();

    card.add(title, form, loginButton, socialButtons, footer);
    add(card);
  }

  private Div createFooter() {
    Div footer = new Div();
    footer.addClassName("auth-footer");

    Span text = new Span("Don't have an account?");
    RouterLink link = new RouterLink("Register", RegisterView.class);

    footer.add(text, link);
    return footer;
  }

  private Div createSocialButtons() {
    Div container = new Div();
    container.addClassName("social-buttons");

    Button google = new Button("Continue with Google");
    google.addClassName("social-button");
    google.addClassName("google");

    Button github = new Button("Continue with GitHub");
    github.addClassName("social-button");
    github.addClassName("github");

    container.add(google, github);
    return container;
  }
}
