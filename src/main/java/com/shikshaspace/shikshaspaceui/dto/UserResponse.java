package com.shikshaspace.shikshaspaceui.dto;

import java.math.BigDecimal;
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
public class UserResponse {

  private UUID id;
  private String username;
  private String email;
  private String firstName;
  private String lastName;

  private Integer age;
  private String bio;
  private BigDecimal experience;

  private String profileImageUrl;
  private String linkedinUrl;
  private String githubUrl;

  private Boolean emailVerified;
  private Boolean isActive;

  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
