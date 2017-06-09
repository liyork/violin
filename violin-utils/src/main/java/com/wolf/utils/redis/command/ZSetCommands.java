
package com.wolf.utils.redis.command;

import com.wolf.utils.redis.command.Commands;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * 
 *  SortedSet操作
 * <br/> Created on 2014-7-3 下午1:51:19

 * @since 3.3
 */
public interface ZSetCommands extends Commands {

    /**
     * 取得<tt>key</tt>和<tt>otherKey</tt>的交集，将结果存入<tt>destKey</tt>
     *
     * @param namespace
     * @param key
     * @param otherKey
     * @param destKey
     */
    <K, V> void intersectAndStore(int namespace, K key, K otherKey, K destKey);
    
    /**
     * 取得<tt>key</tt>和<tt>otherKey</tt>的交集，将结果存入<tt>destKey</tt>
     *
     * @param namespace
     * @param key
     * @param otherKey
     * @param destKey
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     */
    <K, V> void intersectAndStore(int namespace, K key, K otherKey, K destKey, boolean useNewKeySerialize);

    /**
     * 取得<tt>key</tt>和<tt>otherKeys</tt>的交集，将结果存入<tt>destKey</tt>
     *
     * @param namespace
     * @param key
     * @param otherKeys
     * @param destKey
     */
    <K, V> void intersectAndStore(int namespace, K key, Collection<K> otherKeys, K destKey);
    
    /**
     * 取得<tt>key</tt>和<tt>otherKeys</tt>的交集，将结果存入<tt>destKey</tt>
     *
     * @param namespace
     * @param key
     * @param otherKeys
     * @param destKey
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     */
    <K, V> void intersectAndStore(int namespace, K key, Collection<K> otherKeys, K destKey, boolean useNewKeySerialize);

    /**
     * 取得<tt>key</tt>和<tt>otherKey</tt>的并集，将结果存入<tt>destKey</tt>
     *
     * @param namespace
     * @param key
     * @param otherKey
     * @param destKey
     */
    <K, V> void unionAndStore(int namespace, K key, K otherKey, K destKey);
    
    /**
     * 取得<tt>key</tt>和<tt>otherKey</tt>的并集，将结果存入<tt>destKey</tt>
     *
     * @param namespace
     * @param key
     * @param otherKey
     * @param destKey
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     */
    <K, V> void unionAndStore(int namespace, K key, K otherKey, K destKey, boolean useNewKeySerialize);

    /**
     * 取得<tt>key</tt>和<tt>otherKeys</tt>的并集，将结果存入<tt>destKey</tt>
     *
     * @param namespace
     * @param key
     * @param otherKeys
     * @param destKey
     */
    <K, V> void unionAndStore(int namespace, K key, Collection<K> otherKeys, K destKey);
    

    /**
     * 取得<tt>key</tt>和<tt>otherKeys</tt>的并集，将结果存入<tt>destKey</tt>
     *
     * @param namespace
     * @param key
     * @param otherKeys
     * @param destKey
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     */
    <K, V> void unionAndStore(int namespace, K key, Collection<K> otherKeys, K destKey, boolean useNewKeySerialize);

    /**
     * 从权重最低的数据开始，取得位置从<tt>start</tt>到<tt>end</tt>的数据
     *
     * @param namespace
     * @param key
     * @param start
     * @param end
     * @return
     */
    <K, V> Set<V> range(int namespace, K key, long start, long end);
    
    /**
     * 从权重最低的数据开始，取得位置从<tt>start</tt>到<tt>end</tt>的数据
     *
     * @param namespace
     * @param key
     * @param start
     * @param end
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K, V> Set<V> range(int namespace, K key, long start, long end, boolean useNewKeySerialize);

    /**
     * 从权重最低的数据开始，取得位置从<tt>start</tt>到<tt>end</tt>的数据
     *
     * @param namespace
     * @param key
     * @param start
     * @param end
     * @return
     */
    <K, V> Map<V, Double> rangeWithScore(int namespace, K key, long start, long end);
    
    /**
     * 从权重最低的数据开始，取得位置从<tt>start</tt>到<tt>end</tt>的数据
     *
     * @param namespace
     * @param key
     * @param start
     * @param end
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K, V> Map<V, Double> rangeWithScore(int namespace, K key, long start, long end, boolean useNewKeySerialize);

    /**
     * 取得权重从<tt>min</tt>到<tt>max</tt>的数据
     *
     * @param namespace
     * @param key
     * @param min
     * @param max
     * @return
     */
    <K, V> Set<V> rangeByScore(int namespace, K key, double min, double max);

