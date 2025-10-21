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
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

@Slf4j
public class SecurityUtils {

  /** Authenticate user after login/registration */
  public static void authenticateUser(String username, UUID userId, String jwtToken, String email) {
    Authentication auth =
        new UsernamePasswordAuthenticationToken(
            username, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")));

    SecurityContext securityContext = SecurityContextHolder.getContext();
    securityContext.setAuthentication(auth);

    VaadinSession.getCurrent()
        .getSession()
        .setAttribute(
            HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, securityContext);

    VaadinSession.getCurrent().setAttribute("jwt_token", jwtToken);
    VaadinSession.getCurrent().setAttribute("username", username);
    VaadinSession.getCurrent().setAttribute("user_id", userId);
    VaadinSession.getCurrent().setAttribute("email", email);
  }

  /** Check if user is logged in */
  public static boolean isUserLoggedIn() {
    try {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();

      if (auth == null || !auth.isAuthenticated()) {
        return false;
      }

      // ✅ Handle OAuth2 users
      if (auth.getPrincipal() instanceof OAuth2User) {
        return true;
      }

      // Handle regular users
      if ("anonymousUser".equals(auth.getPrincipal())) {
        return false;
      }

      VaadinSession session = VaadinSession.getCurrent();
      if (session == null) {
        return false;
      }

      String token = (String) session.getAttribute("jwt_token");
      String username = (String) session.getAttribute("username");

      return token != null && username != null;

    } catch (Exception e) {
      log.error("Error checking login status", e);
      return false;
    }
  }

  /** Get username (works for both OAuth2 and regular users) */
  public static String getUsername() {
    try {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();

      // ✅ Handle OAuth2 users
      if (auth != null && auth.getPrincipal() instanceof OAuth2User) {
        OAuth2User oauth2User = (OAuth2User) auth.getPrincipal();
        String email = oauth2User.getAttribute("email");
        return email != null ? email.split("@")[0] : "User";
      }

      // Handle regular users
      VaadinSession session = VaadinSession.getCurrent();
      if (session != null) {
        String username = (String) session.getAttribute("username");
        if (username != null) {
          return username;
        }
      }

      if (auth != null && auth.getName() != null) {
        return auth.getName();
      }

      return "Anonymous";
    } catch (Exception e) {
      log.error("Error getting username", e);
      return "Anonymous";
    }
  }

  /** Get email (works for both OAuth2 and regular users) */
  public static String getEmail() {
    try {
      Authentication auth = SecurityContextHolder.getContext().getAuthentication();

      // ✅ Handle OAuth2 users
      if (auth != null && auth.getPrincipal() instanceof OAuth2User) {
        OAuth2User oauth2User = (OAuth2User) auth.getPrincipal();
        return oauth2User.getAttribute("email");
      }

      // Handle regular users
      VaadinSession session = VaadinSession.getCurrent();
      if (session != null) {
        return (String) session.getAttribute("email");
      }

      return null;
    } catch (Exception e) {
      log.error("Error getting email", e);
      return null;
    }
  }

  public static String getJwtToken() {
    if (VaadinSession.getCurrent() == null) {
      return null;
    }
    return (String) VaadinSession.getCurrent().getAttribute("jwt_token");
  }

  public static String getRefreshToken() {
    if (VaadinSession.getCurrent() == null) {
      return null;
    }
    return (String) VaadinSession.getCurrent().getAttribute("refresh_token");
  }

  public static UUID getUserId() {
    if (VaadinSession.getCurrent() == null) {
      return null;
    }
    return (UUID) VaadinSession.getCurrent().getAttribute("user_id");
  }

  public static void logout() {
    SecurityContextHolder.clearContext();
    VaadinSession session = VaadinSession.getCurrent();
    if (session != null) {
      session.close();
    }
  }

  public static boolean isAuthenticated() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal());
  }

  public static String getCurrentUsername() {
    if (!isAuthenticated()) {
      return null;
    }
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    return auth.getName();
  }
}
