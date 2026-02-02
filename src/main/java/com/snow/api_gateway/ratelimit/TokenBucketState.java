package com.snow.api_gateway.ratelimit;

import java.io.Serializable;

public class TokenBucketState implements Serializable {

    private long tokens;
    private long lastRefillTimestamp;

    public TokenBucketState() {}

    public TokenBucketState(long tokens, long lastRefillTimestamp) {
        this.tokens = tokens;
        this.lastRefillTimestamp = lastRefillTimestamp;
    }

    public long getTokens() {
        return tokens;
    }

    public void setTokens(long tokens) {
        this.tokens = tokens;
    }

    public long getLastRefillTimestamp() {
        return lastRefillTimestamp;
    }

    public void setLastRefillTimestamp(long lastRefillTimestamp) {
        this.lastRefillTimestamp = lastRefillTimestamp;
    }
}
