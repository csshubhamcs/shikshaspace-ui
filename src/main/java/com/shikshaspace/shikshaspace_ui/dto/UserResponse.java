package com.shikshaspace.shikshaspace_ui.dto;

import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** User profile response DTO matching User Service API */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

  private UUID id;
  private String username;
  private String email;
  private String firstName;
  private String lastName;

  // Optional profile fields
  private Integer age;
  private String bio;
  private Double experience;

  // Social links
  private String profileImageUrl;
  private String linkedinUrl;
  private String githubUrl;

  // Metadata
  private Boolean isActive;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
