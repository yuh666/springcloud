package org.yuhao.springcloud.order.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yuhao.springcloud.order.aspect.SignCheck;

@RestController
public class SignController {

    @RequestMapping("/sign1")
    @SignCheck(checkFields = {"a", "b", "param.c"}, signKey = "abc", signField = "sign", skipWhenNull = true)
    public Object sign1(String a, String b, String sign, Param param) {
        return "success";
    }

    class Param {
        String c;

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }

        public Param() {
        }
    }

}
