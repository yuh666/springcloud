package org.yuhao.springcloud.order.config;

import com.netflix.hystrix.contrib.metrics.eventstream.HystrixMetricsStreamServlet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.yuhao.springcloud.order.processor.MD5RequestSignMatcher;
import org.yuhao.springcloud.order.processor.SignCheckCollectorBeanPostProcessor;
import org.yuhao.springcloud.order.processor.SignCheckInterceptor;

@Configuration
public class ServletConfig implements WebMvcConfigurer {

    @Autowired
    Environment env;


    @Bean
    public SignCheckCollectorBeanPostProcessor signCheckCollector() {
        return new SignCheckCollectorBeanPostProcessor(env);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        SignCheckInterceptor signCheckInterceptor = new SignCheckInterceptor(signCheckCollector(),
                new MD5RequestSignMatcher(env));
        registry.addInterceptor(signCheckInterceptor);
    }
}
