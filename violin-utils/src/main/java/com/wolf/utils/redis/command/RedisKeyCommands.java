
package com.wolf.utils.redis.command;

import com.wolf.utils.redis.Process;
import com.wolf.utils.redis.Process.Policy;
import com.wolf.utils.redis.SortParams;

import java.util.List;
import java.util.Set;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 上午11:52:50

 * @since 3.3
 */
public interface RedisKeyCommands {

    @Process(Policy.READ)
    Boolean exists(byte[] key);

    @Process(Policy.WRITE)
    Long del(byte[]... keys);

    @Process(Policy.READ)
    String type(byte[] key);

    @Process(Policy.READ)
    Set<byte[]> keys(byte[] pattern);

    @Process(Policy.READ)
    byte[] randomKey();

    @Process(Policy.WRITE)
    Boolean rename(byte[] oldName, byte[] newName);

    @Process(Policy.WRITE)
    Boolean renameNX(byte[] oldName, byte[] newName);

    @Process(Policy.WRITE)
    Boolean expire(byte[] key, long seconds);

    @Process(Policy.WRITE)
    Boolean expireAt(byte[] key, long unixTime);

    @Process(Policy.WRITE)
    Boolean persist(byte[] key);

    @Process(Policy.WRITE)
    Boolean move(byte[] key, int dbIndex);

    @Process(Policy.READ)
    Long ttl(byte[] key);

    @Process(Policy.READ)
    List<byte[]> sort(byte[] key, SortParams params);

    @Process(Policy.READ)
    Long sort(byte[] key, SortParams params, byte[] storeKey);
}
