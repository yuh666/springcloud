package org.yuhao.springcloud.common.util.concurrent;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author yss
 */
public abstract class ServiceThread implements Runnable {

    private Thread thread;
    private boolean daemon = false;
    private ResetCountDownLatch countDownLatch = new ResetCountDownLatch(1);
    private AtomicBoolean started = new AtomicBoolean(false);


    public void start() {
        if (!started.compareAndSet(false, true)) {
            return;
        }
        thread = new Thread(this, serviceName());
        thread.setDaemon(daemon);
        thread.start();
    }

    public void wakeup() {
        countDownLatch.countDown();

    }

    protected void waitForRunning(long interval, TimeUnit unit) {
        countDownLatch.reset();
        try {
            countDownLatch.await(interval, unit);
        } catch (InterruptedException ignored) {
        }
    }

    protected abstract String serviceName();

    public void markStop() {
        started.compareAndSet(true, false);
    }

    public void stop() {
        stop(false, 0);
    }

    public void stop(boolean interrupt, long timeout) {
        if (!started.compareAndSet(true, false)) {
            // 已经停止了
            return;
        }
        countDownLatch.countDown();
        if (interrupt) {
            thread.interrupt();
        }
        try {
            thread.join(timeout);
        } catch (InterruptedException ignored) {

        }
    }

    public boolean isDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    public boolean isRunning() {
        return started.get();
    }
}
