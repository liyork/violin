/**
 * (C) 2011-2012 Alibaba Group Holding Limited.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 2 as published by the Free Software Foundation.
 *
 */
package com.wolf.utils.redis.command;


import com.wolf.utils.redis.Process;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 *
 * @author juxin.zj E-mail:juxin.zj@taobao.com
 * @since 2011-7-25 09:08:49
 * @version 1.0
 */
public interface RedisHashCommands {

    @Process(Process.Policy.WRITE)
    Boolean hSet(byte[] key, byte[] field, byte[] value);

    @Process(Process.Policy.WRITE)
    Boolean hSetNX(byte[] key, byte[] field, byte[] value);

    @Process(Process.Policy.READ)
    byte[] hGet(byte[] key, byte[] field);

    @Process(Process.Policy.READ)
    List<byte[]> hMGet(byte[] key, byte[]... fields);

    @Process(Process.Policy.WRITE)
    Boolean hMSet(byte[] key, Map<byte[], byte[]> hashes);

    @Process(Process.Policy.WRITE)
    Long hIncrBy(byte[] key, byte[] field, long delta);

    @Process(Process.Policy.READ)
    Boolean hExists(byte[] key, byte[] field);

    @Process(Process.Policy.WRITE)
    Long hDel(byte[] key, byte[]... field);

    @Process(Process.Policy.READ)
    Long hLen(byte[] key);

    @Process(Process.Policy.READ)
    Set<byte[]> hKeys(byte[] key);

    @Process(Process.Policy.READ)
    List<byte[]> hVals(byte[] key);

    @Process(Process.Policy.READ)
    Map<byte[], byte[]> hGetAll(byte[] key);

    @Process(Process.Policy.READ)
	List<Map<byte[], byte[]>> hGetAllKeys(byte[]... keys);

}
