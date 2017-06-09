
package com.wolf.utils.redis.command;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * hash 操作
 *  
 * <br/> Created on 2014-7-3 下午1:47:10

 * @since 3.3
 */
public interface HashCommands extends Commands {

    /**
     * 删除<tt>key</tt>的Hash对象的<tt>hashKey</tt>的数据
     *
     * @param namespace
     * @param key
     * @param hashKey
     * @return ,是否成功
     */
    @Deprecated
    <H, HK, HV> boolean delete(int namespace, H key, Object... hashKey);

    /**
     * 删除<tt>key</tt>的Hash对象的<tt>hashKey</tt>的数据
     *
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @param hashKey
     * @return ,是否成功
     */
    @Deprecated
    <H, HK, HV> boolean delete(int namespace, H key, boolean useNewKeySerialize, Object... hashKey);

    /**
     * 删除<tt>key</tt>的Hash对象的<tt>hashKey</tt>的数据
     *
     * @param namespace
     * @param key
     * @param hashKey
     * @return ,是否成功
     */
    <H, HK, HV> boolean ndelete(int namespace, H key, Object... hashKey);

    /**
     * 删除<tt>key</tt>的Hash对象的<tt>hashKey</tt>的数据
     *
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @param hashKey
     * @return ,是否成功
     */
    <H, HK, HV> boolean ndelete(boolean useNewKeySerialize, int namespace, H key, Object... hashKey);

    /**
     * 判断<tt>key</tt>的Hash对象的<tt>hashKey</tt>是否存在
     *
     * @param namespace
     * @param key
     * @param hashKey
     * @return 存在返回<tt>true</tt>，不存在返回<tt>false</tt>
     */
    <H, HK, HV> Boolean hasKey(int namespace, H key, Object hashKey);
    
    /**
     * 判断<tt>key</tt>的Hash对象的<tt>hashKey</tt>是否存在
     *
     * @param namespace
     * @param key
     * @param hashKey
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return 存在返回<tt>true</tt>，不存在返回<tt>false</tt>
     */
    <H, HK, HV> Boolean hasKey(int namespace, H key, Object hashKey, boolean useNewKeySerialize);

    /**
     * 取得<tt>key</tt>的Hash对象的<tt>hashKey</tt>的数据
     *
     * @param namespace
     * @param key
     * @param hashKey
     * @return
     */
    <H, HK, HV> HV get(int namespace, H key, Object hashKey);
    
    /**
     * 取得<tt>key</tt>的Hash对象的<tt>hashKey</tt>的数据
     *
     * @param namespace
     * @param key
     * @param hashKey
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <H, HK, HV> HV get(int namespace, H key, Object hashKey, boolean useNewKeySerialize);

    /**
     * 批量取得数据
     *
     * @param namespace
     * @param key
     * @param hashKeys
     * @return
     */
    <H, HK, HV> Collection<HV> multiGet(int namespace, H key, Collection<HK> hashKeys);
    
    /**
     * 批量取得数据
     *
     * @param namespace
     * @param key
     * @param hashKeys
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <H, HK, HV> Collection<HV> multiGet(int namespace, H key, Collection<HK> hashKeys, boolean useNewKeySerialize);

    /**
     * 计数操作方法 <tt>delta</tt>可为正、负、0值。正值为增加计数、负值为减少计数、0为取得当前值
     * 此方法为只是hash的计数方法，不能操作由put方法维护的value
     * @param namespace
     * @param key
     * @param hashKey
     * @param delta
     * @return
     */
    <H, HK, HV> Long increment(int namespace, H key, HK hashKey, long delta);
    
    /**
     * 计数操作方法 <tt>delta</tt>可为正、负、0值。正值为增加计数、负值为减少计数、0为取得当前值
     * 此方法为只是hash的计数方法，不能操作由put方法维护的value
     * @param namespace
     * @param key
     * @param hashKey
     * @param delta
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <H, HK, HV> Long increment(int namespace, H key, HK hashKey, long delta, boolean useNewKeySerialize);

    /**
     * 得到<tt>key</tt>的Hash对象的所有key
     *
     * @param namespace
     * @param key
     * @return
     */
    <H, HK, HV> Set<HK> keys(int namespace, H key);
    
