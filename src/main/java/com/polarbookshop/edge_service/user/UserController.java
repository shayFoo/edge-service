package com.polarbookshop.edge_service.user;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
public class UserController {

    @GetMapping("/user")
    public Mono<User> user(@AuthenticationPrincipal OidcUser oidcUser) {
        User user = new User(
                oidcUser.getPreferredUsername(),
                oidcUser.getGivenName(),
                oidcUser.getFamilyName(),
                List.of("employee", "customer") // TODO: fetch real roles from an OIDC provider
        );
        return Mono.just(user);
    }
}
