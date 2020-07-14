package org.yuhao.springcloud.common.dm.factory;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO:DOCUMENT ME!
 *
 * @author yuhao
 * @date 2020/7/14 12:47 下午
 */
public class StorageFactory {

    private static Map<Integer, InnerStorageFactory> FACTORIES = new HashMap<>();

    static {
        FACTORIES.put(1, new RedisStorageFactory());
        FACTORIES.put(2, new MysqlStorageFactory());
    }

    public static Storage create(int a) {
        if (!FACTORIES.containsKey(a)) {
            return null;
        }
        return FACTORIES.get(a).create();
    }


    interface InnerStorageFactory {
        Storage create();
    }

    static class RedisStorageFactory implements InnerStorageFactory {

        @Override
        public Storage create() {
            // 复
            // 杂
            // 逻
            // 辑
            return new RedisStorageImpl();
        }
    }

    static class MysqlStorageFactory implements InnerStorageFactory {

        @Override
        public Storage create() {
            // 复
            // 杂
            // 逻
            // 辑
            return new MysqlStorageImpl();
        }
    }
}
