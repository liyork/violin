package com.wolf.test.concurrent.actualcombat.pipeline;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Description:
 * <br/> Created on 23/03/2018 10:53 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class Divide implements Runnable {

    static BlockingQueue<ComputerTransfer> blockingQueue = new LinkedBlockingQueue<ComputerTransfer>();

    @Override
    public void run() {
        for(;;) {
            try {
                ComputerTransfer take = blockingQueue.take();
                int result = take.getB() /2;
                System.out.println("src:"+take.getSourceSrc()+" = "+result);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
