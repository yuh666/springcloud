package org.yuhao.springcloud.order.processor;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.SmartInstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.yuhao.springcloud.order.aspect.EnableSignCheck;
import org.yuhao.springcloud.order.aspect.SignCheck;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;

/**
 * 收集签名校验的信息
 *
 * @author yuhao
 * @date 2020/7/15 6:07 下午
 */
public class SignCheckCollectorBeanPostProcessor implements SmartInstantiationAwareBeanPostProcessor, SignCheckHolder {


    private HashMap<String, SignCheck> signCheckMap = new HashMap<>();

    private Environment env;

    public SignCheckCollectorBeanPostProcessor(Environment env) {
        this.env = env;
    }

    /**
     * 取出Path对应Signcheck信息
     *
     * @param beanClass beanClass
     * @param beanName  beanName
     * @return bean
     * @throws BeansException ignored
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        if (!beanClass.isAnnotationPresent(EnableSignCheck.class)) {
            return null;
        }
        String parentPath = "";
        RequestMapping annotation = beanClass.getAnnotation(RequestMapping.class);
        if (annotation != null) {
            parentPath = annotation.value()[0];
        }
        Method[] methods = beanClass.getMethods();
        for (Method method : methods) {
            SignCheck signCheck = method.getAnnotation(SignCheck.class);
            if (signCheck == null) {
                continue;
            }
            RequestMapping requestMapping = method.getAnnotation(RequestMapping.class);
            if (requestMapping == null) {
                continue;
            }
            String path = env.getProperty("server.servlet.context-path",
                    "") + parentPath + requestMapping.value()[0];
            signCheckMap.put(path, signCheck);
        }
        return null;
    }


    @Override
    public SignCheck getSignCheck(String uri) {
        return signCheckMap.get(uri);
    }

}
