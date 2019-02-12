package com.wolf.test.base.reference;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/02/06
 */
@FunctionalInterface
public interface CacheLoader<K, V> {

    V load(K k);
}
