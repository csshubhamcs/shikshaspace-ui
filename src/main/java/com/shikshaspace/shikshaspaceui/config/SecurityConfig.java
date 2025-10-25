package com.shikshaspace.shikshaspaceui.config;

import com.shikshaspace.shikshaspaceui.views.auth.LoginView;
import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
public class SecurityConfig {

  @Bean
  public SecurityFilterChain securityFilterChain(
      HttpSecurity http, OidcUserService customOAuth2UserService) throws Exception {

    http.authorizeHttpRequests(auth -> auth.requestMatchers("/public/**").permitAll());

    http.with(
        VaadinSecurityConfigurer.vaadin(), configurer -> configurer.loginView(LoginView.class));

    http.oauth2Login(
        oauth2 ->
            oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .userInfoEndpoint(userInfo -> userInfo.oidcUserService(customOAuth2UserService)));

    return http.build();
  }
}
