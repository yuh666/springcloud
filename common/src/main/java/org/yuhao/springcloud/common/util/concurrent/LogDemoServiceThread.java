package org.yuhao.springcloud.common.util.concurrent;

import java.util.concurrent.TimeUnit;

public class LogDemoServiceThread extends ServiceThread {

    @Override
    protected String serviceName() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void run() {
        int i = 0;
        while (isRunning()) {
            System.out.println("Log " + i++);
            waitForRunning(3, TimeUnit.SECONDS);
        }
    }
}