    /**
     * 取得权重从<tt>min</tt>到<tt>max</tt>的数据
     *
     * @param namespace
     * @param key
     * @param min
     * @param max
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K, V> Set<V> rangeByScore(int namespace, K key, double min, double max, boolean useNewKeySerialize);

    /**
     * 取得权重从<tt>min</tt>到<tt>max</tt>的数据
     *
     * @param namespace
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    <K, V> Set<V> rangeByScore(int namespace, K key, double min, double max, int offset, int count);

    /**
     * 取得权重从<tt>min</tt>到<tt>max</tt>的数据
     *
     * @param namespace
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K, V> Set<V> rangeByScore(int namespace, K key, double min, double max, int offset, int count, boolean useNewKeySerialize);

    /**
     * 取得权重从<tt>min</tt>到<tt>max</tt>的数据
     *
     * @param namespace
     * @param key
     * @param min
     * @param max
     * @return
     */
    <K, V> Set<V> reverseRangeByScore(int namespace, K key, double min, double max);

    /**
     * 取得权重从<tt>min</tt>到<tt>max</tt>的数据
     *
     * @param namespace
     * @param key
     * @param min
     * @param max
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K, V> Set<V> reverseRangeByScore(int namespace, K key, double min, double max, boolean useNewKeySerialize);

    /**
     * 取得权重从<tt>min</tt>到<tt>max</tt>的数据
     *
     * @param namespace
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @return
     */
    <K, V> Set<V> reverseRangeByScore(int namespace, K key, double min, double max, int offset, int count);

    /**
     * 取得权重从<tt>min</tt>到<tt>max</tt>的数据
     *
     * @param namespace
     * @param key
     * @param min
     * @param max
     * @param offset
     * @param count
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K, V> Set<V> reverseRangeByScore(int namespace, K key, double min, double max, int offset, int count, boolean useNewKeySerialize);

    /**
     * 取得权重从<tt>min</tt>到<tt>max</tt>的数据
     *
     * @param namespace
     * @param key
     * @param min
     * @param max
     * @return
     */
    <K, V> Map<V, Double> rangeByScoreWithScore(int namespace, K key, double min, double max);

    /**
     * 取得权重从<tt>min</tt>到<tt>max</tt>的数据
     *
     * @param namespace
     * @param key
     * @param min
     * @param max
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K, V> Map<V, Double> rangeByScoreWithScore(int namespace, K key, double min, double max, boolean useNewKeySerialize);   
    
    /**
     * 从权重最高的数据开始，取得位置从<tt>start</tt>到<tt>end</tt>的数据
     *
     * @param namespace
     * @param key
     * @param start
     * @param end
     * @return
     */
    <K, V> Set<V> reverseRange(int namespace, K key, long start, long end);
    
    /**
     * 从权重最高的数据开始，取得位置从<tt>start</tt>到<tt>end</tt>的数据
     *
     * @param namespace
     * @param key
     * @param start
     * @param end
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K, V> Set<V> reverseRange(int namespace, K key, long start, long end, boolean useNewKeySerialize);

    /**
     * 从权重最高的数据开始，取得位置从<tt>start</tt>到<tt>end</tt>的数据
     *
     * @param namespace
     * @param key
     * @param start
     * @param end
     * @return
     */
    <K, V> Map<V, Double> reverseRangeWithScore(int namespace, K key, long start, long end);

    /**
     * 从权重最高的数据开始，取得位置从<tt>start</tt>到<tt>end</tt>的数据
     *
     * @param namespace
     * @param key
     * @param start
     * @param end
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K, V> Map<V, Double> reverseRangeWithScore(int namespace, K key, long start, long end, boolean useNewKeySerialize);

    /**
     * 增加一个数据
     *
     * @param namespace
     * @param key
     * @param value
     * @param score
     * @return
     */
    <K, V> Boolean add(int namespace, K key, V value, double score);

    /**
     * 增加一个数据
     *
     * @param namespace
     * @param key
     * @param value
     * @param score
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K, V> Boolean add(int namespace, K key, V value, double score, boolean useNewKeySerialize);

    /**
     * 增加多个数据
     *
     * @param namespace
     * @param key
     * @param value
     * @param score
     * @return
     */
    <K, V> Long add(int namespace, K key, Map<V, Double> maps);

