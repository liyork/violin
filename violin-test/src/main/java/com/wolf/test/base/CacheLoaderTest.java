package com.wolf.test.base;

import com.google.common.cache.*;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.*;

/**
 * Description: http://ifeve.com/google-guava-cachesexplained/
 * Created on 2021/4/8 5:46 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class CacheLoaderTest {

    public static void main(String[] args) throws Exception {
        //testCacheLoaderThread();

        //testCacheBuilder();

        //testCallback();

        //testException();

        //testPut();

        //testSizeBasedEviction();

        //testTimedEviction();

        //testReferenceBasedEviction();

        //testExplicitEviction();

        //testWhen();

        //testRefresh();

        testFetch();

        // 缓存穿透
        //Guava cache则对此种情况有一定控制。当大量线程用相同的key获取缓存值时，只会有一个线程进入load方法，而其他线程则等待，直到缓存值被生成。这样也就避免了缓存穿透的危险。
        //testLoadCount();
        //testLoadCount2();

        //testFetch2();

    }


    // 有一个对key进行load时，不会再有其他线程还能进行load
    private static void testLoadCount() throws ExecutionException {
        LoadingCache<Integer, String> cache = CacheBuilder.newBuilder()
                .build(new CacheLoader<Integer, String>() {
                    public String load(Integer key) throws InterruptedException { // no checked exception
                        System.out.println(Thread.currentThread().getName() + " load ..");
                        Thread.sleep(5000);
                        return key.toString();
                    }
                });

        new Thread(() -> {
            Integer key = 1;
            while (true) {
                try {
                    String s = cache.get(key);
                    System.out.println(Thread.currentThread().getName() + "成功, " + s);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        String s = cache.get(1);
        s = cache.get(1);
        System.out.println("main s:" + s);
    }

    // 这样也不会重复load
    private static void testLoadCount2() throws ExecutionException {
        LoadingCache<Integer, String> cache = CacheBuilder.newBuilder()
                .build(new CacheLoader<Integer, String>() {
                    public String load(Integer key) throws InterruptedException { // no checked exception
                        System.out.println(Thread.currentThread().getName() + " load ..");
                        Thread.sleep(2000);
                        return key.toString();
                    }
                });

        new Thread(() -> {
            Integer key = 1;
            try {
                String s = cache.get(key, () -> {
                    System.out.println(Thread.currentThread().getName() + " load1 ..");
                    Thread.sleep(2000);
                    return "1";
                });
                System.out.println(Thread.currentThread().getName() + "成功, " + s);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }).start();

        new Thread(() -> {
            Integer key = 1;
            try {
                String s = cache.get(key, () -> {
                    System.out.println(Thread.currentThread().getName() + " load2 ..");
                    Thread.sleep(2000);
                    return "1";
                });
                System.out.println(Thread.currentThread().getName() + "成功, " + s);
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }).start();

        cache.get(1);
    }


    // 实现多线程读，一线程写
    private static void testFetch() throws InterruptedException {
        BlockingQueue<Integer> blockingQueue = new LinkedBlockingDeque(1024);

        LoadingCache<Integer, String> cache = CacheBuilder.newBuilder()
                .maximumSize(5)
                // 设置缓存容器的初始容量，后期会扩容
                // 例如设置了initialCapacity为60，则设置了concurrencyLevel为8。将会把存储的空间分为8块，每块都有一个hash table结构，每个hash table的初始规模为8
                .initialCapacity(8)
                // 设置并发级别，并发级别是指可以同时写缓存的线程数
                .concurrencyLevel(8)
                .expireAfterAccess(30, TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, String>() {
                    public String load(Integer key) {
                        System.out.println("load .." + key);
                        boolean add = blockingQueue.add(key);
                        if (!add) {
                            System.out.println("queue is full!");
                        }
                        return "-1";// 默认正在查询
                    }
                });

        // 读,模拟前端，不断查询
        for (int i = 0; i < 2; i++) {
            new Thread(() -> {
                Integer key = 1;
                while (true) {
                    key++;
                    if (key == 5) {
                        key = 0;
                    }
                    try {
                        String s = cache.get(key);
                        if (s.equals("-1")) {
                            System.out.println(Thread.currentThread().getName() + " 正在查询中 " + key);
                            key--;
                        } else {
                            System.out.println(Thread.currentThread().getName() + "成功, " + s);
                        }
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            Thread.sleep(100);
        }

        // 写，后台查询线程
        Thread writeThread = new Thread(() -> {
            while (true) {
                try {
                    Integer key = blockingQueue.poll(5, TimeUnit.SECONDS);
                    if (null == key) {// 可以用于监听stop
                        System.out.println("queue is empty will retry");
                        Thread.sleep(200);
                        continue;
                    }
                    System.out.println(Thread.currentThread().getName() + "prepare fetch " + key);
                    String s = cache.get(key);
                    if (s.equals("-1")) {// 正在运⾏
                        System.out.println(Thread.currentThread().getName() + "fetch succ " + key);
                        cache.put(key, key.toString());//todo 考虑业务异常
                    } else {// 有正常值
                        continue;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        writeThread.setDaemon(true);
        writeThread.start();

        // 模拟手动刷新
        Thread.sleep(10000);
        cache.invalidateAll();
    }

    // 刷新
    // 刷新和回收不太一样。
    // 刷新表示为键加载新值，这个过程可以是异步的。在刷新操作进行时，缓存仍然可以向其他线程返回旧值，强制刷新的，即强制获取
    // 不像回收操作，读缓存的线程必须等待新值加载完成。
    // 如果刷新过程抛出异常，缓存将保留旧值，而异常会在记录到日志后被丢弃。
    // // 虽然不会有缓存穿透的情况，
    //    // 但是每当某个缓存值过期时，老是会导致大量的请求线程被阻塞。而Guava则提供了另一种缓存策略，缓存值定时刷新
    //    // 对于某个key的缓存来说，只会有一个线程被阻塞，用来生成缓存值，而其他的线程都返回旧的缓存值，不会被阻塞用refreshAfterWrite
    //    // 这里的定时并不是真正意义上的定时。Guava cache的刷新需要依靠用户请求线程，让该线程去触发load方法的调用，所以如果一直没有用户尝试获取该缓存值，则该缓存也并不会刷新
    private static void testRefresh() throws ExecutionException {
        LoadingCache<Integer, String> graphs = CacheBuilder.newBuilder()
                .build(new CacheLoader<Integer, String>() {
                    public String load(Integer key) { // no checked exception
                        System.out.println("load ..");
                        return key.toString();
                    }
                });
        graphs.refresh(1);
        graphs.get(1);
        graphs.refresh(1);
    }

    // 当缓存的key很多时，高并发条件下大量线程同时获取不同key对应的缓存，此时依然会造成大量线程阻塞，并且给数据库带来很大压力。这个问题的解决办法就是将刷新缓存值的任务交给后台线程，所有的用户请求线程均返回旧的缓存值，这样就不会有用户线程被阻塞了
    // 注意此时缓存的刷新依然需要靠用户线程来驱动，只不过和testRefresh不同之处在于该用户线程触发刷新操作之后，会立马返回旧的缓存值。
    private static void testFetch2() throws ExecutionException, InterruptedException {
        //LoadingCache<Integer, String> cache = CacheBuilder.newBuilder()
        //        .maximumSize(5)
        //        .expireAfterAccess(30, TimeUnit.SECONDS)
        //        .build(CacheLoader.asyncReloading(new CacheLoader<Integer, String>() {
        //            @Override
        //            public String load(Integer key) throws Exception {
        //                return "-1";
        //            }
        //        }, Executors.newSingleThreadExecutor()));

        // 方式2
        ListeningExecutorService backgroundRefreshPools =
                MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(20));

        CacheLoader<Integer, String> cacheLoader = new CacheLoader<Integer, String>() {
            @Override
            public String load(Integer key) throws Exception {
                System.out.println("in load1 " + Thread.currentThread().getName());
                return "1";
            }

            @Override
            public ListenableFuture<String> reload(Integer key, String oldValue) throws Exception {
                System.out.println("in load2 " + Thread.currentThread().getName());
                Thread.sleep(1000);
                return backgroundRefreshPools.submit(() -> "2");
            }
        };

        LoadingCache<Integer, String> cache = CacheBuilder.newBuilder()
                .maximumSize(5)
                .expireAfterAccess(30, TimeUnit.SECONDS)// 读/写
                .build(cacheLoader);

        String s = cache.get(1);
        System.out.println(s);
        cache.refresh(1);
        cache.get(1);
        Thread.sleep(1000);
        s = cache.get(1);
        System.out.println(s);


        backgroundRefreshPools.shutdown();
    }

    // 清理什么时候发生？
    // 1. 使用CacheBuilder构建的缓存不会"自动"执行清理和回收工作，也不会在某个缓存项过期后马上清理，也没有诸如此类的清理机制。
    // 相反，它会在写操作时顺带做少量的维护工作，或者偶尔在读操作时做——如果写操作实在太少的话。
    // 2. 这样做的原因在于：如果要自动地持续清理缓存，就必须有一个线程，这个线程会和用户操作竞争共享锁。
    // 此外，某些环境下线程创建可能受限制，这样CacheBuilder就不可用了。
    // 3. 相反，我们把选择权交到你手里。如果你的缓存是高吞吐的，那就无需担心缓存的维护和清理等工作。
    // 如果你的 缓存只会偶尔有写操作，而你又不想清理工作阻碍了读操作，那么可以创建自己的维护线程，以固定的时间间隔调用Cache.cleanUp()。
    // ScheduledExecutorService可以帮助你很好地实现这样的定时调度。
    private static void testWhen() {
    }

    // 显示清除
    private static void testExplicitEviction() throws ExecutionException {
        LoadingCache<Integer, String> graphs = CacheBuilder.newBuilder()
                .build(new CacheLoader<Integer, String>() {
                    public String load(Integer key) { // no checked exception
                        System.out.println("load ..");
                        return key.toString();
                    }
                });
        //graphs.invalidate(1);
        graphs.invalidateAll();
        //graphs.invalidateAll(new ArrayList());
        //graphs.cleanUp();

        graphs.get(1);
        //graphs.invalidateAll();// 失效所有
        graphs.cleanUp();// 清理工作，不失效任何
        String s = graphs.get(1);
        System.out.println(s);
        graphs.get(1);
    }

    // Reference-based Eviction
    private static void testReferenceBasedEviction() {
        Cache<Integer, String> graphs = CacheBuilder.newBuilder()
                .weakKeys()// 使用弱引用存储键
                .weakValues()// 使用弱引用存储值
                .softValues()// 使用软引用存储值
                .build();
    }

    // Timed Eviction
    private static void testTimedEviction() throws java.util.concurrent.ExecutionException, InterruptedException {
        LoadingCache<Integer, String> graphs = CacheBuilder.newBuilder()
                .maximumSize(5)
                //.expireAfterAccess(1, TimeUnit.SECONDS)
                .expireAfterWrite(1, TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, String>() {
                    public String load(Integer key) { // no checked exception
                        System.out.println("load ..");
                        return key.toString();
                    }
                });
        graphs.get(1);
        Thread.sleep(2000);
        String s = graphs.get(1);
        System.out.println(s);
    }

    // size-based eviction,发生在缓存项的数目逼近限定值时。
    private static void testSizeBasedEviction() throws java.util.concurrent.ExecutionException {
        LoadingCache<Integer, String> graphs = CacheBuilder.newBuilder()
                .maximumSize(1)
                //.maximumWeight(1)// 最大总重,不能和maximumSize一起使用。
                //.weigher((Weigher<Integer, String>) (i, v) -> i + 1)// key的权重
                .build(new CacheLoader<Integer, String>() {
                    public String load(Integer key) { // no checked exception
                        return key.toString();
                    }
                });
        graphs.get(1);
        String s = graphs.get(2);
        System.out.println(s);
    }

    private static void testPut() throws java.util.concurrent.ExecutionException {
        Cache<Integer, String> cache = CacheBuilder.newBuilder().maximumSize(1000).build();

        Integer key = 1;

        try {
            cache.get(key, () -> {
                System.out.println("in load " + Thread.currentThread().getName());
                return "1";
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        cache.put(1, "2");

        String s = cache.get(1, () -> "1");
        System.out.println(s);
    }

    private static void testException() {
        Cache<Integer, String> cache = CacheBuilder.newBuilder().maximumSize(1000).build();

        Integer key = 1;

        try {
            String xxxx = cache.get(key, () -> {
                //Thread.sleep(2000);
                System.out.println("in load " + Thread.currentThread().getName());
                //throw new RuntimeException("xxxx"); // 有异常则直接抛到上层让外面捕获
                return null;
            });
            System.out.println(xxxx);// null也会报错
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 保留"获取缓存-如果没有-则计算"[get-if-absent-compute]的原子语义
    private static void testCallback() {
        Cache<Integer, String> cache = CacheBuilder.newBuilder().maximumSize(1000).build();

        Integer key = 1;
        new Thread(() -> {
            try {
                cache.get(key, () -> {
                    Thread.sleep(2000);// 暂停后，由于cache认为他没有返回所以下面的main还会执行,这个不对!!可以查看testLoadCount
                    System.out.println("in load1 " + Thread.currentThread().getName());
                    return "1".toString();
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        try {
            // If the key wasn't in the "easy to compute" group, we need to
            // do things the hard way.
            cache.get(key, () -> {
                Thread.sleep(2000);
                System.out.println("in load2 " + Thread.currentThread().getName());
                return "1".toString();
            });

            cache.get(key, () -> {
                Thread.sleep(2000);
                System.out.println("in load3 " + Thread.currentThread().getName());
                return "1".toString();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testCacheBuilder() {
        LoadingCache<Integer, String> graphs = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .build(new CacheLoader<Integer, String>() {
                    public String load(Integer key) throws Exception {
                        Thread.sleep(2000);
                        System.out.println("in load " + Thread.currentThread().getName());
                        return key.toString();
                    }
                });

        new Thread(() -> {
            try {
                System.out.println("in " + Thread.currentThread().getName() + " start 1");
                graphs.get(1);
                System.out.println("in thread end 1");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        try {
            System.out.println(graphs.get(1));
            System.out.println(graphs.get(1));// 第二次，同样key就不走load了
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testCacheLoaderThread() throws Exception {
        CacheLoader<Integer, String> cacheLoader = new CacheLoader<Integer, String>() {
            @Override
            public String load(Integer integer) throws Exception {
                Thread.sleep(2000);
                System.out.println("in load " + Thread.currentThread().getName());
                return integer.toString();
            }
        };

        // 异步线程不会有影响
        new Thread(() -> {
            try {
                System.out.println("in " + Thread.currentThread().getName() + " start 1");
                cacheLoader.load(1);// 这个很明显就是自己的方法实现。。怎么还会有其他线程。。。
                System.out.println("in thread end 1");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        System.out.println("in main " + Thread.currentThread().getName());
        // 阻塞获取,用的load方法内用的还是当前线程
        cacheLoader.load(1);
        System.out.println("get 1_1");
        cacheLoader.load(1);
        System.out.println("get 1_2");
        cacheLoader.load(2);
        System.out.println("get 2");
    }
}
