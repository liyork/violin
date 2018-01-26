package com.wolf.test.netty.inaction;

import io.netty.buffer.*;
import io.netty.channel.*;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Description:
 * <br/> Created on 9/21/17 8:31 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class ByteBufTest {

    Charset charset = Charset.forName("utf-8");

    @Test
    public void testHeapBuf() throws UnsupportedEncodingException {
        getHeapBuf();
    }

    private ByteBuf getHeapBuf() {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(1);
        byteBuf.writeInt(2);
        byteBuf.writeInt(3);
        if (byteBuf.hasArray()) {
            byte[] array = byteBuf.array();
            int offset = byteBuf.arrayOffset();
            int length = byteBuf.readableBytes();
            System.out.println(offset + " " + length+" "+array.length);

            for (int i = 0; i < byteBuf.capacity(); i++) {
                System.out.println((char)byteBuf.getByte(i));//does not modify readerIndex or writerIndex of this buffer.
            }
        }
        return byteBuf;
    }

    @Test
    public void testDirectBuf() {
        getDirectBuf();

    }

    private ByteBuf getDirectBuf() {
        ByteBuf byteBuf = Unpooled.directBuffer();
        byteBuf.writeInt(1);
        byteBuf.writeInt(2);
        byteBuf.writeInt(3);
        System.out.println(byteBuf.hasArray());

        byte[] array = new byte[byteBuf.readableBytes()];
        byteBuf.getBytes(0, array);
        System.out.println(array);
        return byteBuf;
    }

    @Test
    public void testCompositeBuf() {
        CompositeByteBuf byteBuf = Unpooled.compositeBuffer();
        byteBuf.addComponent(getHeapBuf());
        byteBuf.addComponent(getDirectBuf());
        System.out.println(byteBuf.hasArray());

        for (Iterator<ByteBuf> it = byteBuf.iterator(); it.hasNext(); ) {
            Object o = it.next();
            System.out.println(o);
        }

    }

    //if you need to free up memory as soon as possible.Such an operation isn t free and may affect performance
    @Test
    public void testDiscardReadBytes() throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(1);
        byteBuf.writeInt(2);
        byteBuf.writeInt(3);
        byteBuf.writeInt(4);


        System.out.println(byteBuf.readInt());
        System.out.println(byteBuf.readInt());

        System.out.println(byteBuf.arrayOffset()+" "+byteBuf.readerIndex()+" "+byteBuf.writerIndex());

        byteBuf.discardReadBytes();//移除已经读的部分

        System.out.println(byteBuf.arrayOffset()+" "+byteBuf.readerIndex()+" "+byteBuf.writerIndex());
    }

    @Test
    public void testClear() throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(1);
        byteBuf.writeInt(2);
        byteBuf.writeInt(3);
        byteBuf.writeInt(4);


        System.out.println(byteBuf.readInt());
        System.out.println(byteBuf.readInt());

        System.out.println(byteBuf.arrayOffset()+" "+byteBuf.readerIndex()+" "+byteBuf.writerIndex());

        byteBuf.clear();

        System.out.println(byteBuf.arrayOffset()+" "+byteBuf.readerIndex()+" "+byteBuf.writerIndex());
    }

    //get、set不移动index
    @Test
    public void testSetGet() throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.buffer(16);
        byteBuf.writeInt(2);
        byteBuf.writeInt(4);
        byteBuf.writeInt(1);

        System.out.println(byteBuf.getInt(0));
        byteBuf.setInt(0, 3);

        System.out.println(byteBuf.readerIndex()+" "+byteBuf.writerIndex());

    }

    //read、write移动index
    @Test
    public void testReadWrite() throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.buffer(16);
        Random random= new Random();
        while (byteBuf.writableBytes()>=4) {
            byteBuf.writeInt(random.nextInt(10));
        }

        while (byteBuf.isReadable()) {
            System.out.println(byteBuf.readInt());
        }
    }

    @Test
    public void testByteBefore() throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.buffer();
        byteBuf.writeInt(1);
        byteBuf.writeInt(2);
        byteBuf.writeByte(44);
        byteBuf.writeInt(3);
        byteBuf.writeInt(4);

        int i1 = byteBuf.indexOf(0, byteBuf.capacity(), (byte) 2);
        System.out.println(i1);
        System.out.println(byteBuf.arrayOffset()+" "+byteBuf.readerIndex()+" "+byteBuf.writerIndex());
        int i = byteBuf.bytesBefore((byte) 3);
        System.out.println(i);


        System.out.println(byteBuf.arrayOffset()+" "+byteBuf.readerIndex()+" "+byteBuf.writerIndex());
    }

    //引用一个，create a view of an existing buffer
    @Test
    public void testSlice() throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in action rocks!", charset);
        ByteBuf slice = byteBuf.slice(0, 14);
        System.out.println(slice.toString(charset));

        slice.setByte(0, 'J');
        System.out.println(slice.getByte(0)==byteBuf.getByte(0));
    }

    //两个对象
    @Test
    public void testCopy() throws UnsupportedEncodingException {

        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in action rocks!", charset);
        ByteBuf copy = byteBuf.copy(0, 14);
        System.out.println(copy.toString(charset));

        copy.setByte(0, 'J');
        System.out.println(copy.getByte(0)==byteBuf.getByte(0));
    }

    @Test
    public void testByteBufHolder() throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in action rocks!", charset);
        ByteBufHolder byteBufHolder = new DefaultByteBufHolder(byteBuf);

        ByteBuf content = byteBufHolder.content();
        System.out.println(content);

    }

    @Test
    public void testByteBufAllocator() throws UnsupportedEncodingException {
//        Channel channel = ...;
//        ByteBufAllocator allocator = channel.alloc();                            #1
//
//        ChannelHandlerContext ctx = ...;
//        ByteBufAllocator allocator2 = ctx.alloc();

    }

    @Test
    public void testByteBufUtil() throws UnsupportedEncodingException {
        ByteBuf byteBuf = Unpooled.copiedBuffer("Netty in action rocks!", charset);
        String s = ByteBufUtil.hexDump(byteBuf);
        System.out.println(s);
    }

    @Test
    public void testChannelPipeline() throws UnsupportedEncodingException {
        Channel channel = new NioServerSocketChannel();
        ChannelPipeline pipeline = channel.pipeline();//DefaultChannelPipeline伴随channel而创建

        ChannelHandler channelHandler = new SimpleChannelInboundHandler<String>() {
            @Override
            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {

            }
        };
        pipeline.addFirst(channelHandler);
        pipeline.addLast(channelHandler);

        pipeline.get("xx");

        ChannelHandlerContext context = pipeline.context(channelHandler);
        List<String> names = pipeline.names();
        Iterator<Map.Entry<String, ChannelHandler>> iterator = pipeline.iterator();

        pipeline.fireChannelRegistered();//invoke next
    }

    //写事件流动
    @Test
    public void testPassWriteEvent() throws UnsupportedEncodingException {
        ChannelHandlerContext ctx = null;
        Channel channel = ctx.channel();
        ByteBuf bytebuf = Unpooled.copiedBuffer("xxxx", charset);
        channel.write(bytebuf);
        //相同结果
        ChannelPipeline pipeline = ctx.pipeline();
        pipeline.write(bytebuf);


        //获取指定的ctx,调用write将触发下一个handler
        ChannelHandlerContext context = pipeline.context("appointhandlername");
        context.write(bytebuf);

        //handler可以被多个pipeline使用，如果是那样小心线程问题
    }

    //ChannelHandlerAdapter is Skelton implementation of a {@link ChannelHandler}
}
