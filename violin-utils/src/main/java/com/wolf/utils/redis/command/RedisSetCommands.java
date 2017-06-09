
package com.wolf.utils.redis.command;

import com.wolf.utils.redis.Process;
import com.wolf.utils.redis.Process.Policy;

import java.util.Set;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 下午1:27:41

 * @since 3.3
 */
public interface RedisSetCommands {

    @Process(Policy.WRITE)
    Long sAdd(byte[] key, byte[]... value);

    @Process(Policy.WRITE)
    Long sRem(byte[] key, byte[]... value);

    @Process(Policy.READ)
    byte[] sPop(byte[] key);

    @Process(Policy.WRITE)
    Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value);

    @Process(Policy.READ)
    Long sCard(byte[] key);

    @Process(Policy.READ)
    Boolean sIsMember(byte[] key, byte[] value);

    @Process(Policy.READ)
    Set<byte[]> sInter(byte[]... keys);

    @Process(Policy.WRITE)
    Long sInterStore(byte[] destKey, byte[]... keys);

    @Process(Policy.READ)
    Set<byte[]> sUnion(byte[]... keys);

    @Process(Policy.WRITE)
    Long sUnionStore(byte[] destKey, byte[]... keys);

    @Process(Policy.READ)
    Set<byte[]> sDiff(byte[]... keys);

    @Process(Policy.WRITE)
    Long sDiffStore(byte[] destKey, byte[]... keys);

    @Process(Policy.READ)
    Set<byte[]> sMembers(byte[] key);

    @Process(Policy.READ)
    byte[] sRandMember(byte[] key);
}
