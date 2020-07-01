package org.yuhao.springcloud.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@EnableCircuitBreaker
public class OrderMain80 {

    public static void main(String[] args) throws IOException {
        SpringApplication.run(OrderMain80.class, args);
//        System.out.println(Thread.currentThread().getContextClassLoader().getClass());
    }
}
