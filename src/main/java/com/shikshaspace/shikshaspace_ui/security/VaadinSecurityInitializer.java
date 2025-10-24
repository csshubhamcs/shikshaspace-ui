package com.shikshaspace.shikshaspace_ui.security;

import com.shikshaspace.shikshaspace_ui.views.LoginView;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Production-grade authentication guard for all routes. Intercepts navigation and enforces
 * authentication.
 */
@Slf4j
@Component
public class VaadinSecurityInitializer implements VaadinServiceInitListener {

  /** Public routes that don't require authentication */
  private static final List<String> PUBLIC_ROUTES =
      List.of("login", "register", "oauth2", "login/oauth2");

  @Override
  public void serviceInit(ServiceInitEvent event) {
    log.info("🔵 Initializing Vaadin Security Guard");

    event
        .getSource()
        .addUIInitListener(
            uiEvent -> {
              uiEvent.getUI().addBeforeEnterListener(this::authenticateNavigation);
            });
  }

  private void authenticateNavigation(BeforeEnterEvent event) {
    String route = event.getLocation().getPath();

    log.debug("🔍 Navigation attempt to: {}", route);

    // Handle root route
    if (route == null || route.isEmpty() || route.equals("/")) {
      if (SecurityUtils.isAuthenticated()) {
        log.debug("✅ User authenticated - allowing access to home");
        return;
      } else {
        log.warn("❌ Unauthenticated access to home - redirecting to login");
        event.rerouteTo(LoginView.class);
      }
      return;
    }

    // Allow public routes
    if (isPublicRoute(route)) {
      log.debug("✅ Public route: {}", route);
      return;
    }

    // Check authentication for protected routes
    if (!SecurityUtils.isAuthenticated()) {
      log.warn("❌ Unauthorized access to: {} - redirecting to login", route);
      event.rerouteTo(LoginView.class);
    } else {
      log.debug("✅ Authenticated access to: {}", route);
    }
  }

  /** Check if route is public */
  private boolean isPublicRoute(String route) {
    if (route == null || route.isEmpty()) {
      return false;
    }

    String normalizedRoute = route.toLowerCase().replaceAll("^/+|/+$", "");

    return PUBLIC_ROUTES.stream()
        .anyMatch(
            publicRoute ->
                normalizedRoute.equals(publicRoute)
                    || normalizedRoute.startsWith(publicRoute + "/"));
  }
}
