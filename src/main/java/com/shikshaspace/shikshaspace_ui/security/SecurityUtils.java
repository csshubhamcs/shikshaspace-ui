package com.shikshaspace.shikshaspace_ui.security;

import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.Optional;

/**
 * Security utility methods for authentication and user info
 */
public class SecurityUtils {

    /**
     * Get current authenticated user
     */
    public static Optional<Authentication> getAuthentication() {
        SecurityContext context = SecurityContextHolder.getContext();
        return Optional.ofNullable(context.getAuthentication());
    }

    /**
     * Check if user is authenticated
     */
    public static boolean isAuthenticated() {
        return getAuthentication()
                .map(Authentication::isAuthenticated)
                .orElse(false);
    }

    /**
     * Get current username
     */
    public static String getUsername() {
        return getAuthentication()
                .map(auth -> {
                    if (auth.getPrincipal() instanceof OidcUser) {
                        OidcUser oidcUser = (OidcUser) auth.getPrincipal();
                        return oidcUser.getPreferredUsername();
                    }
                    return auth.getName();
                })
                .orElse("Anonymous");
    }

    /**
     * Get user email
     */
    public static String getEmail() {
        return getAuthentication()
                .map(auth -> {
                    if (auth.getPrincipal() instanceof OidcUser) {
                        OidcUser oidcUser = (OidcUser) auth.getPrincipal();
                        return oidcUser.getEmail();
                    }
                    return null;
                })
                .orElse(null);
    }

    /**
     * Get full name
     */
    public static String getFullName() {
        return getAuthentication()
                .map(auth -> {
                    if (auth.getPrincipal() instanceof OidcUser) {
                        OidcUser oidcUser = (OidcUser) auth.getPrincipal();
                        String firstName = oidcUser.getGivenName();
                        String lastName = oidcUser.getFamilyName();
                        if (firstName != null && lastName != null) {
                            return firstName + " " + lastName;
                        }
                        return oidcUser.getFullName();
                    }
                    return auth.getName();
                })
                .orElse("User");
    }


    /**
     * Logout current user
     */
    public static void logout() {
        VaadinServletRequest request = VaadinServletRequest.getCurrent();
        if (request != null) {
            SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
            logoutHandler.logout(
                    request.getHttpServletRequest(),
                    null,
                    null
            );
        }
        SecurityContextHolder.clearContext();
    }

    /**
     * Get access token from session
     */
    public static String getAccessToken() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            return (String) session.getAttribute("jwt_token");
        }
        return null;
    }

}
