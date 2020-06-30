package org.yuhao.springcloud.common.util.metric;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 熔断统计
 *
 * @author yss
 */
public class MetricStream {
    /**
     * 滑动窗口
     */
    private BucketCircularArray array;

    /**
     * 熔断器
     */
    private CircuitBreaker breaker;

    /**
     * bucket时间跨度
     */
    private long bucketTimeSpanInMills;

    /**
     * /**
     * 读写锁
     * 保证buckets的原子性
     */
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public MetricStream(int bucketLength, long bucketTimeSpanInMills, int minRequestThreshold,
            int failPercentThreshold, int halfOpenWindow) {
        this.breaker = new CircuitBreaker(halfOpenWindow, minRequestThreshold,
                failPercentThreshold);
        this.bucketTimeSpanInMills = bucketTimeSpanInMills;
        this.array = new BucketCircularArray(bucketLength, bucketTimeSpanInMills);
    }

    public boolean allowRequest() {
        return breaker.allowRequest();
    }

    public void addEvent(RequestEvent event) {
        switch (event) {
            case FAIL:
                getCurrentBucket().fail.incrementAndGet();
                break;
            case SUCCESS:
                getCurrentBucket().success.incrementAndGet();
                if (breaker.tryClose()) {
                    lock.writeLock().lock();
                    try {
                        array.reset();
                    } finally {
                        lock.writeLock().unlock();
                    }
                }
                break;
            case TIMEOUT:
                getCurrentBucket().timeout.incrementAndGet();
            default:
                break;
        }
    }

    /**
     * 获取能放入当前时间的一个bucket 没有满足的就新建
     *
     * @return
     */
    private Bucket getCurrentBucket() {
        Bucket bucket;
        lock.readLock().lock();
        long now = System.currentTimeMillis();
        try {
            bucket = array.tail();
            // 如果存在并且满足此刻的时间
            if (bucket != null && bucket.startTime + bucketTimeSpanInMills >= now) {
                return bucket;
            }
        } finally {
            lock.readLock().unlock();
        }
        // 不满足要新建
        lock.writeLock().lock();
        boolean added = false;
        try {
            //double check
            bucket = array.tail();
            // 如果存在并且满足此刻的时间
            if (bucket != null && bucket.startTime + bucketTimeSpanInMills >= now) {
                return bucket;
            }
            // 只能新建了
            bucket = new Bucket(now);
            array.addBucket(bucket);
            added = true;
        } finally {
            lock.writeLock().unlock();
        }
        if (added) {
            breaker.tryOpen(array.toArray());
        }
        return bucket;
    }
}
