package com.shikshaspace.shikshaspaceui.security;

import com.shikshaspace.shikshaspaceui.dto.ExternalAuthRequest;
import com.shikshaspace.shikshaspaceui.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends OidcUserService {

  private final UserService userService;

  @Override
  public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
    OidcUser oidcUser = super.loadUser(userRequest);

    String keycloakId = oidcUser.getAttribute("sub");
    String email = oidcUser.getAttribute("email");
    String name = oidcUser.getAttribute("name");
    String provider = userRequest.getClientRegistration().getRegistrationId();

    log.info("OAuth2 login: email={}, provider={}", email, provider);

    ExternalAuthRequest request =
        ExternalAuthRequest.builder()
            .keycloakId(keycloakId)
            .email(email)
            .username(email != null ? email.split("@")[0] : "user")
            .firstName(name)
            .provider(provider)
            .build();

    try {
      userService.registerExternalUser(request);
      log.info("User registered/synced: {}", email);
    } catch (Exception e) {
      log.error("Failed to register external user", e);
    }

    return oidcUser;
  }
}
