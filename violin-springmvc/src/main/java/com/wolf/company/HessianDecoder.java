package com.wolf.company;

import com.wolf.utils.hessianserialize.HessianSerializerUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * hessian解码器
 */
public class HessianDecoder extends FrameDecoder {

    private static final int BYTE_LENGTH = 4;

    protected Object decode(ChannelHandlerContext context, Channel channel, ChannelBuffer buffer) throws Exception {

        if(buffer.readableBytes() < BYTE_LENGTH) {
            return null;
        }
        buffer.markReaderIndex();
        int dataLength = buffer.readInt();

        if(buffer.readableBytes() < dataLength) {
            buffer.resetReaderIndex();
            return null;
        }
        ChannelBuffer newBuffer = buffer.readBytes(dataLength);

        return HessianSerializerUtils.deserialize(newBuffer.array());
    }

}
