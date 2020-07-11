package org.yuhao.springcloud.common.util;

import java.lang.reflect.Field;
import java.util.*;

public class Zson {


    public String toJson(Object obj) {
        StringBuilder builder = new StringBuilder();
        toJson(obj, builder);
        return builder.toString();
    }

    private void toJson(Object obj, StringBuilder builder) {
        if (obj instanceof String) {
            builder.append("\"").append(obj).append("\"");
        } else if (obj instanceof List) {
            builder.append("[");
            List list = (List) obj;
            for (int i = 0; i < list.size(); i++) {
                toJson(list.get(i), builder);
                if (i != list.size() - 1) {
                    builder.append(",");
                }
            }
            builder.append("]");
        } else if (obj instanceof Map) {
            builder.append("{");
            Map<Object, Object> map = (Map) obj;
            int size = map.size();
            int count = 0;
            for (Map.Entry<Object, Object> entry : map.entrySet()) {
                toJson(entry.getKey(), builder);
                builder.append(":");
                toJson(entry.getValue(), builder);
                if (count++ != size - 1) {
                    builder.append(",");
                }
            }
            builder.append("}");
        } else {
            builder.append("{");
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (int i = 0; i < declaredFields.length; i++) {
                Field declaredField = declaredFields[i];
                declaredField.setAccessible(true);
                try {
                    builder.append("\"").append(declaredField.getName()).append("\"").append(":");
                    Object o = declaredField.get(obj);
                    toJson(o, builder);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (i != declaredFields.length - 1) {
                    builder.append(",");
                }

            }
            builder.append("}");
        }
    }

    static class User {
        String name;
        List<Map<String, String>> cars;

        public User() {
        }
    }

    public static void main(String[] args) {
        Zson zson = new Zson();

        User user = new User();

        user.name = "penglei";

        ArrayList<Map<String, String>> cars = new ArrayList<>();
        HashMap<String, String> car1 = new HashMap<>();
        car1.put("brand", "Rolls-Royce");
        car1.put("price", "8000000");
        HashMap<String, String> car2 = new HashMap<>();
        car2.put("brand", "Bentley");
        car2.put("price", "7000000");
        cars.add(car1);
        cars.add(car2);
        user.cars = cars;
        System.out.println(zson.toJson(user));
    }
}
