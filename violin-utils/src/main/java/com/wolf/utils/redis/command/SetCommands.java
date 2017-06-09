
package com.wolf.utils.redis.command;

import com.wolf.utils.redis.command.Commands;

import java.util.Collection;
import java.util.Set;

/**
 * Set 操作
 *  
 * <br/> Created on 2014-7-3 下午1:48:25

 * @since 3.3
 */
public interface SetCommands extends Commands {

    /**
     * 取得<tt>key</tt>的Set中不存在于<tt>otherKey</tt>的Set的数据<br />
     * 例如： <code>
     * key1 = {a,b,c,d}
     * key2 = {c,d}
     * difference(key1, key2) = {a,b}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKey
     * @return
     */
    <K, V> Set<V> difference(int namespace, K key, K otherKey);
    
    /**
     * 取得<tt>key</tt>的Set中不存在于<tt>otherKey</tt>的Set的数据<br />
     * 例如： <code>
     * key1 = {a,b,c,d}
     * key2 = {c,d}
     * difference(key1, key2) = {a,b}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKey
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <K, V> Set<V> difference(int namespace, K key, K otherKey, boolean useNewKeySerialize);

    /**
     * 取得<tt>key</tt>的Set中不存在于<tt>otherKeys</tt>的Set的数据<br />
     * 例如： <code>
     * key1 = {a,b,c,d}
     * key2 = {c}
     * key3 = {a,d}
     * otherKeys = {key2,key3}
     * difference(key1, otherKeys) = {b,d}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKeys
     * @return
     */
    <K, V> Set<V> difference(int namespace, K key, Collection<K> otherKeys);
    
    /**
     * 取得<tt>key</tt>的Set中不存在于<tt>otherKeys</tt>的Set的数据<br />
     * 例如： <code>
     * key1 = {a,b,c,d}
     * key2 = {c}
     * key3 = {a,d}
     * otherKeys = {key2,key3}
     * difference(key1, otherKeys) = {b,d}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKeys
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <K, V> Set<V> difference(int namespace, K key, Collection<K> otherKeys, boolean useNewKeySerialize);

    /**
     * 取得<tt>key</tt>的Set中不存在于<tt>otherKey</tt>的Set的数据，将数据存到<tt>destKey</tt> 例如：
     * <code>
     * key1 = {a,b,c,d}
     * key2 = {a,c}
     * destKey = differenceAndStore(key1, key2)
     * destKey = {b,d}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKey
     * @param destKey
     */
    <K, V> void differenceAndStore(int namespace, K key, K otherKey, K destKey);
    
    /**
     * 取得<tt>key</tt>的Set中不存在于<tt>otherKey</tt>的Set的数据，将数据存到<tt>destKey</tt> 例如：
     * <code>
     * key1 = {a,b,c,d}
     * key2 = {a,c}
     * destKey = differenceAndStore(key1, key2)
     * destKey = {b,d}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKey
     * @param destKey
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     */
    <K, V> void differenceAndStore(int namespace, K key, K otherKey, K destKey, boolean useNewKeySerialize);

    /**
     * 取得<tt>key</tt>的Set中不存在于<tt>otherKeys</tt>的Set的数据，将数据存到<tt>destKey</tt>
     * 例如： <code>
     * key1 = {a,b,c,d}
     * key2 = {a}
     * key3 = {a,c}
     * otherKeys = {key2,key3}
     * destKey = differenceAndStore(key1, otherKeys)
     * destKey = {b,d}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKeys
     * @param destKey
     */
    <K, V> void differenceAndStore(int namespace, K key, Collection<K> otherKeys, K destKey);
    
    /**
     * 取得<tt>key</tt>的Set中不存在于<tt>otherKeys</tt>的Set的数据，将数据存到<tt>destKey</tt>
     * 例如： <code>
     * key1 = {a,b,c,d}
     * key2 = {a}
     * key3 = {a,c}
     * otherKeys = {key2,key3}
     * destKey = differenceAndStore(key1, otherKeys)
     * destKey = {b,d}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKeys
     * @param destKey
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     */
    <K, V> void differenceAndStore(int namespace, K key, Collection<K> otherKeys, K destKey, boolean useNewKeySerialize);

    /**
     * 取得<tt>key</tt>的Set中<tt>otherKey</tt>的Set中交集的数据 例如： <code>
     * key1 = {a,b,c,d}
     * key2 = {a,c}
     * intersect(key1, key2) = {a,c}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKey
     * @return
     */
    <K, V> Set<V> intersect(int namespace, K key, K otherKey);
    
    /**
     * 取得<tt>key</tt>的Set中<tt>otherKey</tt>的Set中交集的数据 例如： <code>
     * key1 = {a,b,c,d}
     * key2 = {a,c}
     * intersect(key1, key2) = {a,c}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKey
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <K, V> Set<V> intersect(int namespace, K key, K otherKey, boolean useNewKeySerialize);

    /**
     * 取得<tt>key</tt>的Set中<tt>otherKeys</tt>的Set中交集的数据 例如： <code>
     * key1 = {a,b,c,d}
     * key2 = {c}
     * key3 = {a,c,e}
     * otherKeys = {key2,key3}
     * intersect(key1, otherKeys) = {c}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKeys
     * @return
     */
    <K, V> Set<V> intersect(int namespace, K key, Collection<K> otherKeys);
    
