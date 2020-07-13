package org.yuhao.springcloud.common.util;

import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;

import java.util.LinkedHashMap;
import java.util.Map;

public class GsonTest {

    public static void main(String[] args) {
        LinkedHashMap<String, String> map = new LinkedHashMap<>();
        Gson gson = new Gson();

        map.put("3", "1");
        map.put("1", "1");
        map.put("0", "1");
        System.out.println(gson.toJson(map));
    }
}
