
package com.wolf.utils.redis;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 上午11:56:57

 * @since 3.3
 */
public class TedisDataException extends TedisException {

    private static final long serialVersionUID = 7530985946271632187L;

    public TedisDataException(String message) {
        super(message);
    }

    public TedisDataException(Throwable cause) {
        super(cause);
    }

    public TedisDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
