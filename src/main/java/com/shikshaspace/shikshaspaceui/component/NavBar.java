package com.shikshaspace.shikshaspaceui.component;

import com.shikshaspace.shikshaspaceui.constants.Routes;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Hr;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/** Navigation Bar Component CSS: frontend/themes/shikshaspace/components/navbar.css */
public class NavBar extends HorizontalLayout {

  /** CSS class names - corresponds to navbar.css */
  private static class Styles {
    static final String NAVBAR = "navbar";
    static final String LOGO = "navbar__logo";
    static final String LINKS = "navbar__links";
    static final String LINK = "navbar__link";
    static final String PROFILE_MENU = "navbar__profile-menu";
    static final String AVATAR_CONTAINER = "navbar__avatar-container";
    static final String AVATAR = "navbar__avatar";
    static final String DROPDOWN = "navbar__dropdown";
    static final String DROPDOWN_HEADER = "navbar__dropdown-header";
    static final String DROPDOWN_USERNAME = "navbar__dropdown-username";
    static final String DROPDOWN_EMAIL = "navbar__dropdown-email";
    static final String DROPDOWN_DIVIDER = "navbar__dropdown-divider";
    static final String DROPDOWN_ITEM = "navbar__dropdown-item";
    static final String DROPDOWN_ITEM_DANGER = "navbar__dropdown-item--danger";
  }

  private VerticalLayout dropdown;
  private boolean isLoggedIn = true; // TODO: Get from session/security context

  public NavBar() {
    addClassName(Styles.NAVBAR);
    setWidthFull();
    setJustifyContentMode(JustifyContentMode.BETWEEN);
    setAlignItems(Alignment.CENTER);

    add(createLogo(), createNavLinks(), createProfileMenu());
  }

  private Span createLogo() {
    Span logo = new Span("ShikshaSpace");
    logo.addClassName(Styles.LOGO);
    logo.addClickListener(e -> navigateTo(Routes.HOME));
    return logo;
  }

  private HorizontalLayout createNavLinks() {
    HorizontalLayout links = new HorizontalLayout();
    links.addClassName(Styles.LINKS);
    links.setSpacing(true);

    links.add(
        createNavLink("Home", Routes.HOME),
        createNavLink("Explore", Routes.EXPLORE),
        createNavLink("About", Routes.ABOUT));

    return links;
  }

  private Span createNavLink(String text, String route) {
    Span link = new Span(text);
    link.addClassName(Styles.LINK);
    link.addClickListener(
        e -> {
          closeDropdown();
          navigateTo(route);
        });
    return link;
  }

  private Div createProfileMenu() {
    Div container = new Div();
    container.addClassName(Styles.PROFILE_MENU);

    Div avatarContainer = createAvatarContainer();
    dropdown = createDropdown();

    setupClickOutsideHandler(avatarContainer);

    container.add(avatarContainer, dropdown);
    return container;
  }

  private Div createAvatarContainer() {
    Div container = new Div();
    container.addClassName(Styles.AVATAR_CONTAINER);

    Avatar avatar = new Avatar("User"); // TODO: Get from session
    avatar.addClassName(Styles.AVATAR);

    container.add(avatar);
    container.addClickListener(e -> toggleDropdown());

    return container;
  }

  private VerticalLayout createDropdown() {
    VerticalLayout dropdown = new VerticalLayout();
    dropdown.addClassName(Styles.DROPDOWN);
    dropdown.setVisible(false);
    dropdown.setPadding(false);
    dropdown.setSpacing(false);

    dropdown.add(
        createUserHeader(),
        createDivider(),
        createDropdownItem("My ShikshaSpace", Routes.MY_SHIKSHA, false),
        createDropdownItem("Profile Edit", Routes.PROFILE_EDIT, false),
        createDivider(),
        createDropdownItem(isLoggedIn ? "Logout" : "Login", Routes.LOGIN, true));

    return dropdown;
  }

  private Div createUserHeader() {
    Div header = new Div();
    header.addClassName(Styles.DROPDOWN_HEADER);

    Span userName = new Span("User Name"); // TODO: Get from session
    userName.addClassName(Styles.DROPDOWN_USERNAME);

    Span userEmail = new Span("user@example.com"); // TODO: Get from session
    userEmail.addClassName(Styles.DROPDOWN_EMAIL);

    header.add(userName, userEmail);
    return header;
  }

  private Hr createDivider() {
    Hr divider = new Hr();
    divider.addClassName(Styles.DROPDOWN_DIVIDER);
    return divider;
  }

  private Div createDropdownItem(String text, String route, boolean isDanger) {
    Div item = new Div();
    item.addClassName(Styles.DROPDOWN_ITEM);
    if (isDanger) {
      item.addClassName(Styles.DROPDOWN_ITEM_DANGER);
    }
    item.setText(text);
    item.addClickListener(
        e -> {
          closeDropdown();
          navigateTo(route);
        });
    return item;
  }

  private void setupClickOutsideHandler(Div avatarContainer) {
    avatarContainer
        .getElement()
        .executeJs(
            """
                const avatarEl = this;
                const dropdownEl = $0;
                document.addEventListener('click', function(event) {
                    if (!avatarEl.contains(event.target) && !dropdownEl.contains(event.target)) {
                        dropdownEl.style.display = 'none';
                    }
                });
                """,
            dropdown.getElement());
  }

  private void toggleDropdown() {
    dropdown.setVisible(!dropdown.isVisible());
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
