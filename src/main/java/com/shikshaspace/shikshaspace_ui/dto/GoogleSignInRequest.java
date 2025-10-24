package com.shikshaspace.shikshaspace_ui.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Request DTO for Google Sign-In. */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoogleSignInRequest {
  private String googleIdToken;
}
