package com.shikshaspace.shikshaspace_ui.security;

import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Production-grade security utility with proper session management.
 */
@Slf4j
public class SecurityUtils {

    /**
     * Authenticate user and store in both Spring Security and Vaadin session.
     */
    public static void authenticateUser(String username, UUID userId, String token, String email) {
        log.info("🔵 Authenticating user: {}", username);

        // Set Spring Security context
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_USER")
        );

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
            log.info("✅ Session created for: {}", username);
        }

        // Verify authentication was set
        log.info("✅ Authentication status: {}", isAuthenticated());
    }

    /**
     * Check if user is authenticated - checks BOTH Spring Security AND session.
     */
    public static boolean isAuthenticated() {
        // Check Spring Security context
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null
                && authentication.isAuthenticated()
                && !"anonymousUser".equals(authentication.getName())) {
            return true;
        }

        // Fallback: Check Vaadin session
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            String username = session.getAttribute("username") != null
                    ? (String) session.getAttribute("username")
                    : null;
            String token = session.getAttribute("jwttoken") != null
                    ? (String) session.getAttribute("jwttoken")
                    : null;

            boolean sessionAuth = username != null && token != null;
            if (sessionAuth) {
                log.debug("✅ User authenticated via session: {}", username);
            }
            return sessionAuth;
        }

        return false;
    }

    /**
     * Get current authenticated username.
     */
    public static String getUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            if (!"anonymousUser".equals(username)) {
                return username;
            }
        }

        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            return session.getAttribute("username") != null
                    ? (String) session.getAttribute("username")
                    : null;
        }

        return null;
    }

    /**
     * Get current user's email from session.
     */
    public static String getEmail() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            return session.getAttribute("email") != null
                    ? (String) session.getAttribute("email")
                    : null;
        }
        return null;
    }

    /**
     * Get current user ID from session.
     */
    public static UUID getUserId() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            return session.getAttribute("userId") != null
                    ? (UUID) session.getAttribute("userId")
                    : null;
        }
        return null;
    }

    /**
     * Get JWT token from session.
     */
    public static String getToken() {
        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            return session.getAttribute("jwttoken") != null
                    ? (String) session.getAttribute("jwttoken")
                    : null;
        }
        return null;
    }

    /**
     * Logout user - clear all authentication.
     */
    public static void logout() {
        log.info("🔵 Logging out user: {}", getUsername());

        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(),
                null,
                null
        );

        VaadinSession session = VaadinSession.getCurrent();
        if (session != null) {
            session.setAttribute("userId", null);
            session.setAttribute("username", null);
            session.setAttribute("email", null);
            session.setAttribute("jwttoken", null);
            session.close();
        }

        SecurityContextHolder.clearContext();
        log.info("✅ User logged out successfully");
    }
}
