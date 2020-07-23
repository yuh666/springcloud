package org.yuhao.springcloud.order.aspect;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标记类开启了签名校验
 *
 * @see SignCheck
 *
 * @author yuhao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EnableSignCheck {


}
