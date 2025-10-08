package com.polarbookshop.edge_service.security.csrf;

import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

@Component
public class CsrfFilterHelper implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        exchange.getAttributeOrDefault(CsrfToken.class.getName(), Mono.empty())
                .subscribe();
        return chain.filter(exchange);
    }
}
