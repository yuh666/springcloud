package org.yuhao.springcloud.order.config;


import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public class Stream {

    public interface Source {
        @Output("orderOutput")
        MessageChannel orderOutput();
    }
}
