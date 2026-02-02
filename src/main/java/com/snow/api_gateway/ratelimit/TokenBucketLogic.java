package com.snow.api_gateway.ratelimit;

public class TokenBucketLogic {

    private TokenBucketLogic() {}

    public static RateLimitResponse allowRequest(
            TokenBucketState state,
            long capacity,
            long refillRatePerSecond
    ) {
        refill(state, capacity, refillRatePerSecond);

        boolean allowed = false;
        if (state.getTokens() > 0) {
            state.setTokens(state.getTokens() - 1);
            allowed = true;
        }

        long resetTimeSeconds =
                System.currentTimeMillis() / 1000
                        + (capacity - state.getTokens()) / refillRatePerSecond;

        return new RateLimitResponse(
                allowed,
                state.getTokens(),
                capacity,
                resetTimeSeconds
        );
    }


    private static void refill(
            TokenBucketState state,
            long capacity,
            long refillRatePerSecond
    ) {
        long now = System.nanoTime();
        long elapsedSeconds =
                (now - state.getLastRefillTimestamp()) / 1_000_000_000;

        if (elapsedSeconds > 0) {
            long tokensToAdd = elapsedSeconds * refillRatePerSecond;
            state.setTokens(
                    Math.min(capacity, state.getTokens() + tokensToAdd)
            );
            state.setLastRefillTimestamp(now);
        }
    }
}
