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

    // ✅ 1. Headers FIRST
    http.headers(
        headers ->
            headers
                .cacheControl(cache -> cache.disable())
                .frameOptions(frame -> frame.sameOrigin()));

    // ✅ 2. Vaadin Security BEFORE authorizeHttpRequests
    http.with(
        VaadinSecurityConfigurer.vaadin(),
        configurer -> {
          configurer.loginView("/login");
        });

    //        // ✅ 3. OAuth2 login (manual trigger from button)
    //        http.oauth2Login(oauth2 -> oauth2
    //                .loginPage("/login")
    //                .successHandler(oAuth2SuccessHandler)
    //                .defaultSuccessUrl("/home", true));

    http.oauth2Login(oauth2 -> oauth2.loginPage("/login").defaultSuccessUrl("/home", true));

    // ✅ 4. Logout
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
