package com.wolf.test.vertx;

import io.vertx.core.Vertx;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Description:
 * Created on 2021/4/9 9:58 AM
 *
 * @author 李超
 * @version 0.0.1
 */
@ExtendWith(VertxExtension.class)
public class TestVertx {
    @BeforeEach
    void deploy_verticle(Vertx vertx, VertxTestContext testContext) {
        vertx.deployVerticle(new MainVerticle(), testContext.succeeding(id -> testContext.completeNow()));
    }

    // curl -v http://localhost:8888
    @Test
    void verticle_deployed(Vertx vertx, VertxTestContext testContext) throws Throwable {
        Thread.sleep(5000);
        testContext.completeNow();
    }
}
