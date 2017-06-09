
package com.wolf.utils.redis.command;

import com.wolf.utils.redis.Process;
import com.wolf.utils.redis.Process.Policy;

import java.util.List;
import java.util.Properties;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 下午1:27:15

 * @since 3.3
 */
public interface RedisServerCommands {

    @Process(Policy.WRITE)
    Boolean bgWriteAof();

    @Process(Policy.WRITE)
    Boolean bgSave();

    @Process(Policy.READ)
    Long lastSave();

    @Process(Policy.WRITE)
    Boolean save();

    @Process(Policy.READ)
    Long dbSize();

    @Process(Policy.WRITE)
    Boolean flushDb();

    @Process(Policy.WRITE)
    Boolean flushAll();

    @Process(Policy.READ)
    Properties info();

    @Process(Policy.WRITE)
    Boolean shutdown();

    @Process(Policy.READ)
    List<String> getConfig(String pattern);

    @Process(Policy.WRITE)
    Boolean setConfig(String param, String value);

    @Process(Policy.WRITE)
    Boolean resetConfigStats();

    @Process(Policy.WRITE)
    Long getDB();
    /**
     * 修改主从同步配置
     * 
     * <br/> Created on 2014-8-28 上午9:05:44

     * @since 3.4
     * @param host
     * @param port
     */
    void slaveof(final String host, final int port);
    /**
     * 禁止主从同步
     * 
     * <br/> Created on 2014-8-28 上午9:06:07

     * @since 3.4
     * @return
     */
    Boolean slaveofNoOne();
}
