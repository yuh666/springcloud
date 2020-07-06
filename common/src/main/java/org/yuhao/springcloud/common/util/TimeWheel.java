package org.yuhao.springcloud.common.util;

import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.ExecutorService;

public class TimeWheel {

    private ConcurrentSkipListSet<Runnable>[] buckets;
    private volatile int currIndex;
    private ExecutorService executorService;
    private long timeSpanInSeconds;

    public TimeWheel(int timeSpanInSeconds, ExecutorService executorService) {
        if (timeSpanInSeconds <= 0) {
            throw new IllegalArgumentException("wrong timeSpanInSeconds");
        }
        this.timeSpanInSeconds = timeSpanInSeconds;
        if (executorService == null || executorService.isShutdown()) {
            throw new IllegalArgumentException("wrong executorService");
        }
        this.executorService = executorService;
        this.buckets = new ConcurrentSkipListSet[timeSpanInSeconds + 1];
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new ConcurrentSkipListSet<>();
        }
    }

    public void addTask(int executeTimeInSeconds, Runnable runnable) {
        long now = System.currentTimeMillis() / 1000;
        if (executeTimeInSeconds <= now || executeTimeInSeconds > now + timeSpanInSeconds) {
            throw new IllegalArgumentException("wrong executeTime");
        }
        int newIndex = (((int) (executeTimeInSeconds - now)) + currIndex) % buckets.length;
        buckets[newIndex].add(runnable);
    }

    private void init(){

    }
}
