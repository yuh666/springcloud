package org.yuhao.springcloud.order.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yuhao.springcloud.common.dto.payment.PaymentService;

@FeignClient(value = "payment-service",
        fallback = PaymentClient.PaymentClientFallback.class)
public interface PaymentClient extends PaymentService {
    @Service
    @RequestMapping("/fallback")
    class PaymentClientFallback implements PaymentClient {

        @Override
        public String lb() {
            return "fallback";
        }
    }

}
