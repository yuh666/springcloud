package org.yuhao.springcloud.common.util.metric;


import java.util.ArrayList;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 滑动窗口实现
 *
 * @author yss
 */
public class BucketCircularArray {

    /**
     * 数组长度
     */
    private int bucketLength;
    /**
     * 现在有几个bucket
     */
    private int dataLength;
    /**
     * bucket数组
     */
    private Bucket[] buckets;

    /**
     * 每个bucket的时间跨度
     * 也就是统计精度
     */
    private long bucketTimeSpanInMills;

    /**
     * 下一个bucket位置
     */
    private int tail;

    /**
     * 第一个有效bucket位置
     */
    private int head;


    public BucketCircularArray(int bucketLength, long bucketTimeSpanInMills) {
        this.bucketLength = bucketLength;
        this.buckets = new Bucket[bucketLength];
        this.bucketTimeSpanInMills = bucketTimeSpanInMills;
    }

    public void addBucket(Bucket bucket) {
        buckets[tail] = bucket;
        incrementTail();
    }

    public void reset() {
        for (Bucket bucket : buckets) {
            if (bucket != null) {
                bucket.reset();
            }
        }
    }

    public Bucket tail() {
        if (dataLength == 0) {
            //没数据
            return null;
        }
        // 取出最新的一个bucket
        if (dataLength < bucketLength) {
            // 没满就取出前一个
            return buckets[tail - 1];
        }
        // 满了就从head往后数 dataLength - 1 个
        return buckets[(head + dataLength - 1) % bucketLength];
    }

    public Bucket[] toArray() {
        Bucket[] array = new Bucket[0];
        if (dataLength == 0) {
            return array;
        }
        ArrayList<Bucket> list = new ArrayList<>();
        for (int i = 0; i < dataLength; i++) {
            Bucket bucket = buckets[(head + i) % bucketLength];
            if (bucket != null) {
                list.add(bucket);
            }
        }
        return list.toArray(array);
    }


    private void incrementTail() {
        tail = (tail + 1) % bucketLength;
        if (dataLength == bucketLength) {
            // 添加之前就已经满了 tail此时的位置是第一个被覆盖的bucket位置 所以要把head往后移动一位
            head = (head + 1) % bucketLength;
        }
        if (dataLength < bucketLength) {
            //还没满
            dataLength++;
        }
    }


}
