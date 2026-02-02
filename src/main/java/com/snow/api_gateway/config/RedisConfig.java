package com.snow.api_gateway.config;

import com.snow.api_gateway.ratelimit.TokenBucketState;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.*;

@Configuration
public class RedisConfig {

    @Bean
    public ReactiveRedisTemplate<String, TokenBucketState> reactiveRedisTemplate(
            ReactiveRedisConnectionFactory factory) {

        RedisSerializer<String> keySerializer = new StringRedisSerializer();

        // ✅ Use Jackson JSON Serializer for TokenBucketState
        Jackson2JsonRedisSerializer<TokenBucketState> valueSerializer =
                new Jackson2JsonRedisSerializer<>(TokenBucketState.class);

        RedisSerializationContext<String, TokenBucketState> serializationContext =
                RedisSerializationContext.<String, TokenBucketState>newSerializationContext(keySerializer)
                        .value(valueSerializer)
                        .build();

        return new ReactiveRedisTemplate<>(factory, serializationContext);
    }
}
