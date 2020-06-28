package org.yuhao.springcloud.hdash;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

@SpringBootApplication
@EnableHystrixDashboard
public class HDashMain9001 {

    public static void main(String[] args) {
        SpringApplication.run(HDashMain9001.class, args);
    }
}
