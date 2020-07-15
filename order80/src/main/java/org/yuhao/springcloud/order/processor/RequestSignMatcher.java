package org.yuhao.springcloud.order.processor;

import org.yuhao.springcloud.order.aspect.SignCheck;

import javax.servlet.http.HttpServletRequest;

/**
 * Http签名校验逻辑
 *
 * @author yuhao
 * @date 2020/7/15 6:11 下午
 */
public interface RequestSignMatcher {

    /**
     * 校验Http请求签名
     *
     * @param request request
     * @return 成功与否
     */
    boolean match(HttpServletRequest request, SignCheck signCheck);
}
