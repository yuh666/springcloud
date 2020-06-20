package org.yuhao.springcloud.order.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.yuhao.springcloud.common.dto.ResponseResult;
import org.yuhao.springcloud.common.dto.payment.PaymentReqDto;
import org.yuhao.springcloud.common.dto.payment.PaymentRespDto;

import javax.validation.constraints.Min;

@RestController
@Validated
@RequestMapping("/order")
public class OrderController {

    private static final String paymentHost = "http://payment-service/payment";

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/lb")
    public Object lb() {
        return restTemplate.getForObject(paymentHost + "/lb", String.class);
    }

}
