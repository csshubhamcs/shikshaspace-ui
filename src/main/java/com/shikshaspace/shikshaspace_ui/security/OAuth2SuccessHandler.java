package com.shikshaspace.shikshaspace_ui.security;

import com.shikshaspace.shikshaspace_ui.dto.RegisterRequest;
import com.shikshaspace.shikshaspace_ui.service.UserServiceClient;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private final UserServiceClient userServiceClient;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException, ServletException {

    OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

    String email = oAuth2User.getAttribute("email");
    String firstName = oAuth2User.getAttribute("given_name");
    String lastName = oAuth2User.getAttribute("family_name");

    log.info("✅ Google login successful for: {}", email);

    try {
      // Check if user exists
      try {
        userServiceClient.getUserProfile(email).block();
        log.info("✅ User exists: {}", email);
      } catch (Exception e) {
        log.info("Creating new user: {}", email);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(email);
        registerRequest.setUsername(email);
        registerRequest.setPassword("GOOGLE_OAUTH_" + System.currentTimeMillis());
        registerRequest.setFirstName(firstName != null ? firstName : "User");
        registerRequest.setLastName(lastName != null ? lastName : "");

        userServiceClient.register(registerRequest).block();
        log.info("✅ User created: {}", email);
      }
    } catch (Exception e) {
      log.error("❌ Failed to sync Google user: {}", email, e);
    }

    setDefaultTargetUrl("/home");
    super.onAuthenticationSuccess(request, response, authentication);
  }
}
