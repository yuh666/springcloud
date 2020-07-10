package org.yuhao.springcloud.payment.order;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class OrderMqService {

    @StreamListener("orderInput")
    public void handle(HashMap<String, String> message) {
        System.out.println(message);
    }

}
