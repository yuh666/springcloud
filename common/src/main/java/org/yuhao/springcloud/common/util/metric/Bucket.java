package org.yuhao.springcloud.common.util.metric;

import java.util.concurrent.atomic.AtomicInteger;

public class Bucket {
        /**
         * 开始时间
         */
        long startTime;
        /**
         * 成功次数
         */
        AtomicInteger success = new AtomicInteger(0);
        /**
         * 失败次数
         */
        AtomicInteger fail = new AtomicInteger(0);
        /**
         * 超时次数
         */
        AtomicInteger timeout = new AtomicInteger(0);

        public Bucket(long startTime) {
            this.startTime = startTime;
        }

        void reset() {
            success.set(0);
            fail.set(0);
            timeout.set(0);
        }
    }