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
    String profileImage = oAuth2User.getAttribute("picture");

    log.info("Google login successful for: {}", email);

    try {
      // Check if user exists in User Service
      try {
        userServiceClient.getUserProfile(email).block();
        log.info("User already exists in User Service: {}", email);
      } catch (Exception e) {
        // User doesn't exist, create them
        log.info("Creating new user in User Service: {}", email);

        RegisterRequest registerRequest = new RegisterRequest();
        registerRequest.setEmail(email);
        registerRequest.setUsername(email); // Use email as username for Google users
        registerRequest.setPassword(
            "GOOGLE_OAUTH_USER_" + System.currentTimeMillis()); // Random password
        registerRequest.setFirstName(firstName != null ? firstName : "User");
        registerRequest.setLastName(lastName != null ? lastName : "");

        userServiceClient.register(registerRequest).block();
        log.info("User created successfully in User Service: {}", email);
      }
    } catch (Exception e) {
      log.error("Failed to sync Google user to User Service: {}", email, e);
      // Don't block login even if User Service sync fails
    }

    // Redirect to home page
    setDefaultTargetUrl("/home");
    super.onAuthenticationSuccess(request, response, authentication);
  }
}
