/**
 * Google Sign-In integration for Vaadin
 * Uses Google Identity Services (2025 standard)
 */

// Load Google Identity Services library
(function() {
    const script = document.createElement('script');
    script.src = 'https://accounts.google.com/gsi/client';
    script.async = true;
    script.defer = true;
    document.head.appendChild(script);
})();

// Initialize Google Sign-In
window.initGoogleSignIn = function(clientId, callback) {
    google.accounts.id.initialize({
        client_id: clientId,
        callback: callback,
        auto_select: false,
        cancel_on_tap_outside: true
    });
};

// Render Google Sign-In button
window.renderGoogleButton = function(elementId, theme, size) {
    google.accounts.id.renderButton(
        document.getElementById(elementId),
        {
            theme: theme || 'outline',
            size: size || 'large',
            text: 'signin_with',
            shape: 'rectangular',
            logo_alignment: 'left'
        }
    );
};

// Programmatic sign-in (for custom button)
window.promptGoogleSignIn = function() {
    google.accounts.id.prompt();
};
