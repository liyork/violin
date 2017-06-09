
package com.wolf.utils.redis.command;

import com.wolf.utils.redis.Process;
import com.wolf.utils.redis.Process.Policy;
import com.wolf.utils.redis.SafeEncoder;

import java.util.List;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 下午1:26:22

 * @since 3.3
 */
public interface RedisListCommands {

    public enum Position {
        BEFORE, AFTER;
        public final byte[] raw;

        private Position() {
            raw = SafeEncoder.encode(name());
        }
    }

    @Process(Policy.WRITE)
    Long rPush(byte[] key, byte[]... value);

    @Process(Policy.WRITE)
    Long lPush(byte[] key, byte[]... value);

    @Process(Policy.WRITE)
    Long rPushX(byte[] key, byte[] value);

    @Process(Policy.WRITE)
    Long lPushX(byte[] key, byte[] value);

    @Process(Policy.READ)
    Long lLen(byte[] key);

    @Process(Policy.READ)
    List<byte[]> lRange(byte[] key, long begin, long end);

    @Process(Policy.WRITE)
    Boolean lTrim(byte[] key, long begin, long end);

    @Process(Policy.READ)
    byte[] lIndex(byte[] key, long index);

    @Process(Policy.WRITE)
    Long lInsert(byte[] key, Position where, byte[] pivot, byte[] value);

    @Process(Policy.WRITE)
    Boolean lSet(byte[] key, long index, byte[] value);

    @Process(Policy.WRITE)
    Long lRem(byte[] key, long count, byte[] value);

    @Process(Policy.WRITE)
    byte[] lPop(byte[] key);

    @Process(Policy.WRITE)
    byte[] rPop(byte[] key);

    @Process(Policy.WRITE)
    List<byte[]> bLPop(int timeout, byte[]... keys);

    @Process(Policy.WRITE)
    List<byte[]> bRPop(int timeout, byte[]... keys);

    @Process(Policy.WRITE)
    byte[] rPopLPush(byte[] srcKey, byte[] dstKey);

    @Process(Policy.WRITE)
    byte[] bRPopLPush(int timeout, byte[] srcKey, byte[] dstKey);

}
