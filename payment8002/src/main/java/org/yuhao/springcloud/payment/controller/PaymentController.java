package org.yuhao.springcloud.payment.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Value("${server.port}")
    private int port;

    @RequestMapping("/lb")
    public Object lb() {
        return "port: " + port;
    }

}
