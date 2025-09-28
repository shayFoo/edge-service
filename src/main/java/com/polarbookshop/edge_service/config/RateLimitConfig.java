package com.polarbookshop.edge_service.config;

import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
public class RateLimitConfig {
    @Bean
    public KeyResolver keyResolver() {
                // Temporary solution until Spring Security is introduced
        return _ -> Mono.just("anonymous");
    }
}
