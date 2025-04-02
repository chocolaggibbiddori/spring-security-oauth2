package chocola.security.oauth2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IndexController {

    private final ClientRegistrationRepository clientRegistrationRepository;

    @Autowired
    public IndexController(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    @GetMapping("/")
    public String index() {
        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("keycloak");
        System.out.println("clientRegistration = " + clientRegistration);
        return "index";
    }
}
