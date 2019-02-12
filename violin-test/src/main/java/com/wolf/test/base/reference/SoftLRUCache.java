package com.wolf.test.base.reference;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/02/06
 */
public class SoftLRUCache<K, V> {

    private final LinkedList<K> keyList = new LinkedList<>();

    private final Map<K, SoftReference<V>> cache = new HashMap<>();

    private final int capacity;

    private final CacheLoader<K, V> cacheLoader;

    public SoftLRUCache(int capacity, CacheLoader<K, V> cacheLoader) {
        this.capacity = capacity;
        this.cacheLoader = cacheLoader;
    }

    public void put(K key, V value) {

        //删除最老数据
        if (keyList.size() >= capacity) {
            K eldestKey = keyList.removeFirst();
            cache.remove(eldestKey);
        }

        //保持新鲜
        keyList.remove(key);
        keyList.addLast(key);

        cache.put(key, new SoftReference<>(value));
    }

    public V get(K key) {

        V value;
        boolean success = keyList.remove(key);
        if (!success) {
            value = cacheLoader.load(key);
            put(key, value);
        } else {
            value = cache.get(key).get();
            keyList.addLast(key);
        }

        return value;
    }

    @Override
    public String toString() {
        return this.keyList.toString();
    }
}
