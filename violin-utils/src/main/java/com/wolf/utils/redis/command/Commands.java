
package com.wolf.utils.redis.command;


import com.wolf.utils.redis.DataType;
import com.wolf.utils.redis.SortParams;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 *  * 基本操作入口，包括：
 * <ul>
 *  <li>基本的基于key的操作</li>
 *  <li>取得其他各数据结构操作入口</li>
 *  <li>排序</li>
 *  <li>取得当前序列化方式</li>
 * </ul>
 *  
 * <br/> Created on 2014-7-3 下午1:50:43

 * @since 3.3
 */
public interface Commands {

    /**
     * <tt>key</tt>是否存在
     * @param namespace
     * @param key
     * @return
     */
    <K> Boolean hasKey(int namespace, K key);
    
    /**
     * <tt>key</tt>是否存在
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K> Boolean hasKey(int namespace, K key, boolean useNewKeySerialize);

    /**
     * 删除指定<tt>key</tt>
     * @param namespace
     * @param key
     */
    <K> void delete(int namespace, K key);
    
    /**
     * 删除指定<tt>key</tt>
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     */
    <K> void delete(int namespace, K key, boolean useNewKeySerialize);

    /**
     * 批量删除<tt>key</tt>
     * @param namespace
     * @param keys
     */
    <K> void delete(int namespace, Collection<K> keys);

    /**
     * 取得<tt>key</tt>的类型
     * @param namespace
     * @param key
     * @return
     */
    <K> DataType type(int namespace, K key);
    
    /**
     * 取得<tt>key</tt>的类型
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K> DataType type(int namespace, K key, boolean useNewKeySerialize);

    /**
     * 取得<tt>pattern</tt>匹配的<tt>key</tt>集合
     * @param namespace
     * @param pattern
     * @return
     */
    @Deprecated
    <K> Set<K> keys(int namespace, String pattern);
    
    /**
     * 取得<tt>pattern</tt>匹配的<tt>key</tt>集合
     * @param namespace
     * @param pattern
     * @param useNewKeySerialize
     * @return
     */
    @Deprecated
    <K> Set<K> keysByNew(int namespace, String pattern, boolean useNewKeySerialize);

    /**
     * 重命名<tt>key</tt>
     * @param namespace
     * @param oldKey
     * @param newKey
     */
    <K> void rename(int namespace, K oldKey, K newKey);
    
    /**
     * 重命名<tt>key</tt>
     * @param namespace
     * @param oldKey
     * @param newKey
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     */
    <K> void rename(int namespace, K oldKey, K newKey, boolean useNewKeySerialize);

    /**
     * 如果<tt>oldKey</tt>存在则重命名<tt>oldKey</tt>为<tt>newKey</tt>
     * @param namespace
     * @param oldKey
     * @param newKey
     * @return
     */
    <K> Boolean renameIfAbsent(int namespace, K oldKey, K newKey);
    
    /**
     * 如果<tt>oldKey</tt>存在则重命名<tt>oldKey</tt>为<tt>newKey</tt>
     * @param namespace
     * @param oldKey
     * @param newKey
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K> Boolean renameIfAbsent(int namespace, K oldKey, K newKey, boolean useNewKeySerialize);

    /**
     * 设置<tt>key</tt>的过期时间
     * @param namespace
     * @param key
     * @param timeout
     * @param unit
     * @return
     */
    <K> Boolean expire(int namespace, K key, long timeout, TimeUnit unit);
    
    /**
     * 设置<tt>key</tt>的过期时间
     * @param namespace
     * @param key
     * @param timeout
     * @param unit
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K> Boolean expire(int namespace, K key, long timeout, TimeUnit unit, boolean useNewKeySerialize);

    /**设置<tt>key</tt>的过期时间
     * @param namespace
     * @param key
     * @param date
     * @return
     */
    <K> Boolean expireAt(int namespace, K key, Date date);
    
    /**设置<tt>key</tt>的过期时间
     * @param namespace
     * @param key
     * @param date
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K> Boolean expireAt(int namespace, K key, Date date, boolean useNewKeySerialize);

    /**
     * 取消<tt>key</tt>的过期设置
     * @param namespace
     * @param key
     * @return
     */
    <K> Boolean persist(int namespace, K key);
    
    /**
     * 取消<tt>key</tt>的过期设置
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K> Boolean persist(int namespace, K key, boolean useNewKeySerialize);

    /**
     * 取得<tt>key</tt>的过期时间
     * @param namespace
     * @param key
     * @return
     */
    <K> Long getExpire(int namespace, K key);
    
    /**
     * 取得<tt>key</tt>的过期时间
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K> Long getExpire(int namespace, K key, boolean useNewKeySerialize);

    /**
     * 排序
     * @param namespace
     * @param query
     * @return
     */
    <K, V> List<V> sort(int namespace, K key, SortParams params);
    
    /**
     * 排序
     * @param namespace
     * @param query
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K, V> List<V> sort(int namespace, K key, SortParams params, boolean useNewKeySerialize);

    /**
     * 排序
     * @param namespace
     * @param query
     * @param storeKey
     * @return
     */
    <K> Long sort(int namespace, K key, SortParams query, K storeKey);
    
    /**
     * 排序
     * @param namespace
     * @param query
     * @param storeKey
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K> Long sort(int namespace, K key, SortParams query, K storeKey, boolean useNewKeySerialize);

}
