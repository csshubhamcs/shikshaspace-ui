package com.shikshaspace.shikshaspace_ui.dto;

import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
  private String token;
  private String refreshToken;
  private Long expiresIn;
  private UUID userId;
  private String username;
  private String email;
}
