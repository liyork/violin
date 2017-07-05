package com.wolf.utils;

import com.wolf.utils.log.LogUtils;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Random;

/**
 * Description:redis事务是原子顺序执行的。
 * <br/> Created on 2016/8/24 9:12
 *
 * @author 李超()
 * @since 1.0.0
 */
public class RedisLockUtils {

    static JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
    static JedisPool pool2;
    public static Jedis jedis = new Jedis("127.0.0.1");

    public static void init() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(2000);
        poolConfig.setMaxIdle(500);
        pool2 = new JedisPool(poolConfig, "127.0.0.1");
    }

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


    //=========start  本想用另一个key作为cas来操作，但是一想两个操作之间还是有间隙，还是需要锁或者luna表达式保证原子
    public void increaseHasTop1() {

        Long result = null;

        String comment = "commentValue";
        String s = jedis.get(comment);
        int i = Integer.parseInt(s);
        if(i < 100) {
            result = compareAndSet(comment, i, i + 1);
        }

        System.out.println(result);
    }

    public Long compareAndSet(String key, Integer oldValue, Integer newValue) {
        String casKey = "casKey";
        Integer s = Integer.parseInt(jedis.get(key));
        String set = getRandomValue(casKey);
        if(set == null) {

        } else if(set != null && Integer.parseInt(set) == s) {

        }

        return null;
    }

    private String getRandomValue(String casKey) {
        Random random = new Random();
        int next = random.nextInt(1000000);
        return jedis.getSet(casKey, next + "");
    }

    //=========end

    /**
     * 担心watch会互相影响，所以注释掉了unwatch
     *
     * @param key
     * @return
     */
    public static Long increaseHasTop2(String key) {

        Random random = new Random();

        Long result = 100L;
        Jedis resource = null;
        while(null == resource) {
            try {
                resource = pool2.getResource();
            } catch (Exception e) {
            }

            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }


        boolean flag = true;
        while(flag) {
            resource.watch(key);
            //和上面watch不能换顺序，如果先获取了值1，然后有别人设定了2，那么再watch就没有意义了。
            Long currentValue = Long.parseLong(resource.get(key));

            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(currentValue < 100) {
                Transaction multi = resource.multi();
                multi.incr(key);
                //resource.incr(key);//使用了multi就不能使用redis额外发送命令了

                List<Object> exec = multi.exec();//执行EXEC命令后会取消对所有键的监控
                //exec不为null则表明watch期间没有修改
                if(null != exec) {
                    flag = false;
                    result = Long.parseLong(exec.get(0).toString());
                }
            } else {
                flag = false;
                //这里经过自己client上实验，似乎另一个client能unwatch，然后这个事务不能成功了。
                // 但是不知道为什么java测试可以，是不是从pool中获取的redis可以？？
                //resource.unwatch();
            }

        }

        return result;
    }


    /**
     * 每次都执行事务，只是有时带着++，有时什么也不带，这么做的目的是由于上面increaseHasTop2方法我不知道
     * 1.unwatch是否会将其他client取消掉
     * 2.如果去掉了unwatch，则另一个client又来一次watch，会不会有影响？
     * 所以使用这个稳妥方法，因为exec后所有watch会取消，
     * 那问题又来了。。如果这个事务执行完取消了watch，那么其他的事务那个watch怎么办？
     *
     * 注意watch的key是对整个连接有效的，事务也一样。如果连接断开，监视和事务都会被自动清除
     * 当然exec，discard，unwatch命令，及客户端连接关闭都会清除连接中的所有监视。
     * 如果watch一个不稳定(有生命周期)的key并且此key自然过期，exec仍然会执行事务队列的指令。
     *
     * @param key
     * @return
     */
    public static Long increaseHasTop3(String key) {

        Random random = new Random();

        Long result = 100L;
        Jedis resource = null;
        while(null == resource) {
            try {
                resource = pool2.getResource();
            } catch (Exception e) {
            }

            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
        }


        boolean flag = true;
        while(flag) {
            resource.watch(key);
            //和上面watch不能换顺序，如果先获取了值1，然后有别人设定了2，那么再watch就没有意义了。
            Long currentValue = Long.parseLong(resource.get(key));

            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Transaction multi = resource.multi();
            if(currentValue < 100) {
                multi.incr(key);
            }

            List<Object> exec = multi.exec();//执行EXEC命令后会取消对所有键的监控
            //exec不为null则表明watch期间没有修改
            if(null != exec) {
                flag = false;
                //如果上面满足了条件，则只是简单地执行了事务，但是没有任何返回值
                if(!CollectionUtils.isEmpty(exec)) {
                    result = Long.parseLong(exec.get(0).toString());
                }
            }

        }

        return result;
    }


    /**
     * 经过仔细测试，发现watch针对每个client的。。。。互补影响,那就可以实现unwatch了
     *
     * @param key
     * @return
     */
    public static Long increaseHasTop4(String key) {

        Random random = new Random();

        Long result = 100L;
        Jedis resource = null;
        //允许服务调用超时，添加重试机制，一开始提示超时连接，这会想找下原因还没有了。。。
        while(null == resource) {
            try {
                resource = pool2.getResource();
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(null == resource) {
                try {
                    Thread.sleep(random.nextInt(1000));
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }

        //每个线程都是自己获取一个自己的的redis客户端
        System.out.println("resource:"+ resource);


        boolean flag = true;
        while(flag) {
            resource.watch(key);

            Long currentValue = Long.parseLong(resource.get(key));

            try {
                Thread.sleep(random.nextInt(1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if(currentValue < 100) {
                Transaction multi = resource.multi();
                multi.incr(key);

                List<Object> exec = multi.exec();//执行EXEC命令后会取消对所有键的监控
                //exec不为null则表明watch期间没有修改
                if(null != exec) {
                    flag = false;
                    result = Long.parseLong(exec.get(0).toString());
                }
            } else {
                flag = false;
                resource.unwatch();//call UNWATCH so that the connection can already be used freely for new transactions.
            }

        }

        return result;
    }

}
