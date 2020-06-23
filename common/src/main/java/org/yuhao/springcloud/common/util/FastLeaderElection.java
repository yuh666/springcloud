package org.yuhao.springcloud.common.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 多线程模拟zk选举
 * 只模拟最简单(也是最通用)的情况
 */
public class FastLeaderElection extends Thread {

    static class Vote {
        int sid; //投票者的id
        private int proposalSid; // 投的节点id
        private int proposalEpoch; // 投的节点的epoch
        private int proposalZxid; // 投的节点的zxid

        public Vote(int sid, int proposalSid, int proposalEpoch, int proposalZxid) {
            this.sid = sid;
            this.proposalSid = proposalSid;
            this.proposalEpoch = proposalEpoch;
            this.proposalZxid = proposalZxid;
        }
    }

    // 选出主时等待一下 还可能有更新的节点
    private final static long FINALIZE_TIME = 100;
    private final static int LEADER = 1;
    private final static int FOLLOWER = 2;
    private final static int LOOKING = 3;


    // 自己数据
    private int sid;
    private int epoch;
    private int zxid;
    private int state;

    // 选举的数据
    private int proposalSid;
    private int proposalEpoch;
    private int proposalZxid;

    // 读队列
    private LinkedBlockingQueue<Vote> readQ;
    // 写队列
    private LinkedBlockingQueue<Vote>[] writeQs;
    // 计票器
    private Map<Integer, Integer> votes = new HashMap<>();

    public FastLeaderElection(int sid, int epoch, int zxid,
            LinkedBlockingQueue<Vote> readQ,
            LinkedBlockingQueue<Vote>[] writeQs) {
        this.sid = sid;
        this.epoch = epoch;
        this.zxid = zxid;
        this.readQ = readQ;
        this.writeQs = writeQs;
    }

    @Override
    public void run() {
        try {
            state = LOOKING;
            // 号召大家选我
            updateProposal(sid, epoch, zxid);
            broadcast();
            while (state == LOOKING) {
                Vote vote = readQ.poll(FINALIZE_TIME, TimeUnit.MILLISECONDS);
                if (newerThanCurrent(vote)) {
                    updateProposal(vote.proposalSid, vote.proposalEpoch, vote.proposalZxid);
                    broadcast();
                }
                //计票
                votes.put(vote.sid, vote.proposalSid);
                if (hasAllQuorums(proposalSid)) {
                    // 可能出现了
                    Vote finalVote;
                    while ((finalVote = readQ.poll(FINALIZE_TIME, TimeUnit.MILLISECONDS)) != null) {
                        if (newerThanCurrent(finalVote)) {
                            readQ.put(finalVote);
                            break;
                        }
                    }
                    // 确定了
                    state = proposalSid == sid ? LEADER : FOLLOWER;
                    printResult();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printResult() {
        System.out.println(sid + " => " + (state == LEADER ? "LEADER" : "FOLLOWER"));
    }

    private boolean newerThanCurrent(Vote vote) {
        return (vote.proposalEpoch > proposalEpoch)
                || (vote.proposalEpoch == proposalEpoch && vote.proposalZxid > proposalZxid)
                || (vote.proposalEpoch == proposalEpoch && vote.proposalZxid == proposalZxid && vote.proposalSid > proposalSid);
    }

    private void broadcast() throws InterruptedException {
        Vote vote = new Vote(sid, proposalSid, proposalEpoch, proposalZxid);
        for (LinkedBlockingQueue<Vote> writeQ : writeQs) {
            writeQ.put(vote);
        }
    }

    private void updateProposal(int sid, int epoch, int zxid) {
        this.proposalSid = sid;
        this.proposalZxid = zxid;
        this.proposalEpoch = epoch;
    }

    private boolean hasAllQuorums(int proposalSid) {
        Collection<Integer> values = votes.values();
        int count = 0;
        for (Integer value : values) {
            if (proposalSid == value) {
                count++;
            }
        }
        return count >= (writeQs.length + 1) / 2 + 1;
    }

    public static void main(String[] args) {
        LinkedBlockingQueue<Vote> q1 = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<Vote> q2 = new LinkedBlockingQueue<>();
        LinkedBlockingQueue<Vote> q3 = new LinkedBlockingQueue<>();

        FastLeaderElection node1 = new FastLeaderElection(1, 1, 6, q1,
                new LinkedBlockingQueue[]{q2, q3});
        FastLeaderElection node2 = new FastLeaderElection(2, 1, 5, q2,
                new LinkedBlockingQueue[]{q1, q3});
        FastLeaderElection node3 = new FastLeaderElection(3, 1, 5, q3,
                new LinkedBlockingQueue[]{q1, q2});

        node1.start();
        node2.start();
        node3.start();
    }
}

