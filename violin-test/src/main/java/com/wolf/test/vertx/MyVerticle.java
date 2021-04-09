package com.wolf.test.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;

/**
 * Description: 异步启动
 * Created on 2021/4/9 10:21 AM
 *
 * @author 李超
 * @version 0.0.1
 */
public class MyVerticle extends AbstractVerticle {

    private HttpServer server;

    public void start(Promise<Void> startPromise) {
        server = vertx.createHttpServer().requestHandler(req -> {
            req.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from Vert.x!");
        });

        // Now bind the server:
        server.listen(8080, res -> {
            if (res.succeeded()) {
                startPromise.complete();
            } else {
                startPromise.fail(res.cause());
            }
        });
    }

    public void stop(Promise<Void> stopPromise) {
        //obj.doSomethingThatTakesTime(res -> {
        //    if (res.succeeded()) {
        //        stopPromise.complete();
        //    } else {
        //        stopPromise.fail();
        //    }
        //});
    }
}

