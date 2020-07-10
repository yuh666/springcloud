package org.yuhao.springcloud.payment.order;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

@Service
public class OrderMqService {

    @StreamListener("orderInput")
    public void handle(String message) {
        System.out.println(message);
    }

}
