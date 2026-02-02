package com.snow.api_gateway.ratelimit;

public class RateLimitResponse {

    private final boolean allowed;
    private final long remainingTokens;
    private final long capacity;
    private final long resetTimeSeconds;

    public RateLimitResponse(boolean allowed,
                             long remainingTokens,
                             long capacity,
                             long resetTimeSeconds) {
        this.allowed = allowed;
        this.remainingTokens = remainingTokens;
        this.capacity = capacity;
        this.resetTimeSeconds = resetTimeSeconds;
    }

    public boolean isAllowed() {
        return allowed;
    }

    public long getRemainingTokens() {
        return remainingTokens;
    }

    public long getCapacity() {
        return capacity;
    }

    public long getResetTimeSeconds() {
        return resetTimeSeconds;
    }
}
