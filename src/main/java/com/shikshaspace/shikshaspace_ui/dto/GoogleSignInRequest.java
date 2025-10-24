package com.shikshaspace.shikshaspace_ui.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** DTO for sending Google ID token to backend. */
@Data
@Builder
@NoArgsConstructor
public class GoogleSignInRequest {

  /** Google ID token from JavaScript SDK. */
  private String idToken;

  public GoogleSignInRequest(String idToken) {
    this.idToken = idToken;
  }
}
