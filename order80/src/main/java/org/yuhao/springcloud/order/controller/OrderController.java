package org.yuhao.springcloud.order.controller;


import com.ctrip.framework.apollo.model.ConfigChangeEvent;
import com.ctrip.framework.apollo.spring.annotation.ApolloConfigChangeListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.yuhao.springcloud.order.config.Stream;
import org.yuhao.springcloud.order.service.PaymentClient;

import javax.swing.event.ChangeEvent;
import java.util.HashMap;

@RestController
@Validated
@RequestMapping("/order")
@RefreshScope
public class OrderController {

//    @Value("${rate}")
//    private Integer rate;

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
        HashMap<String, String> map = new HashMap<>();
        map.put("a", "b");
        source.orderOutput().send(MessageBuilder.withPayload(map).build());
        return "success";
    }

//    @RequestMapping("/rate")
//    public Object rate() {
//        return rate;
//    }
//
//    @ApolloConfigChangeListener
//    public void onChange(ConfigChangeEvent event){
//        System.out.println(event);
//    }

}
