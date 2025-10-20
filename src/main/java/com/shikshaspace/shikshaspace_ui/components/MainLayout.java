package com.shikshaspace.shikshaspace_ui.components;

import com.shikshaspace.shikshaspace_ui.security.SecurityUtils;
import com.shikshaspace.shikshaspace_ui.views.AboutView;
import com.shikshaspace.shikshaspace_ui.views.ExploreView;
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
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;

/**
 * Production-grade responsive MainLayout with modern premium design Supports mobile (320px+),
 * tablet (768px+), and desktop (1024px+)
 *
 * @author ShikshaSpace Engineering Team
 * @version 2.0 (Responsive + Premium UI)
 */
public class MainLayout extends AppLayout {

  private static final String MOBILE_BREAKPOINT = "768px";
  private static final String TABLET_BREAKPOINT = "1024px";

  public MainLayout() {
    // Enable responsive drawer overlay behavior
    setPrimarySection(Section.DRAWER);

    createResponsiveHeader();
    createResponsiveDrawer();

    // Add responsive CSS classes
    addClassName("responsive-main-layout");

    // Configure drawer for mobile overlay
    getElement().getStyle().set("--vaadin-app-layout-drawer-overlay", "true");
  }

  private void createResponsiveHeader() {
    // Hamburger menu toggle (visible on all devices)
    DrawerToggle toggle = new DrawerToggle();
    toggle.addClassName("drawer-toggle");
    toggle.getStyle().set("color", "white").set("cursor", "pointer");
    toggle.setAriaLabel("Toggle navigation menu");

    // Logo/Title with fluid typography
    H1 logo = new H1("ShikshaSpace");
    logo.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE, "header-logo");
    logo.getStyle()
        .set("color", "white")
        .set("font-weight", "600")
        .set("font-size", "clamp(1.2rem, 4vw, 1.75rem)")
        .set("letter-spacing", "0.5px")
        .set("text-shadow", "0 2px 4px rgba(0,0,0,0.1)")
        .set("cursor", "default");

    // ✅ Navigation links (About & Explore)
    HorizontalLayout navLinks = createNavigationLinks();

    // Profile menu
    MenuBar profileMenu = createResponsiveProfileMenu();
    profileMenu.addClassName("profile-menu-responsive");

    // Header layout
    HorizontalLayout header = new HorizontalLayout(toggle, logo, navLinks, profileMenu);
    header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
    header.setWidthFull();
    header.addClassNames(
        Padding.Vertical.SMALL, Padding.Horizontal.MEDIUM, Gap.MEDIUM, "responsive-header");

    // Modern gradient with glassmorphism effect
    header
        .getStyle()
        .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
        .set("box-shadow", "0 2px 8px rgba(0,0,0,0.1), 0 4px 16px rgba(102, 126, 234, 0.15)")
        .set("backdrop-filter", "blur(10px)")
        .set("position", "sticky")
        .set("top", "0")
        .set("z-index", "1000")
        .set("padding", "clamp(8px, 2vw, 16px)");

