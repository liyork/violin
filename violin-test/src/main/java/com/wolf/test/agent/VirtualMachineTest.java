package com.wolf.test.agent;

import com.sun.tools.attach.VirtualMachine;

/**
 * Description:
 * 用SimpleAgentMain打jar
 * 先运行TargetVM.java测试
 * 再执行此main
 * <p>
 * agent似乎是在defineclass之前执行，由于一次attach时是classloader第一次加载class时，以后当app运行时就会从缓存中取class
 * <p>
 * SimpleAgentMain没有修改class文件，仅仅打印一些信息，下次执行VirtualMachineTest.main则还会打印，不会永久保存
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
        VirtualMachine vm = VirtualMachine.attach("920");
        //第二个参数对应agent中的参数
//        vm.loadAgent("D:\\intellijWrkSpace\\violin\\violin-test\\target\\classes\\simpleagentmain.jar", "D:/tools/java/profile.txt");
//        vm.loadAgent("/Users/chaoli/IdeaProjects/violin/violin-test/target/classes/simpleagentmain.jar", "D:/tools/java/profile.txt");//mac

//        vm.loadAgent("D:\\intellijWrkSpace\\violin\\violin-test\\target\\classes\\agentmain.jar", "D:/tools/java/profile.txt");
        vm.loadAgent("/Users/chaoli/IdeaProjects/violin/violin-test/target/classes/agentmain.jar", "D:/tools/java/profile.txt");
        Thread.sleep(10000);
        //vm.detach();
    }
}
