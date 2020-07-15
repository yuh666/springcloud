package org.yuhao.springcloud.common.util.lock;

/**
 * 分布式锁接口
 *
 * @author yuhao
 * @date 2020/7/15 4:27 下午
 */
public interface DistributeLock {

    /**
     * 非阻塞获取锁
     *
     * @param key key
     * @param ttl 锁超时时间(ms)
     * @return 成功与否
     */
    default boolean tryLock(String key, long ttl) {
        return lock(key, ttl, 0L);
    }

    /**
     * 阻塞获取锁
     *
     * @param key key
     * @param ttl 锁超时时间(ms)
     * @return 成功与否
     */
    default boolean lock(String key, long ttl) {
        return lock(key, ttl, Long.MAX_VALUE);
    }

    /**
     * 阻塞获取锁,有阻塞时长
     *
     * @param key         key
     * @param ttl         锁超时时间(ms)
     * @param lockTimeout 获取锁超时(ms)
     * @return 成功与否
     */
    boolean lock(String key, long ttl, long lockTimeout);

    /**
     * 解锁
     *
     * @param key key
     * @return 成功与否(这里的失败表示锁已经过期)
     */
    boolean unlock(String key);
}
