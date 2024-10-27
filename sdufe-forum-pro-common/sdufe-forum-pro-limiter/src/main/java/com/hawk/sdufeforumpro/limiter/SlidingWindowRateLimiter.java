package com.hawk.sdufeforumpro.limiter;

import org.redisson.api.RRateLimiter;
import org.redisson.api.RateIntervalUnit;
import org.redisson.api.RateType;
import org.redisson.api.RedissonClient;

/**
 * 滑动窗口限流服务
 */
public class SlidingWindowRateLimiter implements RateLimiter {

    private RedissonClient redissonClient;

    private static final String LIMIT_KEY_PREFIX = "sdufe:forum:limit:";

    public SlidingWindowRateLimiter(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public Boolean tryAcquire(String key, int limit, int windowSize) {
        RRateLimiter rateLimiter = redissonClient.getRateLimiter(LIMIT_KEY_PREFIX + key);

        if(!rateLimiter.isExists()) {
            rateLimiter.trySetRate(RateType.OVERALL, limit, windowSize, RateIntervalUnit.SECONDS);
        }

        return rateLimiter.tryAcquire();
    }
}
