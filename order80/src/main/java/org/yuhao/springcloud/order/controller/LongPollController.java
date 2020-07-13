package org.yuhao.springcloud.order.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
public class LongPollController {


    @RequestMapping("/longpoll")
    public DeferredResult<String> longPoll() {
        DeferredResult<String> result = new DeferredResult<>(5000L, "timeout");
        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (Math.random() > 0.5) {
                result.setResult("success");
            }
        }).start();
        return result;
    }
}
