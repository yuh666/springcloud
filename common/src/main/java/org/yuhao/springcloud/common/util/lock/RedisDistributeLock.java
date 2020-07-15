package org.yuhao.springcloud.common.util.lock;

import org.springframework.core.io.ClassPathResource;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
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

    /**
     * 存储val
     */
    private static ThreadLocal<String> lockValues = new ThreadLocal<>();

    /**
     * Redis操作入口
     */
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 解锁CAS脚本
     */
    private DefaultRedisScript<Integer> script;

    public RedisDistributeLock(
            RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        initScript();
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
            Integer unlock = redisTemplate.execute(
                    (RedisCallback<Integer>) connection -> connection.evalSha(script.getSha1(),
                            ReturnType.INTEGER, 1,
                            key.getBytes(), val.getBytes()));
            return unlock != null && unlock == 1;
        } finally {
            lockValues.remove();
        }
    }

    /**
     * 启动将脚本加载到redis中 后面使用sha1代理
     */
    private void initScript() {
        script = new DefaultRedisScript<>();
        script.setLocation(new ClassPathResource("lock.lua"));
        this.redisTemplate.execute(
                (RedisCallback<Object>) connection -> connection.scriptLoad(
                        script.getScriptAsString().getBytes()));
    }
}
