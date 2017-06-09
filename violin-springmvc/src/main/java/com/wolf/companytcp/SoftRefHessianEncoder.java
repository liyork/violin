/**
 * Description: SoftRefHessianEncoder.java
 * All Rights Reserved.
 */
package com.wolf.companytcp;

import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;

/**
 */
public class SoftRefHessianEncoder extends HessianEncoder {

	@Override
	protected Object encode(ChannelHandlerContext context, Channel channel,
			Object object) throws Exception {
		
		return new SoftMark(super.encode(context, channel, object));
	}

}
