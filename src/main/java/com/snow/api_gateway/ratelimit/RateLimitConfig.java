package com.snow.api_gateway.ratelimit;

public class RateLimitConfig {

    private final long capacity;
    private final long refillRatePerSecond;

    public RateLimitConfig(long capacity, long refillRatePerSecond) {
        this.capacity = capacity;
        this.refillRatePerSecond = refillRatePerSecond;
    }

    public long getCapacity() {
        return capacity;
    }

    public long getRefillRatePerSecond() {
        return refillRatePerSecond;
    }
}
