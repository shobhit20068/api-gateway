package com.snow.api_gateway.ratelimit;

import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class CustomRedisRateLimiter {

    // For now: hardcoded limits (we’ll improve later)
    private static final long CAPACITY = 5;
    private static final long REFILL_RATE_PER_SECOND = 1;

    private final ReactiveRedisTemplate<String, TokenBucketState> redisTemplate;

    public CustomRedisRateLimiter(
            ReactiveRedisTemplate<String, TokenBucketState> redisTemplate
    ) {
        this.redisTemplate = redisTemplate;
    }

    public Mono<RateLimitResponse> isAllowed(String key, RateLimitConfig config) {

        return redisTemplate.opsForValue()
            .get(key)
            .defaultIfEmpty(
                new TokenBucketState(CAPACITY, System.nanoTime())
            )
            .map(state -> {
                RateLimitResponse rateLimitResponse =
                        TokenBucketLogic.allowRequest(
                                state,
                                config.getCapacity(),
                                config.getRefillRatePerSecond()
                        );

                // Save updated state back to Redis
                redisTemplate.opsForValue().set(key, state).subscribe();

                return rateLimitResponse;
            });
    }
}
