package org.yuhao.springcloud.common.util.lock;

import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.core.types.Expiration;

import java.util.Arrays;
import java.util.UUID;

/**
 * Redis实现的分布式锁
 *
 * @author yuhao
 * @date 2020/7/15 4:42 下午
 */
public class RedisDistributeLock implements DistributeLock {

    private static ThreadLocal<String> lockValues = new ThreadLocal<>();

    private RedisTemplate<String, String> redisTemplate;

    private DefaultRedisScript<Integer> script = new DefaultRedisScript<>();

    public RedisDistributeLock(
            RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        script.setLocation(new ClassPathResource("lock.lua"));

    }

    @Override
    public boolean lock(String key, long ttl, long lockTimeout) {
        long start = System.currentTimeMillis();
        String value = UUID.randomUUID().toString().replace("-", "");
        do {
            redisTemplate.execute((RedisCallback<Boolean>) redisConnection -> {
                Boolean set = redisConnection.set(key.getBytes(), value.getBytes(),
                        Expiration.milliseconds(ttl),
                        RedisStringCommands.SetOption.SET_IF_ABSENT);
                if (set != null && set) {
                    lockValues.set(value);
                    return true;
                }
                return false;
            });

        } while (System.currentTimeMillis() - start < lockTimeout);
        return false;
    }

    @Override
    public boolean unlock(String key) {
        String val = lockValues.get();
        if (val == null) {
            return false;
        }
        try {
            return redisTemplate.execute(script, Arrays.asList(key), val) == 1;
        } finally {
            lockValues.remove();
        }
    }
}
