package com.wolf.companytcp;

import com.wolf.utils.hessianserialize.HessianSerializerUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * hessian编码器
 */
public class HessianEncoder extends OneToOneEncoder {

    private static final int BYTE_LENGTH = 4;

    protected Object encode(ChannelHandlerContext context, Channel channel, Object object) throws Exception {

        byte[] val = HessianSerializerUtils.serialize(object);
        int dataLength = val.length;
        ChannelBuffer buffer = ChannelBuffers.dynamicBuffer(dataLength + BYTE_LENGTH);
        buffer.writeInt(dataLength);
        buffer.writeBytes(val);
        return buffer;
    }

}
