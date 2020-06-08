//import java.lang.reflect.InvocationTargetException;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLClassLoader;
//
///**
// * @author yuhao
// * @date 2020/6/8 8:48 下午
// */
//public class ClassLoaderTest {
//
//    public static void main(
//            String[] args) throws ClassNotFoundException, MalformedURLException, InterruptedException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
//
//        while (true) {
//            ClassLoader contextClassLoader = newClassLoader();
//            try {
//                Class<?> clazz = contextClassLoader.loadClass("Helloworld");
//                Object o = clazz.newInstance();
//                clazz.getMethod("hello").invoke(o);
//            } catch (Exception e) {
//                System.out.println("编译中...");
//            }
//            Thread.sleep(1000);
//
//        }
//
//    }
//
//
//    public static ClassLoader newClassLoader() throws MalformedURLException {
//        return new URLClassLoader(new URL[]{new URL(
//                "file:////Users/yss/Desktop/develop/java/learn/springcloud/common/target/test-classes/")},
//                null);
//    }
//}
