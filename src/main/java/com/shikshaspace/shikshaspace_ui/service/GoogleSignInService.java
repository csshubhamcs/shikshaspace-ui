package com.shikshaspace.shikshaspace_ui.service;

import com.shikshaspace.shikshaspace_ui.dto.AuthResponse;
import com.shikshaspace.shikshaspace_ui.dto.GoogleSignInRequest;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Service for handling Google Sign-In OAuth2 flow. Communicates with User Service backend to
 * authenticate Google users.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleSignInService {

  private final WebClient.Builder webClientBuilder;

  @Value("${user.service.base-url:http://localhost:7501}")
  private String userServiceBaseUrl;

  /** Authenticate user with Google ID token. Backend automatically creates user if not exists. */
  public Mono<AuthResponse> authenticateWithGoogle(String googleIdToken) {
    log.info("Authenticating with Google Sign-In");

    GoogleSignInRequest request = new GoogleSignInRequest(googleIdToken);

    return webClientBuilder
        .build()
        .post()
        .uri(userServiceBaseUrl + "/api/auth/oauth2/google")
        .bodyValue(request)
        .retrieve()
        .bodyToMono(AuthResponse.class)
        .timeout(Duration.ofSeconds(15))
        .doOnSuccess(
            response ->
                log.info("Google authentication successful for user: {}", response.getUsername()))
        .doOnError(error -> log.error("Google authentication failed: {}", error.getMessage()));
  }
}
