package org.yuhao.springcloud.order.util;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class IdleChecker {


    private int idleTime;
    private Set<String>[] buckets;
    private Map<String, Integer> indexMap = new HashMap<>();


    public IdleChecker(int idleTime) {
        this.idleTime = idleTime;
        buckets = new Set[idleTime + 2];
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


    public void heartBeat(String key) {
        Integer oldIndex = indexMap.get(key);
        if (oldIndex != null) {
            buckets[oldIndex].remove(key);
        }
        int index = (int) (System.currentTimeMillis() / 1000 + idleTime) % buckets.length;
        buckets[index].add(key);
        System.out.println(key + " => " + index);
        indexMap.put(key, index);
    }

    public static void main(String[] args) throws InterruptedException {
        IdleChecker idleChecker = new IdleChecker(1);
        while (true) {
            idleChecker.heartBeat("abc");
            Thread.sleep(500);
        }
    }

}
