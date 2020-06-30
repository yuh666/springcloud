package org.yuhao.springcloud.common.util.metric;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 断路器
 *
 * @author yss
 */
public class CircuitBreaker {

    /**
     * 标识
     */
    private AtomicBoolean open = new AtomicBoolean(false);

    /**
     * 等待半开开始时间
     */
    private AtomicLong halfOpenStart = new AtomicLong();

    /**
     * 断开之后多长时间开启半开
     */
    private long halfOpenSleepTime;

    /**
     * 最少请求数目
     */
    private int minRequestThreshold;

    /**
     * 熔断失败比例
     */
    private int failPercentThreshold;


    public CircuitBreaker(long halfOpenSleepTime, int minRequestThreshold,
            int failPercentThreshold) {
        this.halfOpenSleepTime = halfOpenSleepTime;
        this.minRequestThreshold = minRequestThreshold;
        this.failPercentThreshold = failPercentThreshold;
    }

    public boolean allowRequest() {
        return !open.get() || allowHalfOpenRequest();
    }

    public boolean tryClose() {
        return open.compareAndSet(true, false);
    }

    public boolean tryOpen(Bucket[] buckets) {
        /**
         * 计算成功率
         */
        int sum = 0, fail = 0;
        for (Bucket bucket : buckets) {
            int failCount = bucket.fail.get();
            int successCount = bucket.success.get();
            int timeoutCount = bucket.timeout.get();
            sum += failCount + successCount + timeoutCount;
            fail += failCount + timeoutCount;
        }
        if (sum < minRequestThreshold) {
            return false;
        }
        int failPercent = (int) ((double) fail / sum * 100);
        if (failPercent >= this.failPercentThreshold) {
            // 将断路器打开
            if (open.compareAndSet(false, true)) {
                halfOpenStart.set(System.currentTimeMillis());
                return true;
            }
        }
        return false;
    }

    /**
     * 是否允许半开
     */
    private boolean allowHalfOpenRequest() {
        long closeTime = this.halfOpenStart.get();
        long now = System.currentTimeMillis();
        if (open.get() && now - halfOpenSleepTime > closeTime) {
            return this.halfOpenStart.compareAndSet(closeTime, now);
        }
        return false;
    }
}
