package org.yuhao.springcloud.common.dto.payment;

import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/payment")
public interface PaymentService {

    @RequestMapping("/lb")
    String lb();
}