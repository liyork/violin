package com.wolf.utils;

import com.wolf.utils.log.LogUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Description:
 * <br/> Created on 2016/8/24 9:12
 *
 * @author 李超()
 * @since 1.0.0
 */
public class RedisLockUtils {

    static JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
    static Jedis jedis = new Jedis("127.0.0.1");

    /**
     * 不想重试，一次失败就失败了
     *
     * @param key
     * @param timeout
     * @return
     */
    public static boolean lock(String key, int timeout) {
        jedis = pool.getResource();
        boolean flag = false;
        try {
            flag = jedis.setnx(key, key) == 1;
            if(flag) {
                jedis.expire(key, timeout);
            } else {
                Long expire = jedis.ttl(key);
                if(expire == null) {
                    jedis.expire(key, timeout);
                }
            }
        } catch (Exception e) {
//            LoggerUtil.getExcepLogger().error("lock error key = " + key + ",value = " + value + ",timeout = " + timeout);
        }
        return flag;
    }

    /**
     * 假设每个锁用时很短，可以使用这个
     *
     * @param key
     * @param timeout
     * @return
     */
    public static boolean lockHasSecondTry(String key, int timeout) {
        jedis = pool.getResource();
        boolean flag = false;
        try {
            long nowTime = System.currentTimeMillis();
            String timeoutString = nowTime + timeout + "";
            flag = jedis.setnx(key, timeoutString) == 1;
            if(!flag) {
                String oldTime = jedis.get(key);
                //上一个释放了锁 或者 上一个锁超时了但是redis还没清理
                if(oldTime == null || Long.parseLong(oldTime) < nowTime) {
                    //尝试获取锁,只有第一个获取的和oldTime一样，然后就设定了timeoutTime，其他人获取的都是timeoutTime
                    String temp = jedis.getSet(key, timeoutString);
                    if(temp.equals(oldTime)) {
                        flag = true;
                    }
                }
            }
            if(flag) {
                jedis.expire(key, timeout);
            } else {
//                LoggerUtil.warn(LoggerUtil.LOCK_ERROR, key, "toLockRemark = " + remark, "lockingRemark = " + getLockRemark(key));
            }
        } catch (Exception e) {
            LogUtils.info("lock error key = " + key + ", timeout = " + timeout, e);
        }
        return flag;
    }


    public static void unLock(String key) {
        jedis.del(key);
    }



    public static boolean errorLock(String key, int timeout) {
        boolean flag = false;
        try {
            long nowTime = System.currentTimeMillis();
            String timeoutString = nowTime + timeout + "";
            flag = jedis.setnx(key, timeoutString) == 1;
            if(!flag) {
                String oldTime = jedis.get(key);
                //上一个释放了锁 或者 上一个锁超时了但是redis还没清理
                if(oldTime == null || Long.parseLong(oldTime) < nowTime) {
                    //尝试获取锁,只有第一个获取的和oldTime一样，然后就设定了timeoutTime，其他人获取的都是timeoutTime
                    String temp = jedis.getSet(key, timeoutString);
                    if(temp.equals(oldTime)) {
                        flag = true;
                    }
                }
            }
            if(flag) {
                jedis.expire(key, timeout);
            } else {
//                LoggerUtil.warn(LoggerUtil.LOCK_ERROR, key, "toLockRemark = " + remark, "lockingRemark = " + getLockRemark(key));
            }
        } catch (Exception e) {
            LogUtils.info("lock error key = " + key + ", timeout = " + timeout, e);
        }
        return flag;
    }
}
