
package com.wolf.utils.redis.command;

import com.wolf.utils.redis.Process;
import com.wolf.utils.redis.Process.Policy;

import java.util.List;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 下午1:28:02

 * @since 3.3
 */
public interface RedisTransactionCommands {

    @Process(Policy.WRITE)
    Boolean multi();

    @Process(Policy.WRITE)
    List<Object> exec();

    @Process(Policy.WRITE)
    Boolean discard();

    @Process(Policy.WRITE)
    Boolean watch(byte[]... keys);

    @Process(Policy.WRITE)
    Boolean unwatch();
}