    addToNavbar(header);
  }

  /**
   * ✅ NEW METHOD: Create navigation links (About & Explore) Hidden on mobile, visible on tablet and
   * desktop
   */
  private HorizontalLayout createNavigationLinks() {
    HorizontalLayout navLinks = new HorizontalLayout();
    navLinks.setSpacing(true);
    navLinks.setAlignItems(FlexComponent.Alignment.CENTER);
    navLinks.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
    navLinks.addClassName("header-nav-links");

    navLinks
        .getStyle()
        .set("gap", "24px")
        .set("flex-grow", "1") // ✅ Take remaining space to center
        .set("display", "none") // Hidden on mobile by default
        .set("cursor", "default"); // ✅ Remove pointer cursor

    // RouterLink takes (String text, Class navigationTarget)
    RouterLink aboutLink = new RouterLink("About", AboutView.class);
    aboutLink.addClassName("header-link");
    aboutLink
        .getStyle()
        .set("color", "white")
        .set("text-decoration", "none")
        .set("font-weight", "500")
        .set("font-size", "16px")
        .set("padding", "8px 16px")
        .set("border-radius", "6px")
        .set("transition", "all 0.2s ease")
        .set("cursor", "pointer");

    RouterLink exploreLink = new RouterLink("Explore", ExploreView.class);
    exploreLink.addClassName("header-link");
    exploreLink
        .getStyle()
        .set("color", "white")
        .set("text-decoration", "none")
        .set("font-weight", "500")
        .set("font-size", "16px")
        .set("padding", "8px 16px")
        .set("border-radius", "6px")
        .set("transition", "all 0.2s ease")
        .set("cursor", "pointer");

    // Add hover effects
    addLinkHoverEffect(aboutLink);
    addLinkHoverEffect(exploreLink);

    navLinks.add(aboutLink, exploreLink);

    // Show on tablet and above (768px+)
    navLinks
        .getElement()
        .executeJs(
            "const navLinks = this;"
                + "function updateNavVisibility() {"
                + "  if (window.innerWidth >= 768) {"
                + "    navLinks.style.display = 'flex';"
                + "  } else {"
                + "    navLinks.style.display = 'none';"
                + "  }"
                + "}"
                + "updateNavVisibility();"
                + "window.addEventListener('resize', updateNavVisibility);");

    return navLinks;
  }

  /** Create responsive profile dropdown menu Hides username label on mobile devices */
  private MenuBar createResponsiveProfileMenu() {
    MenuBar menuBar = new MenuBar();
    menuBar.setOpenOnHover(false);
    menuBar.addClassName("profile-menu-bar");
    menuBar.getStyle().set("background", "transparent").set("color", "white");

    // Get current user info from SecurityUtils
    String username = SecurityUtils.getUsername();
    String email = SecurityUtils.getEmail();

    // User avatar with responsive sizing
    Avatar avatar = new Avatar(username != null ? username : "User");
    avatar.addClassName("user-avatar");
    avatar
        .getStyle()
        .set("--vaadin-avatar-size", "var(--lumo-size-m)")
        .set("background", "white")
        .set("color", "#667eea")
        .set("cursor", "pointer")
        .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

    // Username label - hidden on mobile via CSS
    Span usernameLabel = new Span(username != null ? username : "User");
    usernameLabel.addClassNames(LumoUtility.Display.Breakpoint.Small.HIDDEN, "username-label");
    usernameLabel
        .getStyle()
        .set("color", "white")
        .set("margin-left", "10px")
        .set("font-weight", "500")
        .set("font-size", "14px");

    // Create profile menu item with avatar and username
    HorizontalLayout profileItem = new HorizontalLayout(avatar, usernameLabel);
    profileItem.setAlignItems(FlexComponent.Alignment.CENTER);
    profileItem.setSpacing(false);
    profileItem.addClassName("profile-menu-item");

    MenuItem menuItem = menuBar.addItem(profileItem);
    SubMenu subMenu = menuItem.getSubMenu();

    // User info header in dropdown with modern styling
    VerticalLayout userInfo = new VerticalLayout();
    userInfo.setPadding(true);
    userInfo.setSpacing(false);
    userInfo.addClassName("user-info-header");
    userInfo
        .getStyle()
        .set("background", "linear-gradient(145deg, #f8f9ff, #ffffff)")
        .set("border-radius", "8px")
        .set("margin-bottom", "8px");

    Span nameSpan = new Span(username != null ? username : "User");
    nameSpan.getStyle().set("font-weight", "600").set("font-size", "15px").set("color", "#333");

    Span emailSpan = new Span(email != null ? email : "");
    emailSpan
        .getStyle()
        .set("font-size", "12px")
        .set("color", "var(--lumo-secondary-text-color)")
        .set("margin-top", "4px");

    userInfo.add(nameSpan, emailSpan);
    subMenu.addItem(userInfo).setEnabled(false);

    // Modern divider
    Hr divider = new Hr();
    divider
        .getStyle()
        .set("margin", "8px 0")
        .set("border", "none")
        .set("border-top", "1px solid var(--lumo-contrast-10pct)");
    subMenu.add(divider);

    // Edit Profile menu item with icon
    MenuItem editProfile = subMenu.addItem(createMenuItemContent(VaadinIcon.USER, "Edit Profile"));
    editProfile.addClassName("profile-menu-option");
    editProfile.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate("profile")));
    styleMenuItem(editProfile);

    // Logout menu item with icon
    MenuItem logout = subMenu.addItem(createMenuItemContent(VaadinIcon.SIGN_OUT, "Logout"));
    logout.addClassName("profile-menu-option");
    logout.addClickListener(
        e -> {
          SecurityUtils.logout();
          getElement().executeJs("window.location.replace('/login');");
        });
    styleMenuItem(logout);

    return menuBar;
  }

  /** Style individual menu items with hover effects */
  private void styleMenuItem(MenuItem item) {
    item.getElement()
        .getStyle()
        .set("padding", "10px 16px")
        .set("cursor", "pointer")
        .set("transition", "all 0.2s ease")
        .set("border-radius", "6px");
  }

  /** Helper to create menu item with icon and text Used for profile dropdown options */
  private HorizontalLayout createMenuItemContent(VaadinIcon iconType, String text) {
    Icon icon = iconType.create();
    icon.getStyle()
        .set("color", "var(--lumo-secondary-text-color)")
        .set("width", "18px")
        .set("height", "18px");

    Span label = new Span(text);
    label.getStyle().set("margin-left", "12px").set("font-size", "14px").set("font-weight", "500");

    HorizontalLayout layout = new HorizontalLayout(icon, label);
    layout.setAlignItems(FlexComponent.Alignment.CENTER);
    layout.setSpacing(false);
    layout.getStyle().set("width", "100%");

    return layout;
  }

  /**
   * Create responsive navigation drawer (sidebar) Overlay on mobile, permanent sidebar on desktop
   */
  private void createResponsiveDrawer() {
    VerticalLayout drawerContent = new VerticalLayout();
    drawerContent.setSizeFull();
    drawerContent.setPadding(true);
    drawerContent.setSpacing(true);
    drawerContent.addClassName("drawer-content");
    drawerContent
        .getStyle()
        .set("background", "linear-gradient(180deg, #f8f9ff 0%, #ffffff 100%)")
        .set("padding", "clamp(12px, 3vw, 20px)"); // Responsive padding

    // Navigation links with modern styling
    RouterLink homeLink = createResponsiveNavLink(VaadinIcon.HOME, "Home", HomePage.class);

    // Add more navigation items here in the future
    RouterLink profileLink = createResponsiveNavLink(VaadinIcon.USER, "Profile", "profile");

    // Placeholder for future navigation with modern styling
    Span placeholder = new Span("More features coming soon...");
    placeholder.addClassName("drawer-placeholder");
    placeholder
        .getStyle()
        .set("color", "var(--lumo-secondary-text-color)")
        .set("font-size", "12px")
        .set("margin-top", "auto")
        .set("padding", "16px 12px")
        .set("font-style", "italic")
        .set("text-align", "center")
        .set("background", "linear-gradient(145deg, #ffffff, #f8f9ff)")
        .set("border-radius", "8px")
        .set("border", "1px dashed var(--lumo-contrast-20pct)");

    drawerContent.add(homeLink, profileLink, placeholder);

    addToDrawer(drawerContent);
  }

  /**
   * Helper to create responsive navigation link with modern premium design Supports both Class<?
   * extends Component> and String routes
   */
  private RouterLink createResponsiveNavLink(
      VaadinIcon iconType,
      String text,
      Class<? extends com.vaadin.flow.component.Component> routeClass) {
    Icon icon = iconType.create();
    icon.addClassName("nav-icon");
    icon.getStyle().set("width", "20px").set("height", "20px").set("flex-shrink", "0");

    Span label = new Span(text);
    label.addClassName("nav-label");
    label.getStyle().set("font-size", "14px").set("font-weight", "500").set("flex-grow", "1");

    RouterLink link = new RouterLink();
    link.add(icon, label);
    link.setRoute(routeClass);
    link.addClassName("responsive-nav-link");

    // Modern premium styling with hover effects
    link.getStyle()
        .set("display", "flex")
        .set("align-items", "center")
        .set("gap", "12px")
        .set("padding", "12px 16px")
        .set("border-radius", "10px")
        .set("text-decoration", "none")
        .set("color", "var(--lumo-body-text-color)")
        .set("transition", "all 0.3s cubic-bezier(0.4, 0, 0.2, 1)")
        .set("background", "transparent")
        .set("margin-bottom", "6px")
        .set("position", "relative");

    return link;
  }

  /** Overloaded method for String route navigation */
  private RouterLink createResponsiveNavLink(VaadinIcon iconType, String text, String route) {
    Icon icon = iconType.create();
    icon.addClassName("nav-icon");
    icon.getStyle().set("width", "20px").set("height", "20px").set("flex-shrink", "0");

    Span label = new Span(text);
    label.addClassName("nav-label");
    label.getStyle().set("font-size", "14px").set("font-weight", "500").set("flex-grow", "1");

    RouterLink link = new RouterLink(text, HomePage.class); // Fallback
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
        .set("transition", "all 0.3s cubic-bezier(0.4, 0, 0.2, 1)")
        .set("background", "transparent")
        .set("margin-bottom", "6px");

    return link;
  }

  /** ✅ NEW METHOD: Add hover effect to header links */
  private void addLinkHoverEffect(RouterLink link) {
    link.getElement()
        .executeJs(
            "this.addEventListener('mouseenter', () => {"
                + "  this.style.background = 'rgba(255, 255, 255, 0.15)';"
                + "});"
                + "this.addEventListener('mouseleave', () => {"
                + "  this.style.background = 'transparent';"
                + "});");
  }
}
