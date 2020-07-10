package org.yuhao.springcloud.payment.config;


import org.springframework.cloud.stream.annotation.Input;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;

public class Stream {

    public interface Sink {
        @Input("orderInput")
        SubscribableChannel orderInput();
    }
}
