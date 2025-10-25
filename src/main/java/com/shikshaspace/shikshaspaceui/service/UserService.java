package com.shikshaspace.shikshaspaceui.service;

import com.shikshaspace.shikshaspaceui.dto.ExternalAuthRequest;
import com.shikshaspace.shikshaspaceui.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

  private final WebClient userServiceClient;

  public void registerExternalUser(ExternalAuthRequest request) {
    try {
      userServiceClient
          .post()
          .uri("/api/v1/auth/external")
          .bodyValue(request)
          .retrieve()
          .bodyToMono(UserResponse.class)
          .block();

      log.info("External user registered: {}", request.getEmail());
    } catch (Exception e) {
      log.error("Failed to register external user: {}", e.getMessage());
    }
  }

  public UserResponse getCurrentUser(String username) {
    try {
      return userServiceClient
          .get()
          .uri("/api/v1/users/me")
          .retrieve()
          .bodyToMono(UserResponse.class)
          .block();
    } catch (Exception e) {
      log.error("Failed to get current user: {}", e.getMessage());
      return null;
    }
  }
}
