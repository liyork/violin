package com.wolf.test.jdknewfuture.java8inaction;

import com.wolf.utils.TimeUtils;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Description:
 * 阻塞的方式显然和我们的异步编程的初衷相违背，轮询的方式又会耗费无谓的 CPU 资源，而且也不能及时地得到计算结果。
 *
 * @author 李超
 * @date 2019/08/01
 */
public class CompletableFutureTest {

    @Test
    public void testGetPriceAsync() throws Exception {

        Shop xx = new Shop("xx");
        long start = System.nanoTime();
        Future<Double> future = xx.getPriceAsync("new product");
        System.out.println("invoke time :" + (System.nanoTime() - start) / 1_000_000 + " ms");

        System.out.println("do other thing");
        TimeUtils.sleepSecond(1);

        future.get(2000, TimeUnit.MILLISECONDS);
        System.out.println("retrieval time :" + (System.nanoTime() - start) / 1_000_000 + " ms");
    }

    @Test
    public void testGetPriceAsyncErrorException() throws Exception {

        Shop xx = new Shop("xx");
        long start = System.nanoTime();
        Future<Double> future = xx.getPriceAsyncErrorException("new product");

        future.get(2000, TimeUnit.MILLISECONDS);
        System.out.println("retrieval time :" + (System.nanoTime() - start) / 1_000_000 + " ms");
    }

    @Test
    public void testGetPriceAsyncRightException() throws Exception {

        Shop xx = new Shop("xx");
        long start = System.nanoTime();
        Future<Double> future = xx.getPriceAsyncRightException("new product");

        future.get(2000, TimeUnit.MILLISECONDS);
        System.out.println("retrieval time :" + (System.nanoTime() - start) / 1_000_000 + " ms");
    }

    @Test
    public void testGetPriceAsyncSupplier() throws Exception {

        Shop xx = new Shop("xx");
        long start = System.nanoTime();
        Future<Double> future = xx.getPriceAsyncSupplier("new product");

        future.get(2000, TimeUnit.MILLISECONDS);
        System.out.println("retrieval time :" + (System.nanoTime() - start) / 1_000_000 + " ms");
    }

    //只能使用shop.getPrice的同步方法
    @Test
    public void testGetPriceWithAsync() throws Exception {

        long start = System.nanoTime();
        List<Shop> shops = Arrays.asList(new Shop("a"), new Shop("b"), new Shop("c"));
        findPrices1(shops, "apple");
        System.out.println("findPrices1 duration is " + (System.nanoTime() - start) / 1_000_000 + " ms");

        start = System.nanoTime();
        findPrices2(shops, "apple");
        System.out.println("findPrices2 duration is " + (System.nanoTime() - start) / 1_000_000 + " ms");

        start = System.nanoTime();
        findPrices3_1(shops, "apple");
        System.out.println("findPrices3_1 duration is " + (System.nanoTime() - start) / 1_000_000 + " ms");

        start = System.nanoTime();
        findPrices3_2(shops, "apple");
        System.out.println("findPrices3_2 duration is " + (System.nanoTime() - start) / 1_000_000 + " ms");

        ExecutorService executor = newExecutor();
        start = System.nanoTime();
        findPrices4(shops, "apple", executor);
        System.out.println("findPrices4 duration is " + (System.nanoTime() - start) / 1_000_000 + " ms");
        executor.shutdown();
        executor.awaitTermination(4000, TimeUnit.SECONDS);
    }

