package com.shikshaspace.shikshaspaceui.components;

import com.shikshaspace.shikshaspaceui.security.SecurityUtils;
import com.shikshaspace.shikshaspaceui.views.auth.LoginView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class NavBar extends HorizontalLayout {

  private final SecurityUtils securityUtils;
  private VerticalLayout dropdown;

  public NavBar(SecurityUtils securityUtils) {
    this.securityUtils = securityUtils;

    addClassName("navbar");
    setWidthFull();
    setJustifyContentMode(JustifyContentMode.BETWEEN);
    setAlignItems(Alignment.CENTER);

    add(createLogo(), createNavLinks(), createAuthSection());
  }

  private Span createLogo() {
    Span logo = new Span("ShikshaSpace");
    logo.addClassName("navbar__logo");
    logo.addClickListener(e -> navigateTo(""));
    return logo;
  }

  private HorizontalLayout createNavLinks() {
    HorizontalLayout links = new HorizontalLayout();
    links.addClassName("navbar__links");
    links.setSpacing(true);

    links.add(
        createNavLink("Home", ""),
        createNavLink("Explore", "explore"),
        createNavLink("About", "about"));

    return links;
  }

  private Span createNavLink(String text, String route) {
    Span link = new Span(text);
    link.addClassName("navbar__link");
    link.addClickListener(
        e -> {
          closeDropdown();
          navigateTo(route);
        });
    return link;
  }

  private Component createAuthSection() {
    if (securityUtils.isUserLoggedIn()) {
      return createProfileMenu();
    } else {
      Button loginButton = new Button("Login");
      loginButton.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(LoginView.class)));
      return loginButton;
    }
  }

  private Div createProfileMenu() {
    Div container = new Div();
    container.addClassName("navbar__profile-menu");

    Div avatarContainer = createAvatarContainer();
    dropdown = createDropdown();

    container.add(avatarContainer, dropdown);
    return container;
  }

  private Div createAvatarContainer() {
    Div container = new Div();
    container.addClassName("navbar__avatar-container");

    String username = securityUtils.getAuthenticatedUsername();
    Avatar avatar = new Avatar(username != null ? username : "User");
    avatar.addClassName("navbar__avatar");

    container.add(avatar);
    container.addClickListener(e -> toggleDropdown());

    return container;
  }

  private VerticalLayout createDropdown() {
    VerticalLayout dropdown = new VerticalLayout();
    dropdown.addClassName("navbar__dropdown");
    dropdown.setVisible(false);
    dropdown.setPadding(false);
    dropdown.setSpacing(false);

    dropdown.add(
        createUserHeader(),
        createDivider(),
        createDropdownItem("My ShikshaSpace", "my-shiksha", false),
        createDropdownItem("Profile Edit", "profile-edit", false),
        createDivider(),
        createDropdownItem("Logout", "", true));

    return dropdown;
  }

  private Div createUserHeader() {
    Div header = new Div();
    header.addClassName("navbar__dropdown-header");

    String username = securityUtils.getAuthenticatedUsername();
    Span userName = new Span(username != null ? username : "User");
    userName.addClassName("navbar__dropdown-username");

    Span userEmail = new Span("user@example.com");
    userEmail.addClassName("navbar__dropdown-email");

    header.add(userName, userEmail);
    return header;
  }

  private Hr createDivider() {
    Hr divider = new Hr();
    divider.addClassName("navbar__dropdown-divider");
    return divider;
  }

  private Div createDropdownItem(String text, String route, boolean isDanger) {
    Div item = new Div();
    item.addClassName("navbar__dropdown-item");
    if (isDanger) {
      item.addClassName("navbar__dropdown-item--danger");
    }
    item.setText(text);
    item.addClickListener(
        e -> {
          closeDropdown();
          if (isDanger) {
            securityUtils.logout();
            getUI().ifPresent(ui -> ui.navigate(""));
          } else {
            navigateTo(route);
          }
        });
    return item;
  }

  private void toggleDropdown() {
    if (dropdown != null) {
      dropdown.setVisible(!dropdown.isVisible());
    }
  }

  private void closeDropdown() {
    if (dropdown != null) {
      dropdown.setVisible(false);
    }
  }

  private void navigateTo(String route) {
    getUI().ifPresent(ui -> ui.navigate(route));
  }
}
