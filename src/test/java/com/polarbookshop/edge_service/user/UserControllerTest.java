package com.polarbookshop.edge_service.user;

import com.polarbookshop.edge_service.config.SecurityConfig;
import com.polarbookshop.edge_service.security.csrf.SpaCsrfTokenRequestHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.test.web.reactive.server.SecurityMockServerConfigurers;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@WebFluxTest(UserController.class)
@Import(value = {
        SpaCsrfTokenRequestHandler.class,
        SecurityConfig.class
})
class UserControllerTest {

    @Autowired
    WebTestClient webClient;

    @MockitoBean
    ReactiveClientRegistrationRepository clientRegistrationRepository;

    @Test
    void whenNotAuthenticatedThen401() {
        webClient.get()
                .uri("/user")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    void whenAuthenticatedThenReturnUser() {
        User expected = new User("jon.snow", "Jon", "Snow", List.of("employee", "customer"));
        webClient.mutateWith(configureMockOidcLogin(expected))
                .get()
                .uri("/user")
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody(User.class)
                .value(actual -> assertThat(actual).isEqualTo(expected));
    }

    private SecurityMockServerConfigurers.OidcLoginMutator configureMockOidcLogin(User user) {
        return SecurityMockServerConfigurers.mockOidcLogin()
                .idToken(builder ->
                        builder.claim(StandardClaimNames.PREFERRED_USERNAME, user.username())
                                .claim(StandardClaimNames.GIVEN_NAME, user.firstName())
                                .claim(StandardClaimNames.FAMILY_NAME, user.lastName())
                );
    }
}
