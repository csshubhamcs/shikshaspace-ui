package com.shikshaspace.shikshaspaceui.service;

import com.shikshaspace.shikshaspaceui.dto.SpaceResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@Service
@RequiredArgsConstructor
public class SpaceService {

  private final WebClient spaceServiceClient;

  public List<SpaceResponse> getAllSpaces() {
    try {
      return spaceServiceClient
          .get()
          .uri("/api/v1/spaces")
          .retrieve()
          .bodyToFlux(SpaceResponse.class)
          .collectList()
          .block();
    } catch (Exception e) {
      log.error("Failed to get spaces: {}", e.getMessage());
      return List.of();
    }
  }

  public void joinSpace(UUID spaceId) {
    try {
      spaceServiceClient
          .post()
          .uri("/api/v1/spaces/{id}/join", spaceId)
          .retrieve()
          .bodyToMono(Void.class)
          .block();

      log.info("Joined space: {}", spaceId);
    } catch (Exception e) {
      log.error("Failed to join space: {}", e.getMessage());
    }
  }
}
