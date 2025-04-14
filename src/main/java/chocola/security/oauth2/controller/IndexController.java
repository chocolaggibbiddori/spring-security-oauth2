package chocola.security.oauth2.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@Slf4j
@RestController
@RequiredArgsConstructor
public class IndexController {

    private final ClientRegistrationRepository clientRegistrationRepository;

    @GetMapping("/")
    public String index() {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("keycloak");
        log.info("clientRegistration = {}", clientRegistration);
        return "index";
    }

    @GetMapping("/login-page")
    public String loginPage() {
        return "Login page";
    }

    @GetMapping("/user")
    public OAuth2User user(String accessToken) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("keycloak");
        OAuth2AccessToken oAuth2AccessToken =
                new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessToken, Instant.now(), Instant.MAX);
        OAuth2UserRequest oAuth2UserRequest = new OAuth2UserRequest(clientRegistration, oAuth2AccessToken);
        DefaultOAuth2UserService oAuth2UserService = new DefaultOAuth2UserService();

        return oAuth2UserService.loadUser(oAuth2UserRequest);
    }

    @GetMapping("/oidc")
    public OAuth2User oidc(String accessToken, String idToken) {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("keycloak");
        OAuth2AccessToken oAuth2AccessToken =
                new OAuth2AccessToken(OAuth2AccessToken.TokenType.BEARER, accessToken, Instant.now(), Instant.MAX);
        OidcIdToken oidcIdToken = OidcIdToken
                .withTokenValue(idToken)
                .issuer("http://localhost:8080/realms/OAuth2")
                .subject("78d9ddec-6325-42da-b130-6c313e2cc1c2")
                .claim("preferred_username", "user")
                .build();
        OidcUserRequest oidcUserRequest = new OidcUserRequest(clientRegistration, oAuth2AccessToken, oidcIdToken);
        OidcUserService oidcUserService = new OidcUserService();

        return oidcUserService.loadUser(oidcUserRequest);
    }
}
