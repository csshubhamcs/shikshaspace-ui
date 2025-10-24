package com.shikshaspace.shikshaspace_ui.components;

import com.shikshaspace.shikshaspace_ui.security.SecurityUtils;
import com.shikshaspace.shikshaspace_ui.views.LoginView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoUtility;
import lombok.extern.slf4j.Slf4j;

/** Production-grade main layout with proper authentication handling. */
@Slf4j
public class MainLayout extends AppLayout {

  public MainLayout() {
    createHeader();
    createDrawer();
  }

  /** Create header with logo and user menu. */
  private void createHeader() {
    DrawerToggle toggle = new DrawerToggle();
    toggle.getElement().setAttribute("aria-label", "Menu toggle");

    H1 logo = new H1("ShikshaSpace");
    logo.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
    logo.getStyle().set("cursor", "pointer");
    logo.addClickListener(e -> UI.getCurrent().navigate(""));

    // Create header layout
    HorizontalLayout header = new HorizontalLayout();
    header.setWidthFull();
    header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
    header.setAlignItems(FlexComponent.Alignment.CENTER);
    header.setPadding(true);
    header.setSpacing(true);

    // Left side: toggle + logo
    HorizontalLayout leftSection = new HorizontalLayout(toggle, logo);
    leftSection.setAlignItems(FlexComponent.Alignment.CENTER);
    leftSection.setSpacing(true);

    // Right side: user menu (only if authenticated)
    HorizontalLayout rightSection = new HorizontalLayout();
    rightSection.setAlignItems(FlexComponent.Alignment.CENTER);
    rightSection.setSpacing(true);

    // CRITICAL: Only show user info if authenticated
    if (SecurityUtils.isAuthenticated()) {
      rightSection.add(createUserMenu());
    } else {
      // Show login/register buttons
      rightSection.add(createAuthButtons());
    }

    header.add(leftSection, rightSection);
    addToNavbar(header);
  }

  /** Create drawer with navigation items. */
  private void createDrawer() {
    SideNav nav = new SideNav();

    // Always visible items
    nav.addItem(new SideNavItem("Home", "", VaadinIcon.HOME.create()));
    nav.addItem(new SideNavItem("Explore", "explore", VaadinIcon.SEARCH.create()));
    nav.addItem(new SideNavItem("About", "about", VaadinIcon.INFO_CIRCLE.create()));

    // Only show profile if authenticated
    if (SecurityUtils.isAuthenticated()) {
      nav.addItem(new SideNavItem("Profile", "profile", VaadinIcon.USER.create()));
    }

    addToDrawer(nav);
  }

  /** Create user menu with avatar and dropdown (shown when authenticated). */
  private Div createUserMenu() {
    String username = SecurityUtils.getUsername();

    if (username == null || username.isEmpty()) {
      log.warn("⚠️ createUserMenu called but no username found");
      return new Div(); // Return empty div
    }

    // User section container
    Div userSection = new Div();
    userSection.addClassName("user-section");
    userSection
        .getStyle()
        .set("display", "flex")
        .set("align-items", "center")
        .set("gap", "10px")
        .set("cursor", "pointer")
        .set("padding", "8px 12px")
        .set("border-radius", "8px")
        .set("background", "var(--lumo-contrast-5pct)");

    // Avatar
    Avatar avatar = new Avatar(username);
    avatar.setColorIndex(username.hashCode() % 7);

    // Username
    Span usernameSpan = new Span(username);
    usernameSpan.getStyle().set("font-weight", "500").set("color", "var(--lumo-body-text-color)");

    userSection.add(avatar, usernameSpan);

    // Create context menu
    ContextMenu contextMenu = new ContextMenu();
    contextMenu.setTarget(userSection);
    contextMenu.setOpenOnClick(true);

    // Menu items
    contextMenu
        .addItem(
            "Profile",
            e -> {
              log.info("Navigating to profile");
              UI.getCurrent().navigate("profile");
            })
        .addComponentAsFirst(VaadinIcon.USER.create());

    contextMenu
        .addItem(
            "Settings",
            e -> {
              log.info("Navigating to settings");
              UI.getCurrent().navigate("settings");
            })
        .addComponentAsFirst(VaadinIcon.COG.create());

    contextMenu.add(new Div()); // Separator

    contextMenu
        .addItem(
            "Logout",
            e -> {
              log.info("User logout initiated");
              handleLogout();
            })
        .addComponentAsFirst(VaadinIcon.SIGN_OUT.create());

    return userSection;
  }

  /** Create login/register buttons (shown when NOT authenticated). */
  private HorizontalLayout createAuthButtons() {
    HorizontalLayout authButtons = new HorizontalLayout();
    authButtons.setSpacing(true);
    authButtons.setAlignItems(FlexComponent.Alignment.CENTER);

    // Login button
    Button loginButton = new Button("Sign In", new Icon(VaadinIcon.SIGN_IN));
    loginButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    loginButton.addClickListener(e -> UI.getCurrent().navigate("login"));

    // Register button
    Button registerButton = new Button("Sign Up", new Icon(VaadinIcon.USER_CHECK));
    registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    registerButton.addClickListener(e -> UI.getCurrent().navigate("register"));

    authButtons.add(loginButton, registerButton);
    return authButtons;
  }

  /** Handle user logout. */
  private void handleLogout() {
    log.info("🔵 Logout initiated");

    // Clear session
    SecurityUtils.logout();

    // Navigate to login
    UI.getCurrent().navigate(LoginView.class);
    UI.getCurrent().getPage().reload();

    log.info("✅ User logged out and redirected to login");
  }
}
