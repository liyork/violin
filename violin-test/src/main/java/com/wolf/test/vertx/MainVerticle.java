package com.wolf.test.vertx;

import io.vertx.core.*;

/**
 * Description:
 * Created on 2021/4/9 9:25 AM
 *
 * @author 李超
 * @version 0.0.1
 */
public class MainVerticle extends AbstractVerticle {

    @Override
    public void start(Promise<Void> startPromise) throws Exception {
        vertx.createHttpServer().requestHandler(req -> {
            req.response()
                    .putHeader("content-type", "text/plain")
                    .end("Hello from Vert.x!");
        }).listen(8888, http -> {
            if (http.succeeded()) {
                startPromise.complete();
                System.out.println("HTTP server started on port 8888");
            } else {
                startPromise.fail(http.cause());
            }
        });
    }

    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        //Vertx vertx = Vertx.vertx(new VertxOptions().setWorkerPoolSize(40));

        vertx.setPeriodic(1000, id -> {
            // This handler will get called every second
            System.out.println("timer fired!");
        });

        // deploy
        Verticle myVerticle = new MyVerticle();
        vertx.deployVerticle(myVerticle);

        // undeploy
        String deploymentID = null;
        vertx.undeploy(deploymentID, res -> {
            if (res.succeeded()) {
                System.out.println("Undeployed ok");
            } else {
                System.out.println("Undeploy failed!");
            }
        });
    }
}
