package com.polarbookshop.edge_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Configuration
public class WebEndpoints {
    @Bean
    public RouterFunction<ServerResponse> routerFunction() {
        return RouterFunctions.route()
                                .GET("/catalog-fallback", this::fallbackResponse)
                .POST("/catalog-fallback", this::fallbackResponse)
                .build();
    }

    private Mono<ServerResponse> fallbackResponse(ServerRequest serverRequest) {
        String errorMessage = """
                {
                  "message": "Catalog service is currently unavailable. Please try again later."
                }
                """;
                return ServerResponse.status(HttpStatus.OK)
                .body(Mono.just(errorMessage), String.class);
    }
}

