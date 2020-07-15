package org.yuhao.springcloud.order.aspect;


import java.lang.annotation.*;

/**
 * 签名校验注解
 * 标注签名校验的元信息
 * {@link org.yuhao.springcloud.order.aspect.SignCheckAspect}
 *
 * @author zy-user
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SignCheck {

    /**
     * 参与校验的字段
     */
    String[] checkFields();

    /**
     * 签名秘钥
     */
    String signKey();

    /**
     * 请求参数中签名结果字段
     */
    String signField() default "sign";

    /**
     * 字段为空不参与校验
     */
    boolean skipWhenNull() default true;

}
