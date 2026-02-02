package com.snow.api_gateway.ratelimit;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class RateLimitTierResolver {

    public RateLimitConfig resolve(ServerWebExchange exchange) {

        String apiKey = exchange.getRequest()
                .getHeaders()
                .getFirst("X-API-KEY");

        if (apiKey == null) {
            // Anonymous user
            return new RateLimitConfig(2, 2);
        }

        if (apiKey.startsWith("PREMIUM")) {
            return new RateLimitConfig(20, 20);
        }

        // Free user
        return new RateLimitConfig(5, 5);
    }
}
