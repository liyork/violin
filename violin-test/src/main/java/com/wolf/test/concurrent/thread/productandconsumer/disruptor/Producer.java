package com.wolf.test.concurrent.thread.productandconsumer.disruptor;

import com.lmax.disruptor.RingBuffer;
import com.wolf.test.concurrent.thread.productandconsumer.onetoone.useblockingqueue.Food;
import io.netty.buffer.ByteBuf;

import java.nio.ByteBuffer;

/**
 * Description:
 * <br/> Created on 22/03/2018 8:52 PM
 *
 * @author 李超
 * @since 1.0.0
 */
public class Producer {

    private final RingBuffer<Food> ringBuffer;

    public Producer(RingBuffer<Food> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    public void pushData(ByteBuffer byteBuffer) {
        long sequence = ringBuffer.next();
        Food food = ringBuffer.get(sequence);
        food.setId(byteBuffer.getInt(0));

        ringBuffer.publish(sequence);

    }
}
