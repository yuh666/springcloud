package org.yuhao.springcloud.common.util;


public class IDWorker {

    //2020-01-01 00:00:00
    private final static long START_EPOCH = 1577808000000L;
    private final static long CLOCK_BACKWARDS = 15L;

    //Len
    private static final int dcLen = 3;
    private static final int workerLen = 7;
    private static final int seqLen = 12;

    //Shift
    private static final int timestampShift = dcLen + workerLen + seqLen;
    private static final int dcShift = workerLen + seqLen;
    private static final int workerShift = seqLen;

    //mask
    private static final int dcIdMask = (1 << dcLen) - 1;
    private static final int workerIdMask = (1 << workerLen) - 1;
    private static final int seqMask = (1 << seqLen) - 1;

    //this
    private int dcId;
    private int workerId;
    private int seq;
    private long lastTimestamp;

    public synchronized long generate() {
        long currTime = timeGen();
        if (currTime == lastTimestamp) {
            if (((seq + 1) & seqMask) == 0) {
                currTime = untilNextMilli();
                seq = 0;
            } else {
                seq++;
            }
        } else {
            seq = 0;
        }
        lastTimestamp = currTime;
        return ((lastTimestamp - START_EPOCH) << timestampShift)
                | (dcId << dcShift)
                | (workerId << workerShift)
                | seq;
    }

    public IDWorker(int dcId, int workerId) {
        this.dcId = dcId;
        this.workerId = workerId;
    }

    private long timeGen() {
        long now;
        do {
            now = System.currentTimeMillis();
            // 处理时钟回退
            if (now < lastTimestamp - CLOCK_BACKWARDS) {
                throw new RuntimeException("Clock Backwards!");
            }
        } while (now < lastTimestamp);
        return now;
    }

    public static long millis(long id) {
        return (id >> timestampShift) + START_EPOCH;
    }

    public static int dcId(long id) {
        return (int) (id >> dcShift & dcIdMask);
    }

    public static int workerId(long id) {
        return (int) (id >> workerShift & workerIdMask);
    }

    public static int seq(long id) {
        return (int) id & seqMask;
    }

    private long untilNextMilli() {
        long next;
        do {
            next = timeGen();
        } while (next <= lastTimestamp);
        return next;
    }

}



