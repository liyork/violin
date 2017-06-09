
package com.wolf.utils.redis;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 上午11:57:15

 * @since 3.3
 */
public class TedisException extends RuntimeException {

    private static final long serialVersionUID = -1611101501690221263L;

    public TedisException(String message) {
        super(message);
    }

    public TedisException(Throwable e) {
        super(e);
    }

    public TedisException(String message, Throwable cause) {
        super(message, cause);
    }
}
