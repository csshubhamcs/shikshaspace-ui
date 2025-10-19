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
 * Main layout with header and navigation drawer
 * Used by all authenticated views (HomePage, ProfileView, etc.)
 */
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawer();
    }

    /**
     * Create header with logo and profile menu
     */
    private void createHeader() {
        // Hamburger menu toggle
        DrawerToggle toggle = new DrawerToggle();
        toggle.getStyle()
                .set("color", "white");

        // Logo/Title
        H1 logo = new H1("ShikshaSpace");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.NONE
        );
        logo.getStyle()
                .set("color", "white")
                .set("font-weight", "600");

        // Profile menu
        MenuBar profileMenu = createProfileMenu();

        // Header layout
        HorizontalLayout header = new HorizontalLayout(toggle, logo);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.expand(logo);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM
        );
        header.getStyle()
                .set("background", "linear-gradient(135deg, #667eea 0%, #764ba2 100%)")
                .set("box-shadow", "0 2px 8px rgba(0,0,0,0.1)");

        // Add profile menu to header
        header.add(profileMenu);

        addToNavbar(header);
    }

    /**
     * Create profile dropdown menu
     */
    private MenuBar createProfileMenu() {
        MenuBar menuBar = new MenuBar();
        menuBar.setOpenOnHover(false);
        menuBar.getStyle()
                .set("background", "transparent")
                .set("color", "white");

        // Get current user info
        String username = SecurityUtils.getUsername();
        String email = SecurityUtils.getEmail();

        // User avatar
        Avatar avatar = new Avatar(username);
        avatar.getStyle()
                .set("background", "white")
                .set("color", "#667eea");

        // Username label
        Span usernameLabel = new Span(username);
        usernameLabel.getStyle()
                .set("color", "white")
                .set("margin-left", "10px")
                .set("font-weight", "500");

        // Create profile menu item with avatar and username
        HorizontalLayout profileItem = new HorizontalLayout(avatar, usernameLabel);
        profileItem.setAlignItems(FlexComponent.Alignment.CENTER);
        profileItem.setSpacing(false);

        MenuItem menuItem = menuBar.addItem(profileItem);
        SubMenu subMenu = menuItem.getSubMenu();

        // User info header in dropdown
        VerticalLayout userInfo = new VerticalLayout();
        userInfo.setPadding(true);
        userInfo.setSpacing(false);
        
        Span nameSpan = new Span(username);
        nameSpan.getStyle()
                .set("font-weight", "600")
                .set("font-size", "14px");
        
        Span emailSpan = new Span(email != null ? email : "");
        emailSpan.getStyle()
                .set("font-size", "12px")
                .set("color", "var(--lumo-secondary-text-color)");
        
        userInfo.add(nameSpan, emailSpan);
        subMenu.addItem(userInfo).setEnabled(false);
        
        subMenu.add(new Hr());

        // Edit Profile menu item
        MenuItem editProfile = subMenu.addItem(createMenuItemContent(
                VaadinIcon.USER, "Edit Profile"
        ));
        editProfile.addClickListener(e -> 
            getUI().ifPresent(ui -> ui.navigate("profile"))
        );

        // Logout menu item
        MenuItem logout = subMenu.addItem(createMenuItemContent(
                VaadinIcon.SIGN_OUT, "Logout"
        ));
        logout.addClickListener(e -> {
            SecurityUtils.logout();
            getUI().ifPresent(ui -> ui.navigate("login"));
        });

        return menuBar;
    }

    /**
     * Helper to create menu item with icon and text
     */
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

    /**
     * Create navigation drawer (sidebar)
     */
    private void createDrawer() {
        VerticalLayout drawerContent = new VerticalLayout();
        drawerContent.setSizeFull();
        drawerContent.setPadding(true);
        drawerContent.setSpacing(true);

        // Navigation links
        RouterLink homeLink = createNavLink(VaadinIcon.HOME, "Home", "home");
        
        // Placeholder for future navigation
        Span placeholder = new Span("More navigation items coming soon...");
        placeholder.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("font-size", "12px")
                .set("margin-top", "20px")
                .set("font-style", "italic");

        drawerContent.add(homeLink, placeholder);

        addToDrawer(drawerContent);
    }

    /**
     * Helper to create navigation link
     */
    private RouterLink createNavLink(VaadinIcon iconType, String text, String route) {
        Icon icon = iconType.create();
        Span label = new Span(text);

        RouterLink link = new RouterLink();
        link.add(icon, label);
        link.setRoute(HomePage.class); // Set route to HomePage class

        link.getStyle()
                .set("display", "flex")
                .set("align-items", "center")
                .set("gap", "10px")
                .set("padding", "10px 15px")
                .set("border-radius", "8px")
                .set("text-decoration", "none")
                .set("color", "var(--lumo-body-text-color)")
                .set("transition", "background 0.2s");

        return link;
    }
}
