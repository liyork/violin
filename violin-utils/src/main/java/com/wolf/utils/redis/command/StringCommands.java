
package com.wolf.utils.redis.command;

import com.wolf.utils.redis.command.Commands;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * String 操作
 *  
 * <br/> Created on 2014-7-3 下午1:49:02

 * @since 3.3
 */
public interface StringCommands extends Commands {

    /**
     * 在为<tt>key</tt>的String追加<tt>value</tt>组成新的字符串，并返回组装后的字符串长度
     * 
     * @param namespace
     * @param key
     * @param value
     * @return
     */
    Long append(int namespace, String key, String value);
    
    /**
     * 在为<tt>key</tt>的String追加<tt>value</tt>组成新的字符串，并返回组装后的字符串长度
     * 
     * @param namespace
     * @param key
     * @param value
     * @param useNewKeySerialize(是否使用新的key序列化方式)     
     * @return
     */
    Long append(int namespace, String key, String value, boolean useNewKeySerialize);

    /**
     * 得到String从位置<tt>start</tt>到<tt>end</tt>的子字符串
     * 
     * @param namespace
     * @param key
     * @param start
     * @param end
     * @return
     */
    String get(int namespace, String key, long start, long end);
    
    /**
     * 得到String从位置<tt>start</tt>到<tt>end</tt>的子字符串
     * 
     * @param namespace
     * @param key
     * @param start
     * @param end
     * @param useNewKeySerialize(是否使用新的key序列化方式)     
     * @return
     */
    String get(int namespace, String key, long start, long end, boolean useNewKeySerialize);

    /**
     * 从<tt>offset</tt>开始替换为<tt>value</tt>
     * 
     * @param namespace
     * @param key
     * @param value
     * @param offset
     */
    void set(int namespace, String key, String value, long offset);
    
    /**
     * 从<tt>offset</tt>开始替换为<tt>value</tt>
     * 
     * @param namespace
     * @param key
     * @param value
     * @param offset
     * @param useNewKeySerialize(是否使用新的key序列化方式)     
     */
    void set(int namespace, String key, String value, long offset, boolean useNewKeySerialize);

    /**
     * 取得字符串长度
     * 
     * @param namespace
     * @param key
     * @return
     */
    Long size(int namespace, String key);
    
    /**
     * 取得字符串长度
     * 
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式)     
     * @return
     */
    Long size(int namespace, String key, boolean useNewKeySerialize);

    /**
     * 设置数据，如果数据已经存在，则覆盖，如果不存在，则新增
     * 
     * @param namespace
     *            数据所在的namespace
     * @param key
     * @param value
     */
    void set(int namespace, String key, String value);
    
    /**
     * 设置数据，如果数据已经存在，则覆盖，如果不存在，则新增
     * 
     * @param namespace
     *            数据所在的namespace
     * @param key
     * @param value
     * @param useNewKeySerialize(是否使用新的key序列化方式)     
     */
    void set(int namespace, String key, String value, boolean useNewKeySerialize);

    /**
     * 设置数据，如果数据已经存在，则覆盖，如果不存在，则新增
     * 
     * @param namespace
     *            数据所在的namespace
     * @param key
     * @param value
     * @param timeout
     *            数据的有效时间
     * @param unit
     *            时间单位
     */
    void set(int namespace, String key, String value, long timeout, TimeUnit unit);
    
    /**
     * 设置数据，如果数据已经存在，则覆盖，如果不存在，则新增
     * 
     * @param namespace
     *            数据所在的namespace
     * @param key
     * @param value
     * @param timeout
     *            数据的有效时间
     * @param unit
     *            时间单位
     * @param useNewKeySerialize(是否使用新的key序列化方式)     
     */
    void set(int namespace, String key, String value, long timeout, TimeUnit unit, boolean useNewKeySerialize);

    /**
     * 设置数据，如果数据已经存在，则保留原值返回<tt>false</tt>，如果不存在，则新增，返回<tt>true</tt>
     * 
     * @param namespace
     *            数据所在的namespace
     * @param key
     * @param value
     * @return 如果数据不存在返回<tt>true</tt>，否则返回<tt>false</tt>
     */
    Boolean setIfAbsent(int namespace, String key, String value);
    
    /**
     * 设置数据，如果数据已经存在，则保留原值返回<tt>false</tt>，如果不存在，则新增，返回<tt>true</tt>
     * 
     * @param namespace
     *            数据所在的namespace
     * @param key
     * @param value
     * @param useNewKeySerialize(是否使用新的key序列化方式)     
     * @return 如果数据不存在返回<tt>true</tt>，否则返回<tt>false</tt>
     */
    Boolean setIfAbsent(int namespace, String key, String value, boolean useNewKeySerialize);

    /**
     * 批量设置数据
     * 
     * @param namespace
     * @param m
     */
    void multiSet(int namespace, Map<String, String> m);

    /**
     * 批量设置数据，只有当数据不存在时才设置
     * 
     * @param namespace
     * @param m
     */
    void multiSetIfAbsent(int namespace, Map<String, String> m);

    /**
     * 获取数据 注意不能用此方法去获取increment方法产生的key
     * 
     * @param namespace
     * @param key
     * @return
     */
    String get(int namespace, Object key);

    /**
     * 获取数据 注意不能用此方法去获取increment方法产生的key
     * 
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式)     
     * @return
     */
    String get(int namespace, Object key, boolean useNewKeySerialize);

    /**
     * 设置数据同时返回老数据，此方法是原子操作 注意不能用此方法去获取increment方法产生的key
     * 
     * @param namespace
     * @param key
     * @param value
     * @return
     */
    String getAndSet(int namespace, String key, String value);

    /**
     * 设置数据同时返回老数据，此方法是原子操作 注意不能用此方法去获取increment方法产生的key
     * 
     * @param namespace
     * @param key
     * @param value
     * @param useNewKeySerialize(是否使用新的key序列化方式)     
     * @return
     */
    String getAndSet(int namespace, String key, String value, boolean useNewKeySerialize);

    /**
     * 批量获取数据 注意不能用此方法去获取increment方法产生的key
     * 
     * @param namespace
     * @param keys
     * @return
     */
    List<String> multiGet(int namespace, Collection<String> keys);
    
    /**
     * 批量获取数据 注意不能用此方法去获取increment方法产生的key
     * 
     * @param namespace
     * @param keys
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    List<String> multiGet(int namespace, Collection<String> keys, boolean useNewKeySerialize);

}
