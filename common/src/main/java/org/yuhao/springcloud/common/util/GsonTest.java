package org.yuhao.springcloud.common.util;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.Map;

public class GsonTest {

    public static void main(String[] args) {
        LinkedTreeMap<String, String> map = new LinkedTreeMap<>();
        Gson gson = new Gson();

        map.put("1", "1");
        Map.Entry<String, String> next = map.entrySet().iterator().next();
        String s = gson.toJson(next);
        System.out.println(s);
    }
}
