package org.yuhao.springcloud.order.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yuhao.springcloud.common.util.lock.DistributeLock;
import org.yuhao.springcloud.order.config.Stream;
import org.yuhao.springcloud.order.service.PaymentClient;

import java.util.HashMap;

@RestController
@Validated
@RequestMapping("/order")
@RefreshScope
public class OrderController {


    @Value("${server.port}")
    private Integer port;

    @Autowired
    private PaymentClient paymentClient;

    @Autowired
    private DistributeLock lock;

    @Autowired
    private Stream.Source source;

    @RequestMapping("/lb")
    public Object lb() {
        return port + "=>" + paymentClient.lb();
    }

    @RequestMapping("/mq")
    public Object mq() {
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "b");
        source.orderOutput().send(MessageBuilder.withPayload(map).build());
        return "success";
    }

    @RequestMapping("/lock")
    public Object lock() throws InterruptedException {
        boolean lock = this.lock.tryLock("lock", 5000);
        if (!lock) {
            return "failed";
        }
        Thread.sleep(5000);
        this.lock.unlock("lock");
        return "success";
    }


}