    /**
     * 取得<tt>key</tt>的Set中<tt>otherKeys</tt>的Set中交集的数据 例如： <code>
     * key1 = {a,b,c,d}
     * key2 = {c}
     * key3 = {a,c,e}
     * otherKeys = {key2,key3}
     * intersect(key1, otherKeys) = {c}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKeys
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <K, V> Set<V> intersect(int namespace, K key, Collection<K> otherKeys, boolean useNewKeySerialize);

    /**
     * 取得<tt>key</tt>的Set中<tt>otherKey</tt>的Set中交集的数据，将数据放入<tt>destKey</tt> 例如：
     * <code>
     * key1 = {a,b,c,d}
     * key2 = {a,c}
     * intersectAndStore(key1, key2, destKey)
     * destKey = {a,c}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKey
     * @param destKey
     */
    <K, V> void intersectAndStore(int namespace, K key, K otherKey, K destKey);
    
    /**
     * 取得<tt>key</tt>的Set中<tt>otherKey</tt>的Set中交集的数据，将数据放入<tt>destKey</tt> 例如：
     * <code>
     * key1 = {a,b,c,d}
     * key2 = {a,c}
     * intersectAndStore(key1, key2, destKey)
     * destKey = {a,c}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKey
     * @param destKey
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     */
    <K, V> void intersectAndStore(int namespace, K key, K otherKey, K destKey, boolean useNewKeySerialize);

    /**
     * 取得<tt>key</tt>的Set中<tt>otherKeys</tt>的Set中交集数据，将数据放入<tt>destKey</tt> 例如：
     * <code>
     * key1 = {a,b,c,d}
     * key2 = {c}
     * key3 = {a,c,e}
     * otherKeys = {key2,key3}
     * intersectAndStore(key1, otherKeys, destKey)
     * destKey = {c}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKeys
     * @param destKey
     */
    <K, V> void intersectAndStore(int namespace, K key, Collection<K> otherKeys, K destKey);
    
    /**
     * 取得<tt>key</tt>的Set中<tt>otherKeys</tt>的Set中交集数据，将数据放入<tt>destKey</tt> 例如：
     * <code>
     * key1 = {a,b,c,d}
     * key2 = {c}
     * key3 = {a,c,e}
     * otherKeys = {key2,key3}
     * intersectAndStore(key1, otherKeys, destKey)
     * destKey = {c}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKeys
     * @param destKey
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     */
    <K, V> void intersectAndStore(int namespace, K key, Collection<K> otherKeys, K destKey, boolean useNewKeySerialize);

    /**
     * 取得<tt>key</tt>的Set和<tt>otherKey</tt>的Set的并集数据 例如： <code>
     * key1 = {a,b,c}
     * key2 = {c,d}
     * union(key1, key2) = {a,b,c,d}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKey
     * @return
     */
    <K, V> Set<V> union(int namespace, K key, K otherKey);
    
    /**
     * 取得<tt>key</tt>的Set和<tt>otherKey</tt>的Set的并集数据 例如： <code>
     * key1 = {a,b,c}
     * key2 = {c,d}
     * union(key1, key2) = {a,b,c,d}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKey
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <K, V> Set<V> union(int namespace, K key, K otherKey, boolean useNewKeySerialize);

    /**
     * 取得<tt>key</tt>的Set和<tt>otherKeys</tt>的Set的并集数据 例如： <code>
     * key1 = {a,b,c}
     * key2 = {c,d}
     * key3 = {a,c,e}
     * otherKeys = {key2,key3}
     * union(key1, otherKeys) = {a,b,c,d,e}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKeys
     * @return
     */
    <K, V> Set<V> union(int namespace, K key, Collection<K> otherKeys);
    
    /**
     * 取得<tt>key</tt>的Set和<tt>otherKeys</tt>的Set的并集数据 例如： <code>
     * key1 = {a,b,c}
     * key2 = {c,d}
     * key3 = {a,c,e}
     * otherKeys = {key2,key3}
     * union(key1, otherKeys) = {a,b,c,d,e}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKeys
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <K, V> Set<V> union(int namespace, K key, Collection<K> otherKeys, boolean useNewKeySerialize);

    /**
     * 取得<tt>key</tt>的Set和<tt>otherKey</tt>的Set的并集数据，将数据存入<tt>destKey</tt> 例如：
     * <code>
     * key1 = {a,b,c}
     * key2 = {c,d}
     * unionAndStore(key1, key2, destKey)
     * destKey = {a,b,c,d}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKey
     * @param destKey
     */
    <K, V> void unionAndStore(int namespace, K key, K otherKey, K destKey);
    
