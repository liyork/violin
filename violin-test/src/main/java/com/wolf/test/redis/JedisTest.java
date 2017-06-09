package com.wolf.test.redis;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2015/6/19
 */

import org.apache.commons.collections.CollectionUtils;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.util.JedisClusterCRC16;

import java.net.SocketTimeoutException;
import java.net.URI;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author: flychao88
 * Time: 2012.5.7 16:23:15
 */
public class JedisTest {

    JedisPool pool;
    Jedis jedis;
    Logger logger = LoggerFactory.getLogger(JedisTest.class);

    @Before
    public void setUp() {

        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(1000);//最大活动数
        poolConfig.setMaxIdle(100);//最大闲置数
        poolConfig.setMaxWaitMillis(5000);//连接池中没有active/idle的连接时，会等待maxWait时间

//        URI uri = URI.create("http://127.0.0.1:6379");
        URI uri = URI.create("http://127.0.0.1:6380");
        //连接超时，读取超时
        pool = new JedisPool(poolConfig, uri, 2000, 2000);

        jedis = getJedis();
        //jedis.auth("password");
    }

    /**
     * Redis存储初级的字符串
     * CRUD
     */
    @Test
    public void testBasicString() {
        //-----添加数据----------
        jedis.set("name", "minxr");//向key-->name中放入了value-->minxr
        System.out.println(jedis.get("name"));//执行结果：minxr

        //-----修改数据-----------
        //1、在原来基础上修改
        jedis.append("name", "jarorwar");   //很直观，类似map 将jarorwar append到已经有的value之后
        System.out.println(jedis.get("name"));//执行结果:minxrjarorwar

        //2、直接覆盖原来的数据
        jedis.set("name", "闵晓荣");
        System.out.println(jedis.get("name"));//执行结果：闵晓荣

        //删除key对应的记录
        jedis.del("name");
        System.out.println(jedis.get("name"));//执行结果：null

        /**
         * mset相当于
         * jedis.set("name","minxr");
         * jedis.set("jarorwar","闵晓荣");
         */
        jedis.mset("name", "minxr", "jarorwar", "闵晓荣");
        System.out.println(jedis.mget("name", "jarorwar"));

    }

    /**
     * jedis操作Map
     */
    @Test
    public void testMap() {
        Map<String, String> user = new HashMap<String, String>();
        user.put("name", "minxr");
        user.put("pwd", "password");
        jedis.hmset("user", user);
        //取出user中的name，执行结果:[minxr]-->注意结果是一个泛型的List
        //第一个参数是存入redis中map对象的key，后面跟的是放入map中的对象的key，后面的key可以跟多个，是可变参数
        List<String> rsmap = jedis.hmget("user", "name");
        System.out.println(rsmap);

        //删除map中的某个键值
//        jedis.hdel("user","pwd");
        List<String> hmget = jedis.hmget("user", "pwd");
        System.out.println(hmget); //因为删除了，所以返回的是null
        Long user2 = jedis.hlen("user");
        System.out.println(user2); //返回key为user的键中存放的值的个数2
        System.out.println(jedis.exists("user"));//是否存在key为user的记录 返回true
        Set<String> user1 = jedis.hkeys("user");
        System.out.println(user1);//返回map对象中的所有key  [pwd, name]
        System.out.println(jedis.hvals("user"));//返回map对象中的所有value  [minxr, password]

        Iterator<String> iter = jedis.hkeys("user").iterator();
        while(iter.hasNext()) {
            String key = iter.next();
            System.out.println(key + ":" + jedis.hmget("user", key));
        }

    }

    /**
     * jedis操作List
     */
    @Test
    public void testList() {
        //开始前，先移除所有的内容
        String key = "java framework";
        jedis.del(key);
        List<String> lrange = jedis.lrange(key, 0, -1);
        System.out.println(lrange);
        //先向key java framework中存放三条数据
        jedis.lpush(key, "spring");
        jedis.lpush(key, "struts");
        jedis.lpush(key, "hibernate");
        //再取出所有数据jedis.lrange是按范围取出，
        // 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有
        List<String> lrange1 = jedis.lrange(key, 0, -1);
        System.out.println(lrange1);

        String lindex = jedis.lindex(key, 1);
        System.out.println(lindex);

        Long llen = jedis.llen(key);
        System.out.println(llen);

        String lpop = jedis.lpop(key);
        System.out.println(lpop);

        Long llen1 = jedis.llen(key);
        System.out.println(llen1);


    }