    /**
     * 得到<tt>key</tt>的Hash对象的所有key
     *
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <H, HK, HV> Set<HK> keys(int namespace, H key, boolean useNewKeySerialize);
    
    /**
     * 得到<tt>key</tt>的Hash对象的所有key
     *
     * @param namespace
     * @param key
     * @return
     */
    <H, HK, HV> Set<HK> hKeys(int namespace, H key);
    
    /**
     * 得到<tt>key</tt>的Hash对象的所有key
     *
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <H, HK, HV> Set<HK> hKeys(int namespace, H key, boolean useNewKeySerialize);

    /**
     * 缺的<tt>key</tt>的Hash对象的大小
     *
     * @param namespace
     * @param key
     * @return
     */
    <H, HK, HV> Long size(int namespace, H key);
    
    /**
     * 缺的<tt>key</tt>的Hash对象的大小
     *
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <H, HK, HV> Long size(int namespace, H key, boolean useNewKeySerialize);

    /**
     * 批量设置数据
     *
     * @param namespace
     * @param key
     * @param m
     */
    <H, HK, HV> void putAll(int namespace, H key, Map<? extends HK, ? extends HV> m);
    
    /**
     * 批量设置数据
     *
     * @param namespace
     * @param key
     * @param m
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     */
    <H, HK, HV> void putAll(int namespace, H key, Map<? extends HK, ? extends HV> m, boolean useNewKeySerialize);

    /**
     * 设置数据，如果数据存在，则覆盖，如果数据不存在，则新增
     *
     * @param namespace
     * @param key
     * @param hashKey
     * @param value
     */
    <H, HK, HV> void put(int namespace, H key, HK hashKey, HV value);
    
    /**
     * 设置数据，如果数据存在，则覆盖，如果数据不存在，则新增
     *
     * @param namespace
     * @param key
     * @param hashKey
     * @param value
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     */
    <H, HK, HV> void put(int namespace, H key, HK hashKey, HV value, boolean useNewKeySerialize);

    /**
     * 设置数据，只有数据不存在才能设置成功
     *
     * @param namespace
     * @param key
     * @param hashKey
     * @param value
     * @return
     */
    <H, HK, HV> Boolean putIfAbsent(int namespace, H key, HK hashKey, HV value);
    
    /**
     * 设置数据，只有数据不存在才能设置成功
     *
     * @param namespace
     * @param key
     * @param hashKey
     * @param value
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <H, HK, HV> Boolean putIfAbsent(int namespace, H key, HK hashKey, HV value, boolean useNewKeySerialize);

    /**
     * 取得<tt>key</tt>的Hash对象的所有值
     *
     * @param namespace
     * @param key
     * @return
     */
    <H, HK, HV> Collection<HV> values(int namespace, H key);
    
    /**
     * 取得<tt>key</tt>的Hash对象的所有值
     *
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <H, HK, HV> Collection<HV> values(int namespace, H key, boolean useNewKeySerialize);

    /**
     * 缺的<tt>key</tt>的Hash对象的所有key、value
     *
     * @param namespace
     * @param key
     * @return
     */
    <H, HK, HV> Map<HK, HV> entries(int namespace, H key);
    
    /**
     * 缺的<tt>key</tt>的Hash对象的所有key、value
     *
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <H, HK, HV> Map<HK, HV> entries(int namespace, H key, boolean useNewKeySerialize);
    
    
    /**
     * 批量获取Hash对象的所有key、value
     *
     * @param namespace
     * @param keys
     * @return
     */
    <H, HK, HV> List<Map<HK, HV>> multiEntries(int namespace, Collection<H> keys);
    
    /**
     * 批量获取Hash对象的所有key、value
     *
     * @param namespace
     * @param keys
     * @param useNewKeySerialize(是否使用新的key序列化方式)
     * @return
     */
    <H, HK, HV> List<Map<HK, HV>> multiEntries(int namespace, Collection<H> keys, boolean useNewKeySerialize, boolean isThread);

}
