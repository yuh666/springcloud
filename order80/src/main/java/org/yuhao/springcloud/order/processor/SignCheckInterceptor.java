package org.yuhao.springcloud.order.processor;

import org.springframework.core.Constants;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.yuhao.springcloud.order.aspect.SignCheck;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * TODO:DOCUMENT ME!
 *
 * @author yuhao
 * @date 2020/7/22 1:59 下午
 */
public class SignCheckInterceptor extends HandlerInterceptorAdapter {

    private SignCheckHolder signCheckHolder;
    private RequestSignMatcher matcher;

    public SignCheckInterceptor(SignCheckHolder signCheckHolder,
            RequestSignMatcher matcher) {
        this.signCheckHolder = signCheckHolder;
        this.matcher = matcher;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
            Object handler) throws Exception {
        String uri = request.getRequestURI();
        SignCheck signCheck = signCheckHolder.getSignCheck(uri);
        if (signCheck == null) {
            return true;
        }
        boolean match ;
        try {
            match = matcher.match(request, signCheck);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
        if (!match) {
            throw new RuntimeException("签名失败");
        }
        return true;
    }
}
