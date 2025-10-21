package com.shikshaspace.shikshaspace_ui.security;

import com.shikshaspace.shikshaspace_ui.views.LoginView;
// ✅ FIXED: RegisterView not RegistrationView
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Production-grade centralized authentication guard Intercepts ALL route navigations and enforces
 * authentication
 *
 * <p>Benefits: - Single place for authentication logic - No code duplication in views - Automatic
 * protection for new views - Easy to maintain and test
 *
 * @author ShikshaSpace Engineering Team
 * @version 1.0 (Production-Grade)
 */
@Slf4j
@Component
public class VaadinSecurityInitializer implements VaadinServiceInitListener {

  /** Public routes that don't require authentication Add new public routes here */
  private static final List<String> PUBLIC_ROUTES =
      List.of("login", "register", "oauth2", "login/oauth2");

  @Override
  public void serviceInit(ServiceInitEvent event) {
    log.info("Initializing Vaadin Security Guard");

    event
        .getSource()
        .addUIInitListener(
            uiEvent -> {
              uiEvent.getUI().addBeforeEnterListener(this::authenticateNavigation);
            });
  }

  private void authenticateNavigation(BeforeEnterEvent event) {
    String route = event.getLocation().getPath();

    log.debug("Navigation attempt to: {}", route);

    // Handle root route
    if (route == null || route.isEmpty() || route.equals("/")) {
      if (SecurityUtils.isUserLoggedIn()) {
        event.forwardTo("home");
      } else {
        event.rerouteTo(LoginView.class);
      }
      return;
    }

    // Allow public routes
    if (isPublicRoute(route)) {
      log.debug("Public route: {}", route);
      return;
    }

    // Check authentication for protected routes
    if (!SecurityUtils.isUserLoggedIn()) {
      log.warn("Unauthorized access: {}", route);
      event.rerouteTo(LoginView.class);
    }
  }

  /** Check if route is public (doesn't require authentication) */
  private boolean isPublicRoute(String route) {
    if (route == null || route.isEmpty()) {
      // Root route "/" requires authentication
      return false;
    }

    // Normalize route (remove leading/trailing slashes)
    String normalizedRoute = route.toLowerCase().replaceAll("^/+|/+$", "");

    // Check if route matches any public route
    return PUBLIC_ROUTES.stream()
        .anyMatch(
            publicRoute ->
                normalizedRoute.equals(publicRoute)
                    || normalizedRoute.startsWith(publicRoute + "/"));
  }
}
