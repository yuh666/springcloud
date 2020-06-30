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

    public CircuitBreaker(long halfOpenSleepTime) {
        this.halfOpenSleepTime = halfOpenSleepTime;
    }

    public boolean allowRequest() {
        return !open.get() || allowHalfOpenRequest();
    }

    public boolean tryClose() {
        return open.compareAndSet(true, false);
    }

    public boolean open() {
        if (open.compareAndSet(false, true)) {
            halfOpenStart.set(System.currentTimeMillis());
            return true;
        }
        return false;
    }

    private boolean allowHalfOpenRequest() {
        long closeTime = this.halfOpenStart.get();
        long now = System.currentTimeMillis();
        if (open.get() || now - halfOpenSleepTime > closeTime) {
            return this.halfOpenStart.compareAndSet(closeTime, now);
        }
        return false;
    }
}
