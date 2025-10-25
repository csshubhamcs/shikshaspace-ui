package com.shikshaspace.shikshaspaceui.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SpaceResponse {

  private UUID id;
  private String title;
  private String subtitle;
  private String description;

  private UUID hostUserId;
  private String hostUsername;
  private String hostEmail;

  private LocalDateTime scheduledAt;
  private Integer durationMinutes;
  private Integer maxParticipants;
  private String category;

  private String status;
  private Boolean isPublic;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
