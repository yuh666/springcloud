package org.yuhao.springcloud.order.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yuhao.springcloud.order.service.PaymentClient;

@RestController
@Validated
@RequestMapping("/order")
public class OrderController {


    @Value("${server.port}")
    private Integer port;

    @Autowired
    private PaymentClient paymentClient;

    @RequestMapping("/lb")
    public Object lb() {
        return port + "=>" + paymentClient.lb();
    }

}
