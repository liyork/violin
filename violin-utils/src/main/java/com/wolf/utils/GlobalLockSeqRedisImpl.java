package com.wolf.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Description:
 * <br/> Created on 2017/6/15 10:57
 *
 * @author 李超
 * @since 1.0.0
 */
public class GlobalLockSeqRedisImpl {


    private static long maxWaitSeconds = 2;

    private static final String split = "____";

    private static AtomicInteger seqCount = new AtomicInteger();


    private static String host = null;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoadHierarchyProperties.class);

    private final static ExecutorService threadPoolExecutor = Executors.newCachedThreadPool();

    private final Jedis jedis;

    private final int maxLockSeconds;

    private final long sleepTimeMillis;

    private Thread exclusiveOwnerThread;

    private final String key;

    private final Lock lock = new ReentrantLock();

    private String value = null;


    static {
        InetAddress localHost = null;
        try {
            localHost = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        host = (UUID.randomUUID() + localHost.getHostAddress()).replace(split, "");
    }


    public GlobalLockSeqRedisImpl(Jedis jedis, int maxLockSeconds, long sleepTimeMillis, String key) {
        this.jedis = jedis;
        this.maxLockSeconds = maxLockSeconds;
        if(maxLockSeconds > 30) {
            maxLockSeconds = 30;
        }
        this.sleepTimeMillis = sleepTimeMillis;
        if(sleepTimeMillis > 1000) {
            sleepTimeMillis = 1000;
        }
        this.key = key;
    }


    private boolean setIfAbsent(String key, String value, int expireMilliSeconds) {
        String result = jedis.set(key, value, "nx", "px", expireMilliSeconds);
        if(result != null && result.equalsIgnoreCase("OK")) {
            return true;
        }
        return false;

    }


    private int lockRedisSeq(long startMillis) {
        int seqNo = seqCount.incrementAndGet();

        value = host + split + new Date().getTime() + split + seqNo;
        Long listSize = jedis.lpush(key, value);
        int count = 1;
        LOGGER.info("listSize=" + listSize);
        if(listSize.longValue() == 1l) {
            // 长度是1,说明只有自己拿到锁.
            return count;
        } else {
            // 如果锁过多,打印error报警
            if(listSize > 20000) {
                LOGGER.error("too_much_lock_node listSize=" + listSize);
            } else {
                LOGGER.info("lock_queue_size=" + listSize + ",key=" + key);
            }
            // 说明没有锁住
            while(true) {
                try {
                    Thread.sleep(sleepTimeMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // -1代表从尾部获取.FIFO原则
                String tailNodeValue = jedis.lindex(key, -1);
                if(tailNodeValue == null) {
                    // 自己的节点莫名其妙被人删除了
                    String message = "tailNodeValue is null ,all node is delete inclued itself key=" + key;
                    LOGGER.error(message);
                    throw new RuntimeException(message);
                } else if(tailNodeValue.equals(value)) {
                    // 拿到锁了
                    return count;
                } else {
                    String[] split = tailNodeValue.split(GlobalLockSeqRedisImpl.split);
                    String host = split[0];
                    // 保持数据兼容性避免报错
                    Date nodeCreateTime = new Date(Long.valueOf(split[1]));
                    long diffInMillis = new Date().getTime() - nodeCreateTime.getTime();
                    long maxLockTime = TimeUnit.MINUTES.toMillis(1);
                    if(host.equals(this.host) && diffInMillis > TimeUnit.SECONDS.toMillis(3)) {
                        // 发现当前节点的机器是本机(不用担心不同机器的时间不一致问题),且超过3秒,立即删除.
                        lrem(tailNodeValue);
                    }
                    if(diffInMillis > TimeUnit.SECONDS.toMillis(30) && diffInMillis <= maxLockTime) {
                        // 大量error报警,发现问题.请检查对应ip机器和日志所在机器的时间是否一致.并且java默认时区是否一致
                        LOGGER.error("Lock Node donot unLock ,请检查对应机器的时间是否一致,key=" + key + " unlockNodeIp= " + split[0] + ",nodeCreateTime=" + nodeCreateTime);
                    } else if(diffInMillis > maxLockTime) {
                        // 判断下别人节点的时间是否超时
                        LOGGER.error("Lock Node donot unLock ,请检查对应机器的时间是否一致,key=" + key + " unlockNodeIp= " + split[0] + ",nodeCreateTime=" + nodeCreateTime);
                        lrem(tailNodeValue);

                    }
                }

                long now = System.currentTimeMillis();
                long costTime = now - startMillis;
                count++;

                if(costTime > TimeUnit.SECONDS.toMillis(maxLockSeconds)) {
                    String errorString = getErrorString(count, costTime);
                    throw new RuntimeException(errorString);
                }

            }
        }
        //
    }


    private void lrem(String tailNodeValue) {
        // -1代表从尾部获取.FIFO原则.采用rem时间复杂度可能增加,这种情况毕竟少见
        long remCount = jedis.lrem(key, -2, tailNodeValue);
        if(remCount != 1) {
            LOGGER.error("del count dot not euqal 1,remCount=" + remCount);
        }
    }


    public void lock() {
        long startTime = System.currentTimeMillis();

        // 抢占本地锁,会自动唤醒
        try {
            boolean success = lock.tryLock(maxWaitSeconds, TimeUnit.SECONDS);
            if(!success) {
                long end = System.currentTimeMillis();
                // 超时
                long costTime = end - startTime;
                String message = getErrorString(1, costTime);
                LOGGER.error("lock timeOut " + message);
                throw new RuntimeException(message);
            }
        } catch (Exception e) {
            long end = System.currentTimeMillis();
            // 被其他人中断
            long costTime = end - startTime;
            String message = getErrorString(1, costTime);
            LOGGER.error("lock meet exception " + message, e);
            throw new RuntimeException(message);
        }
        int tryCount = lockRedisSeq(startTime);
        exclusiveOwnerThread = Thread.currentThread();
        long endTime = System.currentTimeMillis();
        long costTime = endTime - startTime;
        LOGGER.info("get redis global lock success,maxLockSeconds=" + maxLockSeconds + ",costTimeMillis=" + costTime + ",key=" + key + ",retryCount=" + tryCount + ",sleepTimeMillis=" + sleepTimeMillis + ",costTimePerTime=" + costTime / ((double) tryCount) + ",startTime=" + startTime + ",endTime=" + endTime);
    }


    private int lockRedis(long startTime) {
        // 抢占远程分布式锁,无解锁通知,故采用自旋等待
        int tryCount = 0;
        while(true) {
            tryCount++;
            // setIfAbsent tps对于并发锁控制够用了.
            Boolean result = this.setIfAbsent(key, "1", maxLockSeconds * 1000);
            // 处理锁的自动释放,前三次尝试加锁都会进行超时设置,保证分布式锁有timeOut.避免主加锁进程被无故停止,导致key无失效时间,锁永远不被释放.

            if(result == null || !result) {// 加锁失败,阻塞调用线程
                LOGGER.info(" spin ,key=" + key + ",tryCount=" + tryCount);
                try {
                    Thread.sleep(sleepTimeMillis);
                } catch (InterruptedException e) {
                    // nothing need to be done
                }
                long end = System.currentTimeMillis();

                // 超时
                long costTime = end - startTime;
                // 分布式自旋等待时间已经已经超过了某个时间(暂定位为3秒),说明分布式竞争失败或者key没有正确的被设置超时时间.
                long sleepMillisWaterMark = TimeUnit.SECONDS.toMillis(maxWaitSeconds);
                if(costTime > sleepMillisWaterMark) {
                    String message = getErrorString(tryCount, costTime);
                    // 抛运行期异常,幂等重复执行不会总是抛错.
                    throw new RuntimeException(message);
                }
                continue;
            }
            exclusiveOwnerThread = Thread.currentThread();

            break;
        }
        return tryCount;
    }


    private String getErrorString(int tryCount, long costTime) {
        return "get redis global lock error .1. compete failed 2. key do not set timeOut ,exist for ever ,maxLockSeconds=" + maxLockSeconds + ",costTimeMillis=" + costTime + ",key=" + key + ",retryCount=" + tryCount + ",sleepTimeMillis=" + sleepTimeMillis + ",costTimePerTime=" + (costTime / ((double) tryCount));
    }


    public void unlock() {
        long startTime = System.currentTimeMillis();
        try {
            lock.unlock();
        } catch (IllegalMonitorStateException e) {
            LOGGER.error("IllegalMonitorStateException", e);
        }
        if(exclusiveOwnerThread == Thread.currentThread()) {

            // 解锁,如果解锁失败,重试解锁三次
            Long delCount = unlockSeqRedisAndRetryIfError();
            long endTime = System.currentTimeMillis();
            exclusiveOwnerThread = null;
            LOGGER.info(" global unlock,del count=" + delCount + ".key=" + key + " ,costTime millis=" + (endTime - startTime) + ",startTime=" + startTime + ",endTime=" + endTime);
        } else {
            LOGGER.info(" thread do not get lock ,can not unlock. key=" + key + ",exclusiveOwnerThread=" + exclusiveOwnerThread + ",current thrad=" + Thread.currentThread());
        }

    }


    private Long unlockSeqRedisAndRetryIfError() {
        Long delCount = 0l;
        try {
            delCount = unlockSeqRedisLock();
        } catch (Exception e) {
            LOGGER.error("unlockSeqRedisLock error", e);
            threadPoolExecutor.execute(getRetryUnlockTask());
        }
        return delCount;
    }


    private Runnable getRetryUnlockTask() {
        return new Runnable() {


            public void run() {
                sleepSlience();
                for(int i = 0; i < 3; i++) {
                    try {
                        unlockSeqRedisLock();
                    } catch (Exception e) {
                        // 出现网络超时等异常情况时,重试
                        LOGGER.error("unlockSeqRedisLock error", e);
                        sleepSlience();
                        continue;
                    }
                    break;
                }
            }


            private void sleepSlience() {
                try {
                    Thread.sleep(1000l);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }

        };
    }


    @Deprecated
    // 请使用unlockSeqRedisAndRetryIfError


    private Long unlockSeqRedisLock() {
        // -1代表从尾部删除.FIFO原则
        String lastNodeValue = jedis.rpop(key);
        if(!value.equals(lastNodeValue)) {
            LOGGER.error("del result do not match expect value,expect=" + value + ",acutal value=" + lastNodeValue);
            // 不能用Rpush,否则将导致锁节点被随意变更,造成混乱.
            jedis.lpush(key, lastNodeValue);
            Long lrem = jedis.lrem(key, -2, value);
            if(lrem != 1) {
                LOGGER.error("del resutl do not match expect value,expect" + value);
            }
            return lrem;
        }
        return 1l;

    }


    private Long unlockRedisLock() {
        return jedis.del(key);
    }


    public int getMaxLockSeconds() {
        return maxLockSeconds;
    }
}