    /**
     * jedis操作Set
     */
    @Test
    public void testSet() {
        //添加
        String key = "sname";
        jedis.sadd(key, "minxr");
        jedis.sadd(key, "jarorwar");
        jedis.sadd(key, "闵晓荣");
        jedis.sadd(key, "noname");
        //移除noname
        jedis.srem(key, "noname");
        System.out.println(jedis.smembers(key));//获取所有加入的value
        System.out.println(jedis.sismember(key, "minxr"));//判断 minxr 是否是sname集合的元素
        System.out.println(jedis.srandmember(key));
        System.out.println(jedis.scard(key));//返回集合的元素个数
    }

    @Test
    public void test() throws InterruptedException {
        //keys中传入的可以用通配符
        System.out.println(jedis.keys("*")); //返回当前库中所有的key  [sose, sanme, name, jarorwar, foo, sname, java framework, user, braand]
        System.out.println(jedis.keys("*name"));//返回的sname   [sname, name]
        System.out.println(jedis.del("sanmdde"));//删除key为sanmdde的对象  删除成功返回1 删除失败（或者不存在）返回 0
        System.out.println(jedis.ttl("sname"));//返回给定key的有效时间，如果是-1则表示永远有效
        jedis.setex("timekey", 10, "min");//通过此方法，可以指定key的存活（有效时间） 时间为秒
        Thread.sleep(5000);//睡眠5秒后，剩余时间将为<=5
        System.out.println(jedis.ttl("timekey"));   //输出结果为5
        jedis.setex("timekey", 1, "min");        //设为1后，下面再看剩余时间就是1了
        System.out.println(jedis.ttl("timekey"));  //输出结果为1
        System.out.println(jedis.exists("key"));//检查key是否存在
        System.out.println(jedis.rename("timekey", "time"));
        System.out.println(jedis.get("timekey"));//因为移除，返回为null
        System.out.println(jedis.get("time")); //因为将timekey 重命名为time 所以可以取得值 min

        //jedis 排序
        //注意，此处的rpush和lpush是List的操作。是一个双向链表（但从表现来看的）
        jedis.del("a");//先清除数据，再加入数据进行测试
        jedis.rpush("a", "1");
        jedis.lpush("a", "6");
        jedis.lpush("a", "3");
        jedis.lpush("a", "9");
        System.out.println(jedis.lrange("a", 0, -1));// [9, 3, 6, 1]
        List<String> a = jedis.sort("a");
        System.out.println(a); //[1, 3, 6, 9]  //输入排序后结果
        System.out.println(jedis.lrange("a", 0, -1));

    }

    public static void main(String[] args) throws Exception {
//        JedisPool pool = new JedisPool(new JedisPoolConfig(), "xx.xx.xx.xx");
//        Jedis jedis = pool.getResource();
//
//        Set<String> keys = jedis.keys("*");
//        System.out.println(keys.size());
//
//        System.out.println("2222222222");
//        String xx = jedis.get("xx_driver_weekrewardSql_version");
//        System.out.println(xx);


        //测试最大值，一直跑不出来，最后看源码应该还是long long类型的
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
        Jedis jedis = pool.getResource();
        String key = "autoIncrease";

        jedis.del(key);

        jedis.set(key, "1");

        for(int i = 0; i < Integer.MAX_VALUE; i++) {
            Long incr = jedis.incr(key);
            System.out.println(incr);
        }

    }

    @Test
    public void testCluster() {
        String key = "1417";
        //github上有这一句，Jedis Cluster 会自动去发现集群中的节点，所以JedisClusterNodes只需要 add一个实例
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("127.0.0.1", 6379));
        JedisCluster jc = new JedisCluster(jedisClusterNodes);

        jc.setnx(key, "bar");
        String value = jc.get(key);
        System.out.println("key-" + key + " slot-" + JedisClusterCRC16.getSlot(key) + " value-" + value);

