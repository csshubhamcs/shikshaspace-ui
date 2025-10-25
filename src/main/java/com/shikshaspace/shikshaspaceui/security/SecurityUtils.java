package com.shikshaspace.shikshaspaceui.security;

import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

  public boolean isUserLoggedIn() {
    Authentication authentication = getAuthentication();
    return authentication != null
        && authentication.isAuthenticated()
        && !(authentication instanceof AnonymousAuthenticationToken);
  }

  public String getAuthenticatedUsername() {
    Authentication authentication = getAuthentication();
    if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
      return authentication.getName();
    }
    return null;
  }

  public void logout() {
    SecurityContextHolder.clearContext();
    VaadinServletRequest request = VaadinServletRequest.getCurrent();
    if (request != null) {
      request.getHttpServletRequest().getSession().invalidate();
    }
    VaadinSession session = VaadinSession.getCurrent();
    if (session != null) {
      session.close();
    }
  }

  private Authentication getAuthentication() {
    SecurityContext context = SecurityContextHolder.getContext();
    return context != null ? context.getAuthentication() : null;
  }
}
