package org.yuhao.springcloud.common.util;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class TTLChecker {


    private int maxExpireTime;
    private Set<String>[] buckets;
    private Map<String, Integer> indexMap = new HashMap<>();


    public TTLChecker(int maxExpireTime) {
        this.maxExpireTime = maxExpireTime;
        buckets = new Set[maxExpireTime + 2]; // 防止首尾相接
        for (int i = 0; i < buckets.length; i++) {
            buckets[i] = new HashSet<>();
        }
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            int index = (int) (System.currentTimeMillis() / 1000) % buckets.length;
            System.out.println("scan index: " + index);
            Set<String> bucket = buckets[index];
            Iterator<String> iterator = bucket.iterator();
            while (iterator.hasNext()) {
                String next = iterator.next();
                System.out.println(next + " 过期了");
                iterator.remove();
                indexMap.remove(next);
            }
        }, 1, 1, TimeUnit.SECONDS);
    }


    public void heartBeat(String key, int expireTime) {
        if (expireTime > maxExpireTime) {
            throw new IllegalArgumentException("expireTime is too large");
        }
        Integer oldIndex = indexMap.get(key);
        if (oldIndex != null) {
            buckets[oldIndex].remove(key);
        }
        int index = (int) (System.currentTimeMillis() / 1000 + expireTime) % buckets.length;
        buckets[index].add(key);
        System.out.println(key + " => " + index);
        indexMap.put(key, index);
    }

    public static void main(String[] args) throws InterruptedException {
        TTLChecker idleChecker = new TTLChecker(5);
        while (true) {
            idleChecker.heartBeat("abc", 1);
            //Thread.sleep(500);// 不过期
            Thread.sleep(1500);// 过期
        }
    }

}
