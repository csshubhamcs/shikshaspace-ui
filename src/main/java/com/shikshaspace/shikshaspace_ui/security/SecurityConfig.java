package com.shikshaspace.shikshaspace_ui.security;

import com.shikshaspace.shikshaspace_ui.views.LoginView;  // ADD THIS IMPORT
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Public static resources
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/images/**", "/icons/**", "/public/**")
                .permitAll()
        );

        // Vaadin security with login view
        http.with(VaadinSecurityConfigurer.vaadin(), configurer -> {
            configurer.loginView("/login");
        });

        // OAuth2 login with Keycloak
        http.oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
        );

        // Logout
        http.logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
        );

        return http.build();
    }
}
