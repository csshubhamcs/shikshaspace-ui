package com.shikshaspace.shikshaspace_ui.security;

import com.vaadin.flow.server.VaadinSession;
import java.util.Collections;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

/** Production-grade security utilities Single source of truth for authentication */
@Slf4j
public class SecurityUtils {

  /**
   * Authenticate user in Spring Security context after successful login/registration This prevents
   * redirect loop issues
   */
  public static void authenticateUser(String username, UUID userId, String jwtToken, String email) {
    // Create Spring Security Authentication object
    Authentication auth =
        new UsernamePasswordAuthenticationToken(
            username,
            null, // credentials not needed after authentication
            Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

    // Set in Spring SecurityContext
    SecurityContext securityContext = SecurityContextHolder.getContext();
    securityContext.setAuthentication(auth);

    // Store SecurityContext in HTTP session for Spring Security
    VaadinSession.getCurrent()
        .getSession()
        .setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

    // Store tokens and user data in VaadinSession for API calls
    VaadinSession.getCurrent().setAttribute("jwt_token", jwtToken);
    VaadinSession.getCurrent().setAttribute("username", username);
    VaadinSession.getCurrent().setAttribute("user_id", userId);
    VaadinSession.getCurrent().setAttribute("email", email);
  }

  /** Check if user is authenticated */
  public static boolean isAuthenticated() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
  }

  /** Get current authenticated username */
  public static String getCurrentUsername() {
    if (!isAuthenticated()) {
      return null;
    }
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getName();
  }

  /** Get JWT token from session REQUIRED BY: ProfileView, API calls */
  public static String getJwtToken() {
    if (VaadinSession.getCurrent() == null) {
      return null;
    }
    return (String) VaadinSession.getCurrent().getAttribute("jwt_token");
  }

  /** Get refresh token from session */
  public static String getRefreshToken() {
    if (VaadinSession.getCurrent() == null) {
      return null;
    }
    return (String) VaadinSession.getCurrent().getAttribute("refresh_token");
  }

  /** Get user ID from session */
  public static UUID getUserId() {
    if (VaadinSession.getCurrent() == null) {
      return null;
    }
    return (UUID) VaadinSession.getCurrent().getAttribute("user_id");
  }

  /** Get username from session REQUIRED BY: MainLayout */
  public static String getUsername() {
    if (VaadinSession.getCurrent() == null) {
      return "Anonymous";
    }
    String username = (String) VaadinSession.getCurrent().getAttribute("username");
    return username != null ? username : "Anonymous";
  }

  /**
   * Get user email from session REQUIRED BY: MainLayout Note: Email is not stored in session
   * currently, needs to be added during login
   */
  public static String getEmail() {
    if (VaadinSession.getCurrent() == null) {
      return null;
    }
    return (String) VaadinSession.getCurrent().getAttribute("email");
  }

  /** Logout - Clear all authentication and redirect Works across all tabs (same browser) */
  public static void logout() {
    // Clear Spring Security context
    SecurityContextHolder.clearContext();

    // Invalidate VaadinSession
    VaadinSession session = VaadinSession.getCurrent();
    if (session != null) {
      session.close(); // ✅ This closes session for ALL tabs
    }
  }

  /** Check if user is currently logged in Used by LoginView and HomePage */
  public static boolean isUserLoggedIn() {
    try {
      // Check Spring Security authentication
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();
      boolean hasSpringAuth =
          auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());

      // Check VaadinSession exists
      VaadinSession session = VaadinSession.getCurrent();
      if (session == null) {
        return false;
      }

      // Check session has valid token and username
      String token = (String) session.getAttribute("jwt_token");
      String username = (String) session.getAttribute("username");

      boolean hasValidSession =
          token != null && !token.isBlank() && username != null && !username.isBlank();

      // User is logged in if BOTH Spring Security AND session are valid
      return hasSpringAuth && hasValidSession;

    } catch (Exception e) {
      log.error("Error checking login status", e);
      return false;
    }
  }
}
