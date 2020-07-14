package org.yuhao.springcloud.order.aspect;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SignCheck {


    String[] checkFields();

    String signKey();

    String signField() default "sign";

    boolean skipWhenNull() default true;

}
