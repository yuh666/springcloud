package org.yuhao.springcloud.common.util.chain;

import java.util.List;

public class Chain {
    private int index = -1;
    private List<Filter> filters;

    public Chain(List<Filter> filters) {
        this.filters = filters;
    }

    public void filter(Event event) {
        index++;
        if (index == filters.size()) {
            return;
        }
        filters.get(index).doFilter(event, this);
    }
}
