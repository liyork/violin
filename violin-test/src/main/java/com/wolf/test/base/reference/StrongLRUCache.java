package com.wolf.test.base.reference;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Description: LRU——least recently used，最近最少使用
 * LRU是数据冷热治理的一种思想，不经常使用的数据称为冷数据，经常使用的则称为热数据，对冷数据分配很少的资源或者提前释放，可以帮助我们节省
 * 更多内存。
 * <p>
 * put，数据不满则放入，满了则先删除最老数据(keySeq判定)，再放入
 * get，数据不存在则重新加载放入缓存队尾，若存在使用缓存值返回并清除之前的KeySeq
 *
 * @author 李超
 * @date 2019/02/06
 */
public class StrongLRUCache<K, V> {

    private final LinkedList<K> keySeq = new LinkedList<>();

    private final Map<K, V> cache = new HashMap<>();

    //最大容量能存放数
    private final int capacity;

    private final CacheLoader<K, V> cacheLoader;

    public StrongLRUCache(int capacity, CacheLoader<K, V> cacheLoader) {

        this.capacity = capacity;
        this.cacheLoader = cacheLoader;
    }

    public void put(K key, V value) {

        //删除最老数据
        if (keySeq.size() >= capacity) {
            K eldestKey = keySeq.removeFirst();
            cache.remove(eldestKey);
        }

        //保持新鲜
        keySeq.remove(key);
        keySeq.addLast(key);

        cache.put(key, value);
    }

    public V get(K key) {

        V value;
        boolean success = keySeq.remove(key);
        if (!success) {
            value = cacheLoader.load(key);
            put(key, value);
        } else {
            value = cache.get(key);
            keySeq.addLast(key);
        }

        return value;
    }

    @Override
    public String toString() {
        return this.keySeq.toString();
    }
}
