package com.shikshaspace.shikshaspaceui.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalAuthRequest {

  private String keycloakId;
  private String email;
  private String username;
  private String firstName;
  private String lastName;
  private String provider;
}
