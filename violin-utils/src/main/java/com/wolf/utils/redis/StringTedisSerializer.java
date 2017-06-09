
package com.wolf.utils.redis;

import com.wolf.utils.SerializationUtils;

import java.nio.charset.Charset;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 下午1:23:15

 * @since 3.3
 */
public class StringTedisSerializer implements TedisSerializer<String> {

    private final Charset charset;

    public StringTedisSerializer() {
        this(Charset.forName(SerializationUtils.CHARSET));
    }

    public StringTedisSerializer(Charset charset) {
        this.charset = charset;
    }

    @Override
    public String deserialize(byte[] bytes) {
        return (bytes == null ? null : new String(bytes, charset));
    }

    @Override
    public byte[] serialize(String string) {
        return (string == null ? null : string.getBytes(charset));
    }
}
