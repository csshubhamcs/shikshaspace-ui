package com.shikshaspace.shikshaspace_ui.security;

import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
public class SecurityConfig {

  private final OAuth2SuccessHandler oAuth2SuccessHandler;

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

    // Headers
    http.headers(
        headers ->
            headers
                .cacheControl(cache -> cache.disable())
                .frameOptions(frame -> frame.sameOrigin()));

    // Public resources
    http.authorizeHttpRequests(
        auth -> auth.requestMatchers("/images/**", "/icons/**", "/public/**").permitAll());

    // ✅ Vaadin Security with OAuth2
    http.with(
        VaadinSecurityConfigurer.vaadin(),
        configurer -> {
          configurer.oauth2LoginPage("/oauth2/authorization/keycloak");
        });

    // OAuth2 login with success handler
    http.oauth2Login(
        oauth2 ->
            oauth2
                .successHandler(oAuth2SuccessHandler)
                .defaultSuccessUrl("/home", true)); // ✅ Changed to /home

    // Logout
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
