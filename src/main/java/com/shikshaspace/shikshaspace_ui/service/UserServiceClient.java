package com.shikshaspace.shikshaspace_ui.service;

import com.shikshaspace.shikshaspace_ui.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.UUID;

/**
 * REST client to communicate with User Service
 * Handles login, register, profile operations
 */
@Slf4j
@Service
public class UserServiceClient {

    private final WebClient webClient;
    private final long timeout;

    public UserServiceClient(
            @Value("${user.service.url}") String userServiceUrl,
            @Value("${user.service.timeout:30000}") long timeout) {
        
        this.timeout = timeout;
        this.webClient = WebClient.builder()
                .baseUrl(userServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
        
        log.info("UserServiceClient initialized with URL: {}", userServiceUrl);
    }

    /**
     * Login user and get JWT token
     */
    public Mono<AuthResponse> login(LoginRequest request) {
        log.debug("Attempting login for user: {}", request.getUsername());
        
        return webClient.post()
                .uri("/api/auth/login")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .timeout(Duration.ofMillis(timeout))
                .doOnSuccess(response -> log.info("Login successful for user: {}", request.getUsername()))
                .doOnError(error -> log.error("Login failed for user: {}", request.getUsername(), error));
    }

    /**
     * Register new user
     */
    public Mono<AuthResponse> register(RegisterRequest request) {
        log.debug("Attempting registration for user: {}", request.getUsername());
        
        return webClient.post()
                .uri("/api/auth/register")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .timeout(Duration.ofMillis(timeout))
                .doOnSuccess(response -> log.info("Registration successful for user: {}", request.getUsername()))
                .doOnError(error -> log.error("Registration failed for user: {}", request.getUsername(), error));
    }

    /**
     * Get current user profile
     */
    public Mono<UserProfileDTO> getUserProfile(String token) {
        log.debug("Fetching user profile");
        
        return webClient.get()
                .uri("/api/users/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .bodyToMono(UserProfileDTO.class)
                .timeout(Duration.ofMillis(timeout))
                .doOnSuccess(profile -> log.info("Profile fetched for user: {}", profile.getUsername()))
                .doOnError(error -> log.error("Failed to fetch user profile", error));
    }

    /**
     * Update user profile
     */
    public Mono<UserProfileDTO> updateProfile(String token, UserProfileDTO profile) {
        log.debug("Updating profile for user: {}", profile.getUsername());
        
        return webClient.put()
                .uri("/api/users/profile")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .bodyValue(profile)
                .retrieve()
                .bodyToMono(UserProfileDTO.class)
                .timeout(Duration.ofMillis(timeout))
                .doOnSuccess(updated -> log.info("Profile updated for user: {}", updated.getUsername()))
                .doOnError(error -> log.error("Failed to update profile", error));
    }

    public Mono<AuthResponse> refreshToken(String refreshToken) {
        log.debug("Refreshing access token");

        RefreshTokenRequest request = new RefreshTokenRequest(refreshToken);

        return webClient.post()
                .uri("/api/auth/refresh")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .timeout(Duration.ofMillis(timeout))
                .doOnSuccess(response -> log.info("Token refreshed successfully"))
                .doOnError(error -> log.error("Token refresh failed", error));
    }

    /**
     * Get current authenticated user profile
     */
    public Mono<UserResponse> getCurrentUserProfile(String jwtToken) {
        log.debug("Fetching current user profile");

        return webClient.get()
                .uri("/api/users/me")
                .header("Authorization", "Bearer " + jwtToken)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .timeout(Duration.ofMillis(timeout))
                .doOnSuccess(response -> log.info("Profile loaded for user: {}", response.getUsername()))
                .doOnError(error -> log.error("Failed to load profile", error));
    }

    /**
     * Update user profile
     */
    public Mono<UserResponse> updateProfile(UUID userId, UpdateProfileRequest request, String jwtToken) {
        log.debug("Updating profile for user: {}", userId);

        return webClient.put()
                .uri("/api/users/" + userId)
                .header("Authorization", "Bearer " + jwtToken)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(UserResponse.class)
                .timeout(Duration.ofMillis(timeout))
                .doOnSuccess(response -> log.info("Profile updated for user: {}", userId))
                .doOnError(error -> log.error("Failed to update profile for user: {}", userId, error));
    }
}
