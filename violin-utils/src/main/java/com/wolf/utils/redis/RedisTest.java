package com.wolf.utils.redis;

import com.wolf.utils.redis.command.ValueCommands;

/**
 * Description:
 * <br/> Created on 2017/4/17 11:08
 *
 * @author 李超
 * @since 1.0.0
 */
public class RedisTest {

    public static void main(String[] args) {
        baseTest();

//        new StartRedisReadWrite().execute(null);
    }

    private static void baseTest() {
        WriteDBPercentConfig.init();
        ValueCommands clusterValueCommands = RedisFactory.getClusterValueCommands("violinGroup1");
//        clusterValueCommands.set(360006,"xx122",123);
        Object xxx = clusterValueCommands.get(360006, "xx122");
        System.out.println("xxx:"+xxx);
    }
}
