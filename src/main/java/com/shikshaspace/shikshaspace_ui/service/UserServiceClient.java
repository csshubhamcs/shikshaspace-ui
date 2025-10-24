package com.shikshaspace.shikshaspace_ui.service;

import com.shikshaspace.shikshaspace_ui.dto.AuthResponse;
import com.shikshaspace.shikshaspace_ui.dto.LoginRequest;
import com.shikshaspace.shikshaspace_ui.dto.RegisterRequest;
import com.shikshaspace.shikshaspace_ui.dto.UserResponse;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Production-grade HTTP client for User Service backend communication. Handles authentication, user
 * management, and API calls with proper error handling.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceClient {

  private final WebClient.Builder webClientBuilder;

  @Value("${user.service.base-url}")
  private String userServiceBaseUrl;

  @Value("${user.service.timeout:30000}")
  private long timeout;

  /** Register new user with username/password. */
  public Mono<AuthResponse> register(RegisterRequest request) {
    log.info("Registering user: {}", request.getUsername());

    return webClientBuilder
        .build()
        .post()
        .uri(userServiceBaseUrl + "/api/auth/register")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .retrieve()
        .bodyToMono(AuthResponse.class)
        .timeout(Duration.ofMillis(timeout))
        .doOnSuccess(response -> log.info("User registered: {}", response.getUsername()))
        .doOnError(error -> log.error("Registration failed: {}", error.getMessage()));
  }

  /** Login with username/password. */
  public Mono<AuthResponse> login(LoginRequest request) {
    log.info("Logging in user: {}", request.getUsername());

    return webClientBuilder
        .build()
        .post()
        .uri(userServiceBaseUrl + "/api/auth/login")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(request)
        .retrieve()
        .bodyToMono(AuthResponse.class)
        .timeout(Duration.ofMillis(timeout))
        .doOnSuccess(response -> log.info("User logged in: {}", response.getUsername()))
        .doOnError(error -> log.error("Login failed: {}", error.getMessage()));
  }

  /** Get user profile by ID with JWT authentication. */
  public Mono<UserResponse> getUserProfile(String userId, String token) {
    log.debug("Fetching user profile: {}", userId);

    return webClientBuilder
        .build()
        .get()
        .uri(userServiceBaseUrl + "/api/users/" + userId)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .retrieve()
        .bodyToMono(UserResponse.class)
        .timeout(Duration.ofMillis(timeout))
        .doOnError(error -> log.error("Failed to fetch profile: {}", error.getMessage()));
  }

  /** Get current user profile (using /me endpoint). */
  public Mono<UserResponse> getCurrentUserProfile(String token) {
    log.debug("Fetching current user profile");

    return webClientBuilder
        .build()
        .get()
        .uri(userServiceBaseUrl + "/api/users/me")
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .retrieve()
        .bodyToMono(UserResponse.class)
        .timeout(Duration.ofMillis(timeout))
        .doOnError(error -> log.error("Failed to fetch current profile: {}", error.getMessage()));
  }

  /** Update user profile with JWT authentication. */
  public Mono<UserResponse> updateProfile(String userId, Object updateRequest, String token) {
    log.info("Updating user profile: {}", userId);

    return webClientBuilder
        .build()
        .put()
        .uri(userServiceBaseUrl + "/api/users/" + userId)
        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(updateRequest)
        .retrieve()
        .bodyToMono(UserResponse.class)
        .timeout(Duration.ofMillis(timeout))
        .doOnSuccess(response -> log.info("Profile updated: {}", response.getUsername()))
        .doOnError(error -> log.error("Profile update failed: {}", error.getMessage()));
  }

  /** Refresh access token using refresh token. */
  public Mono<AuthResponse> refreshToken(String refreshToken) {
    log.debug("Refreshing access token");

    return webClientBuilder
        .build()
        .post()
        .uri(userServiceBaseUrl + "/api/auth/refresh")
        .contentType(MediaType.APPLICATION_JSON)
        .bodyValue(new RefreshTokenRequest(refreshToken))
        .retrieve()
        .bodyToMono(AuthResponse.class)
        .timeout(Duration.ofMillis(timeout))
        .doOnSuccess(response -> log.info("Token refreshed successfully"))
        .doOnError(error -> log.error("Token refresh failed: {}", error.getMessage()));
  }

  /** Simple DTO for refresh token request. */
  private record RefreshTokenRequest(String refreshToken) {}
}
