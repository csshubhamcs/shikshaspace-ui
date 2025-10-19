package com.shikshaspace.shikshaspace_ui.service;

import com.vaadin.flow.server.VaadinSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import java.util.Collections;
import java.util.UUID;

public class SecurityUtils {

    /**
     * Authenticate user in Spring Security context
     * MUST be called after successful login/registration
     */
    public static void authenticateUser(String username, UUID userId, String jwtToken) {
        // Create Spring Security Authentication
        Authentication auth = new UsernamePasswordAuthenticationToken(
                username,
                null,
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );

        // Set in SecurityContext
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(auth);

        // Store in session for Spring Security
        VaadinSession.getCurrent().getSession().setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext
        );

        // Also store tokens for API calls
        VaadinSession.getCurrent().setAttribute("jwt_token", jwtToken);
        VaadinSession.getCurrent().setAttribute("username", username);
        VaadinSession.getCurrent().setAttribute("user_id", userId);
    }

    /**
     * Check if user is authenticated
     */
    public static boolean isAuthenticated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null && auth.isAuthenticated() && 
               !auth.getPrincipal().equals("anonymousUser");
    }

    /**
     * Get current username
     */
    public static String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth != null ? auth.getName() : null;
    }

    /**
     * Logout user
     */
    public static void logout() {
        SecurityContextHolder.clearContext();
        VaadinSession.getCurrent().getSession().invalidate();
    }
}
