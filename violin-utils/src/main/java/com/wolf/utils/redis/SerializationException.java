
package com.wolf.utils.redis;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 下午1:32:46

 * @since 3.3
 */
public class SerializationException extends RuntimeException {

    private static final long serialVersionUID = -8561411072499373859L;

    /**
     * Constructs a new <code>SerializationException</code> instance.
     * 
     * @param msg
     * @param cause
     */
    public SerializationException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs a new <code>SerializationException</code> instance.
     * 
     * @param msg
     */
    public SerializationException(String msg) {
        super(msg);
    }
}
