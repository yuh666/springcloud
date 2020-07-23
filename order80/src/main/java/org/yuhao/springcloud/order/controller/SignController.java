package org.yuhao.springcloud.order.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yuhao.springcloud.order.aspect.EnableSignCheck;
import org.yuhao.springcloud.order.aspect.SignCheck;

@RestController
@EnableSignCheck
@RequestMapping("/sign")
public class SignController {

    @RequestMapping("/sign1")
    @SignCheck(secretKey = "secret.key")
    public Object sign1(String a, Long b) {
        return "success";
    }

}
