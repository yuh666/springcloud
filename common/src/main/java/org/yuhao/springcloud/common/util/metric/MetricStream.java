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

    private long bucketTimeSpanInMills;

    /**
     * 最少请求数
     */
    private int minRequestThreshold;

    /**
     * 熔断失败比例
     */
    private int failPercentThreshold;


    /**
     * 读写锁
     * 保证buckets的原子性
     */
    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public MetricStream(int bucketLength, long bucketTimeSpanInMills, int minRequestThreshold,
            int failPercentThreshold, int halfOPenWindow) {
        this.breaker = new CircuitBreaker(halfOPenWindow);
        this.minRequestThreshold = minRequestThreshold;
        this.failPercentThreshold = failPercentThreshold;
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
        lock.readLock().lock();
        long now = System.currentTimeMillis();
        try {
            Bucket bucket = array.tail();
            // 如果存在并且满足此刻的时间
            if (bucket != null && bucket.startTime + bucketTimeSpanInMills >= now) {
                return bucket;
            }
        } finally {
            lock.readLock().unlock();
        }
        // 不满足要新建
        lock.writeLock().lock();
        try {
            //double check
            Bucket bucket = array.tail();
            // 如果存在并且满足此刻的时间
            if (bucket != null && bucket.startTime + bucketTimeSpanInMills >= now) {
                return bucket;
            }
            // 只能新建了
            Bucket newBucket = new Bucket(now);
            array.addBucket(newBucket);
            // 统计
            tryOpen();
            return newBucket;
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void tryOpen() {
        Bucket[] buckets = array.toArray();
        int sum = 0, success = 0, fail = 0;
        for (Bucket bucket : buckets) {
            int failCount = bucket.fail.get();
            int successCount = bucket.success.get();
            int timeoutCount = bucket.timeout.get();
            sum += failCount + successCount + timeoutCount;
            success += successCount;
            fail += failCount + timeoutCount;
        }
        int failPercent = (int) ((double) fail / sum * 100);
        if (failPercent >= this.failPercentThreshold) {
            breaker.open();
        }
    }


}
