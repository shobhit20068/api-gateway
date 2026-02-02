package com.snow.api_gateway.filter;

import com.snow.api_gateway.ratelimit.CustomRedisRateLimiter;
import com.snow.api_gateway.ratelimit.RateLimitConfig;
import com.snow.api_gateway.ratelimit.RateLimitTierResolver;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class RateLimitFilter implements GlobalFilter, Ordered {

    private final CustomRedisRateLimiter customRedisRateLimiter;
    private final KeyResolver keyResolver;
    private final RateLimitTierResolver tierResolver;

    public RateLimitFilter(CustomRedisRateLimiter customRedisRateLimiter,
                           KeyResolver keyResolver,
                           RateLimitTierResolver tierResolver) {
        this.customRedisRateLimiter = customRedisRateLimiter;
        this.keyResolver = keyResolver;
        this.tierResolver = tierResolver;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        String path = exchange.getRequest().getPath().toString();
        if (path.startsWith("/fallback")) {
            return chain.filter(exchange);
        }

        return keyResolver.resolve(exchange)
                .flatMap(clientKey -> {

                    RateLimitConfig config = tierResolver.resolve(exchange);
                    String redisKey = "rate_limit:" + clientKey;

                    return customRedisRateLimiter.isAllowed(redisKey, config)
                            .flatMap(response -> {

                                // 🔒 FINAL SAFETY CHECK (THIS FIXES YOUR ERROR)
                                if (exchange.getResponse().isCommitted()) {
                                    return Mono.empty();
                                }

                                exchange.getResponse().getHeaders().add(
                                        "X-RateLimit-Limit",
                                        String.valueOf(response.getCapacity())
                                );
                                exchange.getResponse().getHeaders().add(
                                        "X-RateLimit-Remaining",
                                        String.valueOf(response.getRemainingTokens())
                                );

                                if (!response.isAllowed()) {
                                    exchange.getResponse()
                                            .setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                                    return exchange.getResponse().setComplete();
                                }

                                return chain.filter(exchange);
                            });
                });
    }

    /**
     * MUST run after routing + circuit breaker
     */
    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}

