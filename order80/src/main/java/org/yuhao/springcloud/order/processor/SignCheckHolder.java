package org.yuhao.springcloud.order.processor;

import org.yuhao.springcloud.order.aspect.SignCheck;

/**
 * signCheck的集中地
 *
 * @author yuhao
 * @date 2020/7/15 6:31 下午
 */
public interface SignCheckHolder {


    /**
     * 根据uri获取SignCheck
     *
     * @param uri
     * @return
     */
    SignCheck getSignCheck(String uri);
}
