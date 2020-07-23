package org.yuhao.springcloud.order.aspect;


import java.lang.annotation.*;

/**
 * 签名校验注解
 * 标注签名校验的元信息
 *
 * @see EnableSignCheck
 *
 * @author yuhao
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SignCheck {

    /**
     * 签名秘钥
     */
    String secretKey();

    /**
     * 请求参数中签名结果字段
     */
    String signField() default "sign";

}
