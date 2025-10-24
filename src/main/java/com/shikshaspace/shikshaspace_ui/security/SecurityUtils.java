package com.shikshaspace.shikshaspace_ui.security;

import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

/**
 * Production-grade security utility for authentication management. Handles user authentication,
 * session management, and security context.
 */
@Slf4j
public class SecurityUtils {

  /** Authenticate user and store in Spring Security context and Vaadin session. */
  public static void authenticateUser(String username, UUID userId, String token, String email) {
    log.info("Authenticating user: {}", username);

    List<GrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(username, null, authorities);

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);

    // Store user info in Vaadin session
    VaadinSession session = VaadinSession.getCurrent();
    if (session != null) {
      session.setAttribute("userId", userId);
      session.setAttribute("username", username);
      session.setAttribute("email", email);
      session.setAttribute("jwttoken", token);
      log.debug("User session created for: {}", username);
    }
  }

  /** Get current authenticated username from security context. */
  public static String getUsername() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (authentication != null && authentication.isAuthenticated()) {
      String username = authentication.getName();
      if (!"anonymousUser".equals(username)) {
        return username;
      }
    }

    // Fallback to session
    VaadinSession session = VaadinSession.getCurrent();
    if (session != null) {
      return session.getAttribute("username") != null
          ? (String) session.getAttribute("username")
          : null;
    }

    return null;
  }

  /** Get current user's email from session. */
  public static String getEmail() {
    VaadinSession session = VaadinSession.getCurrent();
    if (session != null) {
      return session.getAttribute("email") != null ? (String) session.getAttribute("email") : null;
    }
    return null;
  }

  /** Get current user ID from session. */
  public static UUID getUserId() {
    VaadinSession session = VaadinSession.getCurrent();
    if (session != null) {
      return session.getAttribute("userId") != null ? (UUID) session.getAttribute("userId") : null;
    }
    return null;
  }

  /** Get JWT token from session. */
  public static String getToken() {
    VaadinSession session = VaadinSession.getCurrent();
    if (session != null) {
      return session.getAttribute("jwttoken") != null
          ? (String) session.getAttribute("jwttoken")
          : null;
    }
    return null;
  }

  /** Check if user is authenticated. */
  public static boolean isAuthenticated() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication != null
        && authentication.isAuthenticated()
        && !"anonymousUser".equals(authentication.getName());
  }

  /** Logout user - clear security context and session. */
  public static void logout() {
    log.info("Logging out user: {}", getUsername());

    // Clear Spring Security context
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);

    // Clear Vaadin session
    VaadinSession session = VaadinSession.getCurrent();
    if (session != null) {
      session.setAttribute("userId", null);
      session.setAttribute("username", null);
      session.setAttribute("email", null);
      session.setAttribute("jwttoken", null);
      session.setAttribute("refreshtoken", null);
      session.close();
    }

    SecurityContextHolder.clearContext();
    log.info("User logged out successfully");
  }
}
