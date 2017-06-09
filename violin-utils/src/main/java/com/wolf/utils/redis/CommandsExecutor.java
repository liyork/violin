package com.wolf.utils.redis;

import java.util.List;

/**
 * 
 * Description: 命令执行器
 * All Rights Reserved.
 * @param <K>
 * @param <V>
 * Created on 2016-5-27 下午4:16:55
 * @author
 */
public interface CommandsExecutor<K,V> {
	//批量执行命令
	List<V> multiExecute(int namespace, List<K> partKeys, final byte[][] rawKeys);
	//单一执行命令
	V singleExecute(int namespace, K key, boolean useNewKeySerialize);
}
