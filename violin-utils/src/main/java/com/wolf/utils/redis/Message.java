
package com.wolf.utils.redis;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 
 *  
 * <br/> Created on 2014-7-3 上午11:48:00

 * @since 3.3
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 425983229881691138L;
    private final byte[] channel;
    private final byte[] body;

    public Message(byte[] channel, byte[] body) {
        this.body = body;
        this.channel = channel;
    }

    public byte[] getChannel() {
        return (channel != null ? channel.clone() : null);
    }

    public byte[] getBody() {
        return (body != null ? body.clone() : null);
    }

    @Override
    public String toString() {
        return Arrays.toString(body);
    }

}