    /**
     * 取得<tt>key</tt>的Set和<tt>otherKey</tt>的Set的并集数据，将数据存入<tt>destKey</tt> 例如：
     * <code>
     * key1 = {a,b,c}
     * key2 = {c,d}
     * unionAndStore(key1, key2, destKey)
     * destKey = {a,b,c,d}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKey
     * @param destKey
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     */
    <K, V> void unionAndStore(int namespace, K key, K otherKey, K destKey, boolean useNewKeySerialize);

    /**
     * 取得<tt>key</tt>的Set和<tt>otherKeys</tt>的Set的并集数据，将数据存入<tt>destKey</tt> 例如：
     * <code>
     * key1 = {a,b,c}
     * key2 = {c,d}
     * key3 = {a,c,e}
     * otherKeys = {key2,key3}
     * union(key1, otherKeys, destKey)
     * destKey = {a,b,c,d,e}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKeys
     * @param destKey
     */
    <K, V> void unionAndStore(int namespace, K key, Collection<K> otherKeys, K destKey);
    
    /**
     * 取得<tt>key</tt>的Set和<tt>otherKeys</tt>的Set的并集数据，将数据存入<tt>destKey</tt> 例如：
     * <code>
     * key1 = {a,b,c}
     * key2 = {c,d}
     * key3 = {a,c,e}
     * otherKeys = {key2,key3}
     * union(key1, otherKeys, destKey)
     * destKey = {a,b,c,d,e}
     * </code>
     *
     * @param namespace
     * @param key
     * @param otherKeys
     * @param destKey
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     */
    <K, V> void unionAndStore(int namespace, K key, Collection<K> otherKeys, K destKey, boolean useNewKeySerialize);

    /**
     * 向<tt>key</tt>的Set增加一个数据
     *
     * @param namespace
     * @param key
     * @param value
     * @return
     */
    <K, V> Long add(int namespace, K key, V... value);
    
    /**
     * 向<tt>key</tt>的Set增加一个数据
     *
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @param value
     * @return
     */
    <K, V> Long add(boolean useNewKeySerialize, int namespace, K key, V... value);

    /**
     * 判断<tt>o</tt>是否为<tt>key</tt>的Set中的数据
     *
     * @param namespace
     * @param key
     * @param o
     * @return
     */
    <K, V> Boolean isMember(int namespace, K key, Object o);
    
    /**
     * 判断<tt>o</tt>是否为<tt>key</tt>的Set中的数据
     *
     * @param namespace
     * @param key
     * @param o
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <K, V> Boolean isMember(int namespace, K key, Object o, boolean useNewKeySerialize);

    /**
     * 取得所有成员
     *
     * @param namespace
     * @param key
     * @return
     */
    <K, V> Set<V> members(int namespace, K key);
    
    /**
     * 取得所有成员
     *
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <K, V> Set<V> members(int namespace, K key, boolean useNewKeySerialize);

    /**
     * 移动<tt>key</tt>的Set中的<tt>value</tt>到<tt>destKey</tt>
     *
     * @param namespace
     * @param key
     * @param value
     * @param destKey
     * @return
     */
    <K, V> Boolean move(int namespace, K key, V value, K destKey);
    
    /**
     * 移动<tt>key</tt>的Set中的<tt>value</tt>到<tt>destKey</tt>
     *
     * @param namespace
     * @param key
     * @param value
     * @param destKey
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <K, V> Boolean move(int namespace, K key, V value, K destKey, boolean useNewKeySerialize);

    /**
     * 随机取得一个数据
     *
     * @param namespace
     * @param key
     * @return
     */
    <K, V> V randomMember(int namespace, K key);
    
    /**
     * 随机取得一个数据
     *
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <K, V> V randomMember(int namespace, K key, boolean useNewKeySerialize);

    /**
     * 删除一个或者多个数据
     * @param namespace
     * @param key
     * @param values
     * @param <K>
     * @param <V>
     * @return
     */
    <K, V> Long nremove(int namespace, K key, Object... values);

    /**
     * 删除一个或者多个数据，并且可以指定key的序列化方式
     * @param namespace
     * @param key
     * @param useNewKeySerialize
     * @param values
     * @param <K>
     * @param <V>
     * @return
     */
    <K, V> Long nremove(boolean useNewKeySerialize, int namespace, K key, Object... values);

    /**
     * 删除一个数据
     *
     * @param namespace
     * @param key
     * @param o
     * @return
     */
    @Deprecated
    <K, V> Long remove(int namespace, K key, Object... o);
    
    /**
     * 删除一个数据
     *
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @param o
     * @return
     */
    @Deprecated
    <K, V> Long remove(int namespace, K key, boolean useNewKeySerialize, Object... o);

    /**
     * 删除并取出第一个数据
     *
     * @param namespace
     * @param key
     * @return
     */
    <K, V> V pop(int namespace, K key);
    
    /**
     * 删除并取出第一个数据
     *
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <K, V> V pop(int namespace, K key, boolean useNewKeySerialize);

    /**
     * 取得Set大小
     *
     * @param namespace
     * @param key
     * @return
     */
    <K, V> Long size(int namespace, K key);
    
    /**
     * 取得Set大小
     *
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <K, V> Long size(int namespace, K key, boolean useNewKeySerialize);

}
