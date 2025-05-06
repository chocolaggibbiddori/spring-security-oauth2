package chocola.security.oauth2;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.function.Consumer;

public class CustomOAuth2AuthorizationRequestResolver implements OAuth2AuthorizationRequestResolver {

    private static final String REGISTRATION_ID_URI_VARIABLE_NAME = "registrationId";
    private static final Consumer<OAuth2AuthorizationRequest.Builder> DEFAULT_PKCE_APPLIER = OAuth2AuthorizationRequestCustomizers.withPkce();

    private final ClientRegistrationRepository clientRegistrationRepository;
    private final String baseUri;
    private final OAuth2AuthorizationRequestResolver defaultOAuth2AuthorizationRequestResolver;
    private final RequestMatcher authorizationRequestMatcher;

    public CustomOAuth2AuthorizationRequestResolver(ClientRegistrationRepository clientRegistrationRepository, String baseUri) {
        this.clientRegistrationRepository = clientRegistrationRepository;
        this.baseUri = baseUri;
        this.defaultOAuth2AuthorizationRequestResolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, baseUri);
        this.authorizationRequestMatcher = new AntPathRequestMatcher(baseUri + "/{" + REGISTRATION_ID_URI_VARIABLE_NAME + "}");
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request) {
        String registrationId = resolveRegistrationId(request);
        if (registrationId == null) return null;

        if (registrationId.equals("keycloakWithPKCE")) {
            OAuth2AuthorizationRequest oauth2AuthorizationRequest = defaultOAuth2AuthorizationRequestResolver.resolve(request);
            return customResolve(oauth2AuthorizationRequest);
        }

        return defaultOAuth2AuthorizationRequestResolver.resolve(request);
    }

    @Override
    public OAuth2AuthorizationRequest resolve(HttpServletRequest request, String registrationId) {
        if (registrationId == null) return null;

        if (registrationId.equals("keycloakWithPKCE")) {
            OAuth2AuthorizationRequest oauth2AuthorizationRequest = defaultOAuth2AuthorizationRequestResolver.resolve(request);
            return customResolve(oauth2AuthorizationRequest);
        }

        return defaultOAuth2AuthorizationRequestResolver.resolve(request);
    }

    private String resolveRegistrationId(HttpServletRequest request) {
        if (this.authorizationRequestMatcher.matches(request)) {
            return this.authorizationRequestMatcher.matcher(request)
                    .getVariables()
                    .get(REGISTRATION_ID_URI_VARIABLE_NAME);
        }

        return null;
    }

    private OAuth2AuthorizationRequest customResolve(OAuth2AuthorizationRequest oauth2AuthorizationRequest) {
        OAuth2AuthorizationRequest.Builder builder = OAuth2AuthorizationRequest.from(oauth2AuthorizationRequest);
        DEFAULT_PKCE_APPLIER.accept(builder);

        return builder.build();
    }
}