        String key2 = "288";
        jc.setnx(key2, "bar2");
        String value2 = jc.get(key);
        System.out.println("key-" + key2 + " slot-" + JedisClusterCRC16.getSlot(key2) + " value-" + value2);
    }


    /**
     * 按时间维度筛选
     */
    @Test
    public void testLatestN() {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
        Jedis jedis = pool.getResource();
        String key = "latestN";

        jedis.del(key);

        jedis.lpush(key, "1");
        jedis.lpush(key, "2");
        jedis.lpush(key, "3");
        jedis.lpush(key, "4");
        jedis.lpush(key, "5");

        int latestN = 3;
        List<String> lrange = jedis.lrange(key, 0, latestN - 1);
        for(String s : lrange) {
            System.out.println(s);
        }

        //当添加新元素后，始终保持前N个数据
        jedis.lpush(key, "6");
        jedis.ltrim(key, 0, 5);

        //这里取出的数据如果不够n，可以从db中获取
        List<String> lrange1 = jedis.lrange(key, 0, latestN - 1);
        for(String s : lrange1) {
            System.out.println(s);
        }

        //也可以创建不同分类的list用来查询
    }

    /**
     * 按某种条件维度筛选
     */
    @Test
    public void testTopN() {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
        Jedis jedis = pool.getResource();
        String key = "topN";

        jedis.del(key);

        jedis.zadd(key, 5, "a");
        jedis.zadd(key, 1, "b");
        jedis.zadd(key, 3, "c");
        jedis.zadd(key, 4, "d");
        jedis.zadd(key, 2, "e");

        int topN = 3;
        Set<String> zrange = jedis.zrange(key, 0, topN - 1);
        for(String s : zrange) {
            System.out.println(s);
        }
    }

    /**
     * 过期筛选
     */
    @Test
    public void testExpireSelected() {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
        Jedis jedis = pool.getResource();
        String key = "topN";

        jedis.del(key);

        Calendar instance = Calendar.getInstance();
        instance.add(Calendar.MINUTE, -1);
        long timea = instance.getTime().getTime();
        jedis.zadd(key, timea, "a");
        instance.add(Calendar.MINUTE, 2);
        long timeb = instance.getTime().getTime();
        jedis.zadd(key, timeb, "b");
        instance.add(Calendar.MINUTE, 1);
        long timec = instance.getTime().getTime();
        jedis.zadd(key, timec, "c");
        instance.add(Calendar.MINUTE, 1);
        long timed = instance.getTime().getTime();
        jedis.zadd(key, timed, "d");
        instance.add(Calendar.MINUTE, 1);
        long timee = instance.getTime().getTime();
        jedis.zadd(key, timee, "e");

        //前几名代表时间较长的数据，可以用来删除，然后用这个时间戳当做索引去数据中删除
        int topN = 3;
        Set<String> zrange = jedis.zrange(key, 0, topN - 1);
        for(String s : zrange) {
            System.out.println(s);
        }
    }

    /**
     * 原子++ --
     */
    @Test
    public void testAtomicIncr() {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
        Jedis jedis = pool.getResource();
        String key = "autoIncrease";

        jedis.del(key);

        jedis.set(key, "1");

        Long incr = jedis.incr(key);
        System.out.println(incr);
        Long aLong = jedis.incrBy(key, 2);
        System.out.println(aLong);
        Long decr = jedis.decr(key);
        System.out.println(decr);
        Long decr2 = jedis.decrBy(key, 2);
        System.out.println(decr2);

    }

    /**
     * 去除重复数据
     * 一般set，无序，不重复
     */
    @Test
    public void testSAdd() {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
        Jedis jedis = pool.getResource();
        String key = "set";

        jedis.del(key);

        jedis.sadd(key, "1");
        jedis.sadd(key, "1");
        jedis.sadd(key, "2");
        jedis.sadd(key, "3");
        jedis.sadd(key, "4");

        Set<String> sinter = jedis.sinter(key);
        for(String s : sinter) {
            System.out.println(s);
        }

    }

    /**
     * 队列
     */
    @Test
    public void testQueue() {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
        Jedis jedis = pool.getResource();
        String key = "queue";

        jedis.del(key);

        jedis.lpush(key, "1");
        jedis.lpush(key, "2");
        jedis.lpush(key, "3");
        jedis.lpush(key, "4");

        List<String> lrange = jedis.lrange(key, 0, jedis.llen(key) - 1);
        for(String s : lrange) {
            System.out.println(s);
        }

        String lpop1 = jedis.lpop(key);
        System.out.println(lpop1);
        String lpop2 = jedis.lpop(key);
        System.out.println(lpop2);
        System.out.println(jedis.llen(key));

    }

    /**
     * Pipeline 批量发送请求,节省通信时间
     * 注：redis 必须在处理完所有命令前先缓存起所有命令的处理结果，所以内存会有占用
     */
    @Test
    public void testPipeline() {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
        Jedis jedis = pool.getResource();

        jedis.del("age");

        Pipeline pipelined = jedis.pipelined();
        pipelined.set("age", "123");

        for(int i = 0; i < 20; i++) {
            pipelined.incr("age");
        }

        pipelined.sync();

        String age = jedis.get("age");
        System.out.println(age);
    }

    /**
     * 使用脚本的好处：
     * 减少网络开销。可以将多个请求通过脚本的形式一次发送，减少网络时延
     * 原子操作。redis会将整个脚本作为一个整体执行，中间不会被其他命令插入。因此在编写脚本的过程中无需担心会出现竞态条件，无需使用事务。
     * 复用。客户端发送的脚步会永久存在redis中，这样，其他客户端可以复用这一脚本而不需要使用代码完成相同的逻辑。
     */
    @Test
    public void testIsExceedRate() {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
        Jedis jedis = pool.getResource();

        RateLimit rateLimit = new RateLimit(jedis);
        try {
            rateLimit.init();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //模拟请求1秒3次
        for(int i = 0; i < 19; i++) {
            if(rateLimit.isExceedRate("rate.frequency.limiting:" + "/xxx/yy/qqq", 5, 10)) {//5秒10次
                System.out.println("2222");
            } else {
                System.out.println("1111");
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 事务
     */
    @Test
    public void testTransaction() {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
        Jedis jedis = pool.getResource();

        jedis.del("age");

        Pipeline pipelined = jedis.pipelined();
        pipelined.multi();
        pipelined.set("age", "123");

        for(int i = 0; i < 20; i++) {
            pipelined.incr("age");
        }

        pipelined.exec();//提交事务

        pipelined.sync();//关闭pipeline
        String age = jedis.get("age");
        System.out.println(age);
    }

    /**
     * ttl
     * -1表示永不过期
     * -2表示过期
     * 正值表示过期时间
     */
    @Test
    public void testTtl() throws InterruptedException {
        JedisPool pool = new JedisPool(new JedisPoolConfig(), "127.0.0.1");
        Jedis jedis = pool.getResource();

        jedis.del("ttltest");

        jedis.set("ttltest", "123");

        Long ttltest = jedis.ttl("ttltest");
        System.out.println(ttltest);

        jedis.expire("ttltest", 2);
        Long ttltest1 = jedis.ttl("ttltest");
        System.out.println(ttltest1);

        Thread.sleep(2000);
        Long ttltest2 = jedis.ttl("ttltest");
        System.out.println(ttltest2);
    }


    /**
     * 线程数多一些少一些也总有一些拿不到连接
     * JVM在线程很多的时候不稳定，而且网络偶尔丢包或者中断那么几毫秒还是很常见的，作为程序员需要考虑这种极端的环境，而不要太相信机器太相信逻辑。
     *
     * @throws InterruptedException
     */
    @Test
    public void testRedisPool() throws InterruptedException {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        //默认只有8个连接
        poolConfig.setMaxTotal(5000);
        JedisPool pool = new JedisPool(poolConfig, "127.0.0.1");

        final ExecutorService executorService = Executors.newFixedThreadPool(5000);
        for(int i = 0; i < 5000; i++) {
            try {
                final Jedis jedis = pool.getResource();
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
//                        jedis.set("key:"+ finalI, finalI +"");
                        System.out.println("jedis:" + jedis);
                        try {
                            Thread.sleep(10000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        executorService.shutdown();
    }


    /**
     * 建立重试机制
     *
     * @return
     */
    public Jedis getJedis() {
        int timeoutCount = 0;
        while(true) // 如果是网络超时则多试几次
        {
            try {
                return pool.getResource();
            } catch (Exception e) {
                // 底层原因是SocketTimeoutException，不过redis已经捕捉且抛出JedisConnectionException，不继承于前者
                if(e instanceof JedisConnectionException || e instanceof SocketTimeoutException) {
                    timeoutCount++;
                    logger.warn("getJedis timeoutCount={}", timeoutCount);
                    if(timeoutCount > 3) {
                        break;
                    }
                } else {
                    logger.warn("jedisInfo。NumActive=" + pool.getNumActive() + //
                    ", NumIdle=" + pool.getNumIdle() + ", NumWaiters=" + pool.getNumWaiters() +//
                    ", isClosed=" + pool.isClosed());
                    logger.error("getJedis error", e);
                    break;
                }
            }
        }
        return null;
    }


    @Test
    public void testConfig() {
        List<String> slaveof = jedis.configGet("slaveof");
        if(!CollectionUtils.isEmpty(slaveof)) {
            for(String s : slaveof) {
                System.out.println(s);
            }
        }
    }

}


