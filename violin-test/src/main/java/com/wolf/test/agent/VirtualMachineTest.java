package com.wolf.test.agent;

import com.sun.tools.attach.VirtualMachine;

/**
 * Description:
 * agent似乎是在defineclass之前执行，由于一次attach时是classloader第一次加载class时，以后当app运行时就会从缓存中取class
 * <p>
 * <p>
 * <br/> Created on 2018/1/24 10:03
 *
 * @author 李超
 * @since 1.0.0
 */
public class VirtualMachineTest {

    public static void main(String[] args) throws Exception {
        // attach to target VM
        VirtualMachine vm = VirtualMachine.attach("6952");
        //第二个参数对应agent中的参数
//        vm.loadAgent("D:\\intellijWrkSpace\\violin\\violin-test\\target\\classes\\simpleagentmain.jar", "D:/tools/java/profile.txt");
        vm.loadAgent("D:\\intellijWrkSpace\\violin\\violin-test\\target\\classes\\agentmain.jar", "D:/tools/java/profile.txt");
        Thread.sleep(10000);
        //vm.detach();
    }
}
