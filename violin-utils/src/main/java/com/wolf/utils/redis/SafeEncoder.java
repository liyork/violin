
package com.wolf.utils.redis;

import java.io.UnsupportedEncodingException;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 下午1:22:21

 * @since 3.3
 */
public class SafeEncoder {
	
	public static byte[] longToByte(long number) {         
		long temp = number;         
		byte[] b = new byte[8];         
		for (int i = 0; i < b.length; i++) {             
			b[i] = new Long(temp & 0xff).byteValue();
			// 将最低位保存在最低位             
			temp = temp >> 8; // 向右移8位       
		
		}        
		return b;  
	}
	
	
    public static byte[] encode(final String str) {
        try {
            if (str == null) {
                throw new TedisDataException("value sent to redis cannot be null");
            }
            return str.getBytes(Protocol.CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new TedisException(e);
        }
    }

    public static String encode(final byte[] data) {
        try {
            return new String(data, Protocol.CHARSET);
        } catch (UnsupportedEncodingException e) {
            throw new TedisException(e);
        }
    }
}
