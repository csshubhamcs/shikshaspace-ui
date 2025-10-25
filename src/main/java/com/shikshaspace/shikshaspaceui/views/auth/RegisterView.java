package com.shikshaspace.shikshaspaceui.views.auth;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AnonymousAllowed;

@Route("register")
@PageTitle("Register - ShikshaSpace")
@AnonymousAllowed
public class RegisterView extends Div {

  public RegisterView() {
    addClassName("auth-page");

    VerticalLayout card = new VerticalLayout();
    card.addClassName("auth-card");
    card.setPadding(false);
    card.setSpacing(false);

    H1 title = new H1("Create Account");

    Paragraph subtitle = new Paragraph("Join ShikshaSpace today");
    subtitle.addClassName("auth__subtitle");

    Div socialButtons = createSocialButtons();

    Span divider = new Span("OR");
    divider.addClassName("auth__divider");

    FormLayout form = createForm();

    Button registerButton = new Button("Create Account");
    registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

    Div footer = createFooter();

    card.add(title, subtitle, socialButtons, divider, form, registerButton, footer);
    add(card);
  }

  private Div createSocialButtons() {
    Div container = new Div();
    container.addClassName("social-buttons");

    Button google = new Button("Continue with Google");
    google.addClassName("social-button");
    google.addClassName("social-button--google");
    google.addClickListener(
        e -> UI.getCurrent().getPage().setLocation("/oauth2/authorization/google"));

    container.add(google);
    return container;
  }

  private FormLayout createForm() {
    FormLayout form = new FormLayout();
    form.setResponsiveSteps(
        new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("400px", 2));

    TextField firstName = new TextField();
    TextField lastName = new TextField();
    EmailField email = new EmailField();
    PasswordField password = new PasswordField();

    form.addFormItem(firstName, "First Name");
    form.addFormItem(lastName, "Last Name");
    form.addFormItem(email, "Email");
    form.addFormItem(password, "Password");

    form.setColspan(email, 2);
    form.setColspan(password, 2);

    return form;
  }

  private Div createFooter() {
    Div footer = new Div();
    footer.addClassName("auth__footer");

    Span text = new Span("Already have an account?");
    RouterLink link = new RouterLink("Login", LoginView.class);

    footer.add(text, link);
    return footer;
  }
}
