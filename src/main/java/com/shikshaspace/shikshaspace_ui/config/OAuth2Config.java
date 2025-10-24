package com.shikshaspace.shikshaspace_ui.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * OAuth2 configuration for Google Sign-In integration. Stores Google Client ID for frontend
 * authentication.
 */
@Getter
@Configuration
public class OAuth2Config {

  @Value("${google.oauth2.client-id}")
  private String googleClientId;

  @Value("${google.oauth2.redirect-uri}")
  private String redirectUri;
}
