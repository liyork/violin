
package com.wolf.utils.redis;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 上午11:56:35

 * @since 3.3
 */
public class TedisConnectionException extends TedisException {
    private static final long serialVersionUID = 6991632939402705878L;

    public TedisConnectionException(String message) {
        super(message);
    }

    public TedisConnectionException(Throwable cause) {
        super(cause);
    }

    public TedisConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
