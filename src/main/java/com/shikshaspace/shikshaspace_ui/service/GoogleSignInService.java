package com.shikshaspace.shikshaspace_ui.service;

import com.shikshaspace.shikshaspace_ui.dto.AuthResponse;
import com.shikshaspace.shikshaspace_ui.dto.GoogleSignInRequest;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.page.Page;
import java.time.Duration;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * Service for handling Google Sign-In OAuth2 flow.
 * Handles both JavaScript SDK initialization and backend authentication.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GoogleSignInService {

    private final WebClient.Builder webClientBuilder;

    @Value("${user.service.base-url:http://localhost:7501}")
    private String userServiceBaseUrl;

    @Value("${google.oauth2.client-id}")
    private String googleClientId;

    /**
     * Initialize Google Sign-In button with JavaScript SDK.
     * This method loads the Google library and renders the sign-in button.
     */
    public void initializeGoogleSignIn(Consumer<String> onSuccess) {
        UI ui = UI.getCurrent();
        if (ui == null) {
            log.error("❌ No UI context available for Google Sign-In initialization");
            return;
        }

        Page page = ui.getPage();

        // Load Google Sign-In library and initialize
        String googleScript = String.format("""
        <script src="https://accounts.google.com/gsi/client" async defer></script>
        <script>
        window.onGoogleLibraryLoad = function() {
            google.accounts.id.initialize({
                client_id: '%s',
                callback: handleCredentialResponse
            });
            
            google.accounts.id.renderButton(
                document.getElementById('google-signin-button'),
                { 
                    theme: 'filled_blue',
                    size: 'large',
                    width: '100%%',
                    text: 'signin_with'
                }
            );
        };
        
        function handleCredentialResponse(response) {
            if (response.credential) {
                window.handleGoogleCallback(response.credential);
            } else {
                console.error('No credential received from Google');
            }
        }
        
        if (typeof google !== 'undefined') {
            window.onGoogleLibraryLoad();
        }
        </script>
        """, googleClientId);

        page.executeJs(googleScript);

        // Register callback from JavaScript to Java
        page.executeJs("""
        window.handleGoogleCallback = function(idToken) {
            $0.$server.onGoogleSignIn(idToken);
        }
        """, ui);

        log.info("✅ Google Sign-In initialized with client ID: {}...",
                googleClientId.substring(0, Math.min(20, googleClientId.length())));
    }

    /**
     * Authenticate user with Google ID token.
     * Backend automatically creates user if not exists.
     */
    public Mono<AuthResponse> authenticateWithGoogle(String googleIdToken) {
        log.info("🔵 Authenticating with Google Sign-In");

        GoogleSignInRequest request = new GoogleSignInRequest(googleIdToken);

        return webClientBuilder
                .build()
                .post()
                .uri(userServiceBaseUrl + "/api/auth/oauth2/google")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .timeout(Duration.ofSeconds(15))
                .doOnSuccess(response ->
                        log.info("✅ Google authentication successful for user: {}", response.getUsername()))
                .doOnError(error ->
                        log.error("❌ Google authentication failed: {}", error.getMessage()));
    }
}
