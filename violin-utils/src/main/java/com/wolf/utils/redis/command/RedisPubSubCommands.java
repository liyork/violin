
package com.wolf.utils.redis.command;

import com.wolf.utils.redis.MessageListener;
import com.wolf.utils.redis.Process;
import com.wolf.utils.redis.Process.Policy;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 下午1:26:51

 * @since 3.3
 */
public interface RedisPubSubCommands {

    @Process(Policy.READ)
    boolean isSubscribed();

    @Process(Policy.WRITE)
    Long publish(byte[] channel, byte[] message);

    @Process(Policy.WRITE)
    Boolean subscribe(MessageListener listener, byte[]... channels);

    @Process(Policy.WRITE)
    Boolean pSubscribe(MessageListener listener, byte[]... patterns);
}
