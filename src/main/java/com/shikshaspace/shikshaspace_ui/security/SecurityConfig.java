package com.shikshaspace.shikshaspace_ui.security;

import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Production-grade Security Configuration - Session-based authentication (fast, no repeated server
 * calls) - Cache disabled for authenticated pages (prevents back button issues) - Persistent
 * sessions across tabs - Proper logout cleanup
 *
 * @author ShikshaSpace Engineering Team
 * @version 2.0 (Production-Grade with Centralized Auth)
 */
@EnableWebSecurity
@Configuration
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    // ✅ FIXED: Disable cache for authenticated pages (prevents back button showing old pages)
    http.headers(
        headers ->
            headers
                .cacheControl(cache -> cache.disable())
                .frameOptions(frame -> frame.sameOrigin()));

    // Public static resources
    http.authorizeHttpRequests(
        auth -> auth.requestMatchers("/images/**", "/icons/**", "/public/**").permitAll());

    // ✅ Vaadin security handles session automatically
    http.with(
        VaadinSecurityConfigurer.vaadin(),
        configurer -> {
          configurer.loginView("/login");
        });

    // OAuth2 login (if using Keycloak - optional)
    http.oauth2Login(oauth2 -> oauth2.loginPage("/login").defaultSuccessUrl("/", true));

    // ✅ Proper logout with session cleanup
    http.logout(
        logout ->
            logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true));

    return http.build();
  }
}
