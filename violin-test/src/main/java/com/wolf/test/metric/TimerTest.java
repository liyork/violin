package com.wolf.test.metric;

import com.sun.net.httpserver.HttpServer;
import io.micrometer.core.instrument.Metrics;
import io.micrometer.prometheus.PrometheusConfig;
import io.micrometer.prometheus.PrometheusMeterRegistry;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Description:
 * Created on 2021/9/15 5:30 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class TimerTest {
    public static Random random = new Random();

    private static void request() throws InterruptedException {
        Thread.sleep(random.nextInt(1000));
    }

    public static void main(String[] args) throws Exception {
        PrometheusMeterRegistry prometheusRegistry = new PrometheusMeterRegistry(PrometheusConfig.DEFAULT);

        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
            server.createContext("/prometheus", httpExchange -> {
                String response = prometheusRegistry.scrape();
                httpExchange.sendResponseHeaders(200, response.getBytes().length);
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes());
                }
            });

            new Thread(server::start).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Metrics.addRegistry(prometheusRegistry);
        new Thread(() -> {
            while (true) {
                Metrics.timer("aaa", "tag1","tags2").record(() -> {
                    System.out.println("1111");
                });
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
