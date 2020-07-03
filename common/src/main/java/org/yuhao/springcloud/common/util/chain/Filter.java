package org.yuhao.springcloud.common.util.chain;

@FunctionalInterface
public interface Filter {
    void doFilter(Event event, Chain chain);
}
