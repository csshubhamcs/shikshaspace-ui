package com.shikshaspace.shikshaspace_ui.security;

import com.shikshaspace.shikshaspace_ui.views.LoginView;
import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Production-grade security configuration for Vaadin 24.9+ Flow application. Uses
 * VaadinSecurityConfigurer (replaces deprecated VaadinWebSecurity).
 */
@Configuration
@EnableWebSecurity
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
public class SecurityConfig {

  /**
   * Configure security filter chain with Vaadin integration. Uses VaadinSecurityConfigurer for
   * automatic Vaadin-related security setup.
   */
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    // Allow access to public endpoints and static resources
    http.authorizeHttpRequests(
        auth ->
            auth.requestMatchers("/images/**")
                .permitAll()
                .requestMatchers("/styles/**")
                .permitAll()
                .requestMatchers("/VAADIN/**")
                .permitAll()
                .requestMatchers("/frontend/**")
                .permitAll()
                .requestMatchers("/themes/**")
                .permitAll()
                .requestMatchers("/sw.js")
                .permitAll()
                .requestMatchers("/manifest.webmanifest")
                .permitAll()
                .requestMatchers("/icons/**")
                .permitAll()
                .requestMatchers("/favicon.ico")
                .permitAll()
                .requestMatchers("/offline.html")
                .permitAll()
                .requestMatchers("/error")
                .permitAll());

    // Configure Vaadin security with automatic CSRF and navigation control
    http.with(
        VaadinSecurityConfigurer.vaadin(),
        configurer -> {
          configurer.loginView(LoginView.class);
          // Automatically handles:
          // - CSRF configuration for Vaadin internal requests
          // - Navigation access control (@PermitAll, @RolesAllowed, etc.)
          // - Static resource bypassing
          // - Vaadin-specific exception handling
        });

    return http.build();
  }
}
