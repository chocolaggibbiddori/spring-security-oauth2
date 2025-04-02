package chocola.security.oauth2.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.ClientRegistrations;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
            .build();
    }

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(keycloakClientRegistration());
    }

    private ClientRegistration keycloakClientRegistration() {
        return ClientRegistrations
            .fromIssuerLocation("http://localhost:8080/realms/OAuth2")
            .registrationId("keycloak")
            .clientId("oauth2-client-app")
            .clientSecret("PJrMtfY8Yv1SWyKCGgBXtFFzRvh1pTHU")
            .redirectUri("http://localhost:8081/login/oauth2/code/keycloak")
            .build();
    }
}
