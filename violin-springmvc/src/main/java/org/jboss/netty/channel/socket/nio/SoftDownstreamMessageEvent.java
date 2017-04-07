/**
 * Description: SoftDownstreamMessageEvent.java
 * All Rights Reserved.

 */
package org.jboss.netty.channel.socket.nio;

import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.DownstreamMessageEvent;

import java.lang.ref.SoftReference;
import java.net.SocketAddress;

/**
 *  soft event
 * <br/> Created on 2014-10-28 下午3:04:04

 * @since 3.4
 */
public class SoftDownstreamMessageEvent extends DownstreamMessageEvent {

	public SoftDownstreamMessageEvent(Channel channel, ChannelFuture future,
			Object message, SocketAddress remoteAddress) {
		super(channel, future, message, remoteAddress);
		
	}

	@Override
	public Object getMessage() {
		
		SoftReference<?> value =  (SoftReference<?>)super.getMessage();
		Object obj = value.get();
		if(obj == null){
			return ChannelBuffers.EMPTY_BUFFER;
		}
		return obj;
	}
	
	

}
