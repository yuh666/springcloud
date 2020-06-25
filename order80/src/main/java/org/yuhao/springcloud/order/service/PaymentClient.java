package org.yuhao.springcloud.order.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yuhao.springcloud.common.dto.payment.PaymentService;

@FeignClient("payment-service")
public interface PaymentClient extends PaymentService {

}
