package org.yuhao.springcloud.order.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yuhao.springcloud.order.config.Stream;
import org.yuhao.springcloud.order.service.PaymentClient;
@RestController
@Validated
@RequestMapping("/order")
public class OrderController {

    @Value("${server.port}")
    private Integer port;

    @Autowired
    private PaymentClient paymentClient;

    @Autowired
    private Stream.Source source;

    @RequestMapping("/lb")
    public Object lb() {
        return port + "=>" + paymentClient.lb();
    }

    @RequestMapping("/mq")
    public Object mq() {
        source.orderOutput().send(MessageBuilder.withPayload("Hello").build());
        return "success";
    }

}
