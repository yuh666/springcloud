package org.yuhao.springcloud.common.util;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

public class Path {

    public static void main(String[] args) throws IOException {
        ClassLoader classLoader = Path.class.getClassLoader();
//        Enumeration<URL> resources = classLoader.getResources("META-INF/spring.factories");
//        while(resources.hasMoreElements()){
//            System.out.println(resources.nextElement());
//        }
        System.out.println(System.getProperty("java.class.path"));
    }
}
