package org.yuhao.springcloud.common.dm.singleton;


public class Singleton {

    enum ABC {
        INSTANCE;

        ABC() {
            System.out.println("init");
        }
    }

    public static void main(String[] args) throws ClassNotFoundException {
       Class.forName("org.yuhao.springcloud.common.util.ratelimiter.Singleton$ABC");
    }
}
