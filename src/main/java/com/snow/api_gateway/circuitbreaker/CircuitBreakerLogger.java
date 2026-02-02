package com.snow.api_gateway.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CircuitBreakerLogger {

    public CircuitBreakerLogger(CircuitBreakerRegistry registry) {
        registry.circuitBreaker("backendService")
            .getEventPublisher()
            .onStateTransition(event ->
                log.warn("CB state change: {}", event)
            );
    }
}
