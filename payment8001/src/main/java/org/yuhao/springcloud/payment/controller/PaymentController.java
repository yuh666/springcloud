package org.yuhao.springcloud.payment.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.yuhao.springcloud.common.dto.payment.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController implements PaymentService {

    @Value("${server.port}")
    private int port;

    @Override
    @RequestMapping("/lb")
    public String lb() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return "port: " + port;
    }

}
