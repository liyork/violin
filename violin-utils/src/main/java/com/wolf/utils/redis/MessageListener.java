
package com.wolf.utils.redis;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 上午11:48:29

 * @since 3.3
 */
public interface MessageListener {

    void onMessage(Message message, byte[] pattern);
}
