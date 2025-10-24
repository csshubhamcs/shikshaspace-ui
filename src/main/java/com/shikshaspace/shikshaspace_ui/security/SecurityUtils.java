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

/** FIXED Security utility - properly checks authentication. */
@Slf4j
public class SecurityUtils {

  /** Authenticate user and store in session. */
  public static void authenticateUser(String username, UUID userId, String token, String email) {
    log.info("🔵 Authenticating user: {}", username);

    // Set Spring Security context
    List<GrantedAuthority> authorities =
        Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));

    UsernamePasswordAuthenticationToken authentication =
        new UsernamePasswordAuthenticationToken(username, null, authorities);

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);

    // Store in Vaadin session
    VaadinSession session = VaadinSession.getCurrent();
    if (session != null) {
      session.setAttribute("userId", userId);
      session.setAttribute("username", username);
      session.setAttribute("email", email);
      session.setAttribute("jwttoken", token);
      session.setAttribute("authenticated", true); // ADD THIS FLAG
      log.info("✅ Session created for: {}", username);
    }
  }

  /** Check if user is authenticated - CRITICAL FIX. */
  public static boolean isAuthenticated() {
    // First check Vaadin session flag
    VaadinSession session = VaadinSession.getCurrent();
    if (session != null) {
      Boolean authenticated = (Boolean) session.getAttribute("authenticated");
      String username = (String) session.getAttribute("username");
      String token = (String) session.getAttribute("jwttoken");

      // User is authenticated ONLY if flag is true AND has username AND token
      boolean sessionAuth =
          Boolean.TRUE.equals(authenticated)
              && username != null
              && !username.isEmpty()
              && token != null
              && !token.isEmpty();

      if (sessionAuth) {
        log.debug("✅ User authenticated via session: {}", username);
        return true;
      }
    }

    // Fallback: Check Spring Security context
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null
        && authentication.isAuthenticated()
        && !"anonymousUser".equals(authentication.getName())) {
      log.debug("✅ User authenticated via Security context: {}", authentication.getName());
      return true;
    }

    log.debug("❌ User NOT authenticated");
    return false;
  }

  /** Get current username. */
  public static String getUsername() {
    VaadinSession session = VaadinSession.getCurrent();
    if (session != null) {
      return (String) session.getAttribute("username");
    }

    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (authentication != null && authentication.isAuthenticated()) {
      String username = authentication.getName();
      if (!"anonymousUser".equals(username)) {
        return username;
      }
    }

    return null;
  }

  public static String getEmail() {
    VaadinSession session = VaadinSession.getCurrent();
    return session != null ? (String) session.getAttribute("email") : null;
  }

  public static UUID getUserId() {
    VaadinSession session = VaadinSession.getCurrent();
    return session != null ? (UUID) session.getAttribute("userId") : null;
  }

  public static String getToken() {
    VaadinSession session = VaadinSession.getCurrent();
    return session != null ? (String) session.getAttribute("jwttoken") : null;
  }

  /** Logout user - clear everything. */
  public static void logout() {
    String username = getUsername();
    log.info("🔵 Logging out user: {}", username);

    // Clear Vaadin session
    VaadinSession session = VaadinSession.getCurrent();
    if (session != null) {
      session.setAttribute("userId", null);
      session.setAttribute("username", null);
      session.setAttribute("email", null);
      session.setAttribute("jwttoken", null);
      session.setAttribute("authenticated", null); // CLEAR FLAG
    }

    // Clear Security context
    SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
    logoutHandler.logout(VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null);

    SecurityContextHolder.clearContext();
    log.info("✅ User logged out successfully");
  }
}
