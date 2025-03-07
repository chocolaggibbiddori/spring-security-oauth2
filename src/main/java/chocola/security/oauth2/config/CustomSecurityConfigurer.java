package chocola.security.oauth2.config;

import org.springframework.security.config.annotation.SecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;

public class CustomSecurityConfigurer implements SecurityConfigurer<DefaultSecurityFilterChain, HttpSecurity> {

    private boolean secure;

    public CustomSecurityConfigurer setSecure(boolean secure) {
        this.secure = secure;
        return this;
    }

    public boolean isSecure() {
        return secure;
    }

    @Override
    public void init(HttpSecurity builder) throws Exception {
        System.out.println("Start init method");
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        System.out.println("Start configure method");

        if (isSecure()) System.out.println("Http is required");
        else System.out.println("Http is optional");
    }
}
