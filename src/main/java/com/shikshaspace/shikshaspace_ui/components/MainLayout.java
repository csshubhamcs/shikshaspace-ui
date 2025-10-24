package com.shikshaspace.shikshaspace_ui.components;

import com.shikshaspace.shikshaspace_ui.security.SecurityUtils;
import com.shikshaspace.shikshaspace_ui.views.HomePage;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.LumoUtility;

/**
 * Production-grade responsive main layout. Provides navigation drawer and top header with user
 * profile menu.
 */
public class MainLayout extends AppLayout {

  public MainLayout() {
    createHeader();
    createDrawer();
    addClassName("responsive-main-layout");
  }

  /** Create responsive header with logo and profile menu. */
  private void createHeader() {
    DrawerToggle toggle = new DrawerToggle();
    toggle.getStyle().set("color", "white").set("cursor", "pointer");
    toggle.setAriaLabel("Toggle navigation menu");

    H1 logo = new H1("ShikshaSpace");
    logo.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
    logo.getStyle()
        .set("color", "white")
        .set("font-weight", "600")
        .set("font-size", "clamp(1.2rem, 4vw, 1.75rem)")
        .set("letter-spacing", "0.5px");

    MenuBar profileMenu = createProfileMenu();

    HorizontalLayout header = new HorizontalLayout(toggle, logo, profileMenu);
    header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    header.expand(logo);
    header.setWidthFull();
    header.addClassNames(LumoUtility.Padding.Vertical.SMALL, LumoUtility.Padding.Horizontal.MEDIUM);
    header
        .getStyle()
        .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
        .set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)");

    addToNavbar(header);
  }

  /** Create profile dropdown menu with user info and actions. */
  private MenuBar createProfileMenu() {
    MenuBar menuBar = new MenuBar();
    menuBar.setOpenOnHover(false);
    menuBar.getStyle().set("background", "transparent").set("color", "white");

    String username = SecurityUtils.getUsername();
    String email = SecurityUtils.getEmail();

    Avatar avatar = new Avatar(username != null ? username : "User");
    avatar.getStyle().set("background", "white").set("color", "#667eea").set("cursor", "pointer");

    Span usernameLabel = new Span(username != null ? username : "User");
    usernameLabel.addClassName("username-label");
    usernameLabel
        .getStyle()
        .set("color", "white")
        .set("margin-left", "10px")
        .set("font-weight", "500");

    HorizontalLayout profileItem = new HorizontalLayout(avatar, usernameLabel);
    profileItem.setAlignItems(FlexComponent.Alignment.CENTER);
    profileItem.setSpacing(false);

    MenuItem menuItem = menuBar.addItem(profileItem);
    SubMenu subMenu = menuItem.getSubMenu();

    // User info header
    VerticalLayout userInfo = new VerticalLayout();
    userInfo.setPadding(true);
    userInfo.setSpacing(false);

    Span nameSpan = new Span(username != null ? username : "User");
    nameSpan.getStyle().set("font-weight", "600").set("font-size", "15px");

    Span emailSpan = new Span(email != null ? email : "");
    emailSpan.getStyle().set("font-size", "12px").set("color", "var(--lumo-secondary-text-color)");

    userInfo.add(nameSpan, emailSpan);
    subMenu.addItem(userInfo).setEnabled(false);
    subMenu.add(new Hr());

    // Profile link
    MenuItem editProfile = subMenu.addItem(createMenuItemContent(VaadinIcon.USER, "Edit Profile"));
    editProfile.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("profile")));

    // Logout link
    MenuItem logout = subMenu.addItem(createMenuItemContent(VaadinIcon.SIGN_OUT, "Logout"));
    logout.addClickListener(
        e -> {
          SecurityUtils.logout();
          getUI().ifPresent(ui -> ui.navigate("login"));
        });

    return menuBar;
  }

  /** Helper to create menu item with icon and text. */
  private HorizontalLayout createMenuItemContent(VaadinIcon iconType, String text) {
    Icon icon = iconType.create();
    icon.getStyle().set("color", "var(--lumo-secondary-text-color)");

    Span label = new Span(text);
    label.getStyle().set("margin-left", "10px");

    HorizontalLayout layout = new HorizontalLayout(icon, label);
    layout.setAlignItems(FlexComponent.Alignment.CENTER);
    layout.setSpacing(false);
    return layout;
  }

  /** Create navigation drawer with menu items. */
  private void createDrawer() {
    VerticalLayout drawerContent = new VerticalLayout();
    drawerContent.setSizeFull();
    drawerContent.setPadding(true);
    drawerContent.setSpacing(true);

    RouterLink homeLink = createNavLink(VaadinIcon.HOME, "Home", HomePage.class);
    RouterLink profileLink = createNavLink(VaadinIcon.USER, "Profile", "profile");

    drawerContent.add(homeLink, profileLink);
    addToDrawer(drawerContent);
  }

  /** Create navigation link with icon and label. */
  private RouterLink createNavLink(
      VaadinIcon iconType,
      String text,
      Class<? extends com.vaadin.flow.component.Component> routeClass) {
    Icon icon = iconType.create();
    icon.getStyle().set("width", "20px").set("height", "20px");

    Span label = new Span(text);

    RouterLink link = new RouterLink();
    link.add(icon, label);
    link.setRoute(routeClass);
    link.addClassName("responsive-nav-link");
    link.getStyle()
        .set("display", "flex")
        .set("align-items", "center")
        .set("gap", "12px")
        .set("padding", "12px 16px")
        .set("border-radius", "10px")
        .set("text-decoration", "none")
        .set("color", "var(--lumo-body-text-color)")
        .set("transition", "all 0.3s ease");

    return link;
  }

  /** Overloaded method for String route navigation. */
  private RouterLink createNavLink(VaadinIcon iconType, String text, String route) {
    Icon icon = iconType.create();
    icon.getStyle().set("width", "20px").set("height", "20px");

    Span label = new Span(text);

    RouterLink link = new RouterLink(text, HomePage.class);
    link.getElement().setAttribute("href", route);
    link.removeAll();
    link.add(icon, label);
    link.addClassName("responsive-nav-link");
    link.getStyle()
        .set("display", "flex")
        .set("align-items", "center")
        .set("gap", "12px")
        .set("padding", "12px 16px")
        .set("border-radius", "10px")
        .set("text-decoration", "none")
        .set("color", "var(--lumo-body-text-color)")
        .set("transition", "all 0.3s ease");

    return link;
  }
}
