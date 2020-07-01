package org.yuhao.springcloud.common.util.chain;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        ArrayList<Filter> filters = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            filters.add((e, c) -> {
                e.a++;
                c.filter(e);
            });
        }
        Event event = new Event();
        Chain chain = new Chain(filters);
        chain.filter(event);
        System.out.println(event);
    }
}