    /**
     * 增加多个数据
     *
     * @param namespace
     * @param key
     * @param value
     * @param score
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K, V> Long add(int namespace, K key, Map<V, Double> maps, boolean useNewKeySerialize);

    /**
     * 增加一个数据的权重，并且返回更新后的权重值
     *
     * @param namespace
     * @param key
     * @param value
     * @param delta
     * @return
     */
    <K, V> Double incrementScore(int namespace, K key, V value, double delta);

    /**
     * 增加一个数据的权重，并且返回更新后的权重值
     *
     * @param namespace
     * @param key
     * @param value
     * @param delta
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K, V> Double incrementScore(int namespace, K key, V value, double delta, boolean useNewKeySerialize);

    /**
     * 得到一个数据跟中权重的排名，排名最低的Rank值为0
     *
     * @param namespace
     * @param key
     * @param o
     * @return
     */
    <K, V> Long rank(int namespace, K key, Object o);

    /**
     * 得到一个数据跟中权重的排名，排名最低的Rank值为0
     *
     * @param namespace
     * @param key
     * @param o
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K, V> Long rank(int namespace, K key, Object o, boolean useNewKeySerialize);

    /**
     * 得到一个数据跟中权重的排名，排名最高的Rank值为0
     *
     * @param namespace
     * @param key
     * @param o
     * @return
     */
    <K, V> Long reverseRank(int namespace, K key, Object o);

    /**
     * 得到一个数据跟中权重的排名，排名最高的Rank值为0
     *
     * @param namespace
     * @param key
     * @param o
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K, V> Long reverseRank(int namespace, K key, Object o, boolean useNewKeySerialize);

    /**
     * 得到一个数据的权重
     *
     * @param namespace
     * @param key
     * @param o
     * @return
     */
    <K, V> Double score(int namespace, K key, Object o);

    /**
     * 得到一个数据的权重
     *
     * @param namespace
     * @param key
     * @param o
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K, V> Double score(int namespace, K key, Object o, boolean useNewKeySerialize);

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
     * 删除一个或者多个数据
     * @param namespace
     * @param key
     * @param value
     * @param <K>
     * @param <V>
     * @return
     */
    <K, V> Long nremove(int namespace, K key, Object... value);

    /**
     * 删除一个或者多个数据，并且可以指定key的序列化方式
     * @param namespace
     * @param key
     * @param useNewKeySerialize
     * @param value
     * @param <K>
     * @param <V>
     * @return
     */
    <K, V> Long nremove(boolean useNewKeySerialize, int namespace, K key, Object... value);

    /**
     * 删除坐标从<tt>start</tt>到<tt>end</tt>的所有数据
     *
     * @param namespace
     * @param key
     * @param start
     * @param end
     */
    <K, V> void removeRange(int namespace, K key, long start, long end);

    /**
     * 删除坐标从<tt>start</tt>到<tt>end</tt>的所有数据
     *
     * @param namespace
     * @param key
     * @param start
     * @param end
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     */
    <K, V> void removeRange(int namespace, K key, long start, long end, boolean useNewKeySerialize);

    /**
     * 删除权重值从<tt>min</tt>到<tt>max</tt>的所有数据
     *
     * @param namespace
     * @param key
     * @param min
     * @param max
     */
    <K, V> void removeRangeByScore(int namespace, K key, double min, double max);

    /**
     * 删除权重值从<tt>min</tt>到<tt>max</tt>的所有数据
     *
     * @param namespace
     * @param key
     * @param min
     * @param max
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     */
    <K, V> void removeRangeByScore(int namespace, K key, double min, double max, boolean useNewKeySerialize);

    /**
     * 取得权重值从<tt>min</tt>到<tt>max</tt>的数据数量
     *
     * @param namespace
     * @param key
     * @param min
     * @param max
     * @return
     */
    <K, V> Long count(int namespace, K key, double min, double max);
    
    /**
     * 取得权重值从<tt>min</tt>到<tt>max</tt>的数据数量
     *
     * @param namespace
     * @param key
     * @param min
     * @param max
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K, V> Long count(int namespace, K key, double min, double max, boolean useNewKeySerialize);

    /**
     * 取得SortedSet大小
     *
     * @param namespace
     * @param key
     * @return
     */
    <K, V> Long size(int namespace, K key);
    
    /**
     * 取得SortedSet大小
     *
     * @param namespace
     * @param key
     * @param useNewKeySerialize(是否使用新的key序列化方式) 
     * @return
     */
    <K, V> Long size(int namespace, K key, boolean useNewKeySerialize);

}
