package org.yuhao.springcloud.order.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yuhao.springcloud.order.aspect.SignCheck;

@RestController
public class SignController {

    @RequestMapping("/sign1")
    @SignCheck(checkFields = {"a", "b", "param.c"}, secretKey = "secret.key")
    public Object sign1(String a, Long b, Param param, String sign) {
        return "success";
    }


    @RequestMapping("/sign2")
    @SignCheck(checkFields = {"user.name", "user.age", "user.p.c"}, secretKey = "secret.key", signField = "signResult")
    public Object sign2(@RequestBody User user, String signResult) {
        return "success";
    }


    static class Param {
        String c;

        public Param() {
        }

        public String getC() {
            return c;
        }

        public void setC(String c) {
            this.c = c;
        }
    }

    static class User {
        String name;
        Integer age;
        Param p;

        public User() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Integer getAge() {
            return age;
        }

        public void setAge(Integer age) {
            this.age = age;
        }

        public Param getP() {
            return p;
        }

        public void setP(Param p) {
            this.p = p;
        }
    }

}
