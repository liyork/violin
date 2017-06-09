
package com.wolf.utils;


import com.wolf.utils.redis.TedisDataException;
import com.wolf.utils.redis.TedisException;
import com.wolf.utils.redis.TedisSerializer;
import com.wolf.utils.redis.command.RedisZSetCommands.Tuple;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 下午1:32:32

 * @since 3.3
 */
@SuppressWarnings("unchecked")
public abstract class SerializationUtils {

    public static final byte[] EMPTY_ARRAY = new byte[0];

    public static final String CHARSET = "UTF-8";

    public static boolean isEmpty(byte[] data) {
        return (data == null || data.length == 0);
    }

    public static byte[] serialize(final String str) {
        try {
            if (str == null) {
                throw new TedisDataException("value sent to redis cannot be null");
            }
            return str.getBytes(CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new TedisException(e);
        }
    }

    public static byte[][] serialize(List<String> strings, TedisSerializer<String> stringSerializer) {
        List<byte[]> raw = null;

        if (strings == null) {
            raw = Collections.emptyList();
        } else {
            raw = new ArrayList<byte[]>(strings.size());
            for (String key : strings) {
                raw.add(stringSerializer.serialize(key));
            }
        }
        return raw.toArray(new byte[raw.size()][]);
    }

    public static String deserialize(final byte[] data) {
        try {
            return new String(data, CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new TedisException(e);
        }
    }

    public static <T extends Collection<?>> T deserializeValues(Collection<byte[]> rawValues, Class<T> type, TedisSerializer<?> redisSerializer) {
        if (rawValues == null) {
            return null;
        }

        Collection<Object> values = (List.class.isAssignableFrom(type) ? new ArrayList<Object>(rawValues.size()) : new LinkedHashSet<Object>(rawValues.size()));
        for (byte[] bs : rawValues) {
            values.add(redisSerializer.deserialize(bs));
        }

        return (T) values;
    }

    public static <T> Set<T> deserialize(Set<byte[]> rawValues, TedisSerializer<T> redisSerializer) {
        return deserializeValues(rawValues, Set.class, redisSerializer);
    }

    public static <T> List<T> deserialize(List<byte[]> rawValues, TedisSerializer<T> redisSerializer) {
        return deserializeValues(rawValues, List.class, redisSerializer);
    }

    public static <T> Collection<T> deserialize(Collection<byte[]> rawValues, TedisSerializer<T> redisSerializer) {
        return deserializeValues(rawValues, List.class, redisSerializer);
    }

    public static <HK> Map<HK, Double> deserializeTruble(Set<Tuple> tuples, TedisSerializer<HK> redisSerializer) {
        if (tuples == null) {
            return null;
        }
        Map<HK, Double> map = new LinkedHashMap<HK, Double>(tuples.size());
        for (Tuple t : tuples) {
            map.put((HK) redisSerializer.deserialize(t.getValue()), t.getScore());
        }
        return map;
    }
}
