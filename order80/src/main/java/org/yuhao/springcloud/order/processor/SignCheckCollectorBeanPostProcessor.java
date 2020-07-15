package org.yuhao.springcloud.order.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.stereotype.Component;
import org.yuhao.springcloud.order.aspect.SignCheck;

import java.util.HashMap;

/**
 * 收集签名校验的信息
 *
 * @author yuhao
 * @date 2020/7/15 6:07 下午
 */
@Component
public class SignCheckCollectorBeanPostProcessor implements BeanPostProcessor, SignCheckHolder {


    private HashMap<String, SignCheck> signCheckMap = new HashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean,
            String beanName) throws BeansException {

        return null;
    }

    @Override
    public SignCheck getSignCheck(String uri) {
        return signCheckMap.get(uri);
    }
}
