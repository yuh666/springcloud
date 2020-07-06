package org.yuhao.springcloud.common.util.concurrent;

/**
 * TODO:DOCUMENT ME!
 *
 * @author yuhao
 * @date 2020/7/6 4:27 下午
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        LogDemoServiceThread serviceThread = new LogDemoServiceThread();
        serviceThread.start();

        // 3s 输出一次
        Thread.sleep(10 * 1000);


        for (int i = 0; i < 10; i++) {
            // 1s 输出一次
            serviceThread.wakeup();
            Thread.sleep(1000);
        }

        serviceThread.stop();

    }
}
