
package com.wolf.utils.redis;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 下午1:23:38

 * @since 3.3
 */
public interface TedisSerializer<T> {

    /**
     * Serialize the given object to binary data.
     * 
     * @param t
     *            object to serialize
     * @return the equivalent binary data
     */
    byte[] serialize(T t) throws SerializationException;

    /**
     * Deserialize an object from the given binary data.
     * 
     * @param bytes
     *            object binary representation
     * @return the equivalent object instance
     */
    T deserialize(byte[] bytes) throws SerializationException;

}
