package org.yuhao.springcloud.common.util.metric;

/**
 * 熔断器测试
 */
public class Main {

    public static void main(String[] args) throws InterruptedException {
        MetricStream metricStream = new MetricStream(10, 1000, 10, 50, 2000);
        for (int i = 0; i < 1000; i++) {
            metricStream.addEvent(RequestEvent.FAIL);
            Thread.sleep(500);
            System.out.println(metricStream.allowRequest());
            if(metricStream.allowRequest()){
                metricStream.addEvent(RequestEvent.SUCCESS);
            }
        }
    }
}