    private ExecutorService newExecutor() {
        return Executors.newFixedThreadPool(Math.min(3, 100),//根据公式得出最大100线程
                new ThreadFactory() {
                    private AtomicInteger count = new AtomicInteger();

                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r, "x-" + count.incrementAndGet() + "-t");
                        thread.setDaemon(true);
                        return thread;
                    }
                });
    }

    @Test
    public void testGetPriceDiscountWithAsync1() throws Exception {

        long start = System.nanoTime();
        List<Shop> shops = Arrays.asList(new Shop("a"), new Shop("b"), new Shop("c"));
        findPricesDiscount1(shops, "apple");
        System.out.println("findPricesDiscount1 duration is " + (System.nanoTime() - start) / 1_000_000 + " ms");
    }

    @Test
    public void testGetPriceDiscountWithAsync2() throws Exception {

        ExecutorService executor = newExecutor();
        long start = System.nanoTime();
        List<Shop> shops = Arrays.asList(new Shop("a"), new Shop("b"), new Shop("c"));
        findPricesDiscount2(shops, "apple", executor);
        System.out.println("findPricesDiscount2 duration is " + (System.nanoTime() - start) / 1_000_000 + " ms");
    }

    @Test
    public void testGetPriceDiscountWithAsync3() throws Exception {

        ExecutorService executor = newExecutor();
        long start = System.nanoTime();
        List<Shop> shops = Arrays.asList(new Shop("a"), new Shop("b"), new Shop("c"));
        findPricesDiscount3(shops, "apple", executor);
        System.out.println("findPricesDiscount3 duration is " + (System.nanoTime() - start) / 1_000_000 + " ms");
    }

    @Test
    public void testGetPriceStream() throws Exception {

        ExecutorService executor = newExecutor();
        long start = System.nanoTime();
        List<Shop> shops = Arrays.asList(new Shop("a"), new Shop("b"), new Shop("c"));

        CompletableFuture[] completableFutures = findPricesStream(shops, "apple", executor)
                //有结果就展示-相当于callback。使用当前线程执行，避免上下文切换开销，以及等待线程池进行调度的开销。或thenAcceptAsync
                .map(c -> c.thenAccept(price -> System.out.println(price + " done is " +
                        (System.nanoTime() - start) / 1_000_000 + " ms")))
                .toArray(CompletableFuture[]::new);
        //等待所有都执行完毕。或anyOf
        CompletableFuture.allOf(completableFutures).join();

        System.out.println("testGetPriceStream duration is " + (System.nanoTime() - start) / 1_000_000 + " ms");
    }

    //顺序执行,互相阻塞
    public List<String> findPrices1(List<Shop> shops, String product) {
        return shops.stream()
                .map(shop -> String.format(" %s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(Collectors.toList());
    }

    //并行流
    public List<String> findPrices2(List<Shop> shops, String product) {
        return shops.parallelStream()
                .map(shop -> String.format(" %s price is %.2f", shop.getName(), shop.getPrice(product)))
                .collect(Collectors.toList());

    }

    //顺序流的延迟特性，对一个元素执行一边完整的流水线再执行下一个
    public List<String> findPrices3_1(List<Shop> shops, String product) {
        return shops.stream()
                .peek(p -> System.out.println("after stream:" + p))
                .map(shop -> CompletableFuture.supplyAsync(() -> String.format(" %s price is %.2f", shop.getName(), shop.getPrice(product))))
                .peek(p -> System.out.println("after supplyAsync map:" + p))
                .map(CompletableFuture::join)
                .peek(p -> System.out.println("after join map:" + p))
                .collect(Collectors.toList());
    }

    public List<String> findPrices3_2(List<Shop> shops, String product) {
        return shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> String.format(" %s price is %.2f", shop.getName(), shop.getPrice(product))))
                .collect(Collectors.toList())
                .stream()//开启两个流的目的是最大化并行。
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    //可以自己定制线程池，比并行流强，
    public List<String> findPrices4(List<Shop> shops, String product, Executor executor) {

        return shops.stream()
                .peek(p -> System.out.println("after stream1:" + p))
                .map(shop ->
                        CompletableFuture.supplyAsync(() ->
                        {
                            System.out.println("thread name:" + Thread.currentThread().getName());
                            return String.format(" %s price is %.2f", shop.getName(), shop.getPrice(product));
                        }, executor))
                .peek(p -> System.out.println("after supplyAsync map:" + p))
                .collect(Collectors.toList())
                .stream()
                .peek(p -> System.out.println("after stream2:" + p))
                .map(c -> {
                    try {
                        return c.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                    return "";
                })
                .collect(Collectors.toList());
    }

    //串行版本，演示功能
    public List<String> findPricesDiscount1(List<Shop> shops, String product) {

        return shops.stream()
                .map(shop -> shop.getPriceDiscount(product))
                .map(Quote::parse)
                .map(Discount::applyDiscount)
                .collect(Collectors.toList());
    }

    //整个CompletableFuture所有代码执行都没有阻塞，这利于流水线操作
    //completablefuture承接执行
    public List<String> findPricesDiscount2(List<Shop> shops, String product, ExecutorService executor) {

        return shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> shop.getPriceDiscount(product), executor))
                .map(c -> c.thenApply(Quote::parse))//存在则串行执行,由于单纯计算并无太多延迟
                .map(c -> c.thenCompose(//承接，由于上个线程执行完毕也没有工作，那就直接用此线程，减少线程切换
                        quote -> CompletableFuture.supplyAsync(
                                () -> Discount.applyDiscount(quote), executor)))
                .collect(Collectors.toList())//收集completableFuture
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    //使用thenCombine组合，实现类似countdownlatch功能，两个CompletableFuture都执行完再执行fn
    public List<Double> findPricesDiscount3(List<Shop> shops, String product, ExecutorService executor) {

        return shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> shop.getPrice(product), executor))
                .map(c -> c.thenCombine(
                        CompletableFuture.supplyAsync(
                                () -> {
                                    System.out.println("get other sth..");
                                    return 1;
                                }, executor),
                        (price, rate) -> price * rate//前俩都执行完再执行fn
                ))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    //不再等待所有返回再显示，而是有返回价格流
    public Stream<CompletableFuture<String>> findPricesStream(List<Shop> shops, String product, ExecutorService executor) {

        return shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(
                        () -> shop.getPriceDiscount(product), executor))
                .map(c -> c.thenApply(Quote::parse))
                .map(c -> c.thenCompose(
                        quote -> CompletableFuture.supplyAsync(
                                () -> Discount.applyDiscount(quote), executor)));

    }
}
