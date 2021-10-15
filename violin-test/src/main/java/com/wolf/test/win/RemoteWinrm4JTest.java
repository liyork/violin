package com.wolf.test.win;

///**
// * Description: jdk8可以执行
// * Created on 2021/9/3 5:36 PM
// *
// * @author 李超
// * @version 0.0.1
// */
//public class RemoteWinrm4JTest {
//    public static void main(String[] args) {
//        WinRmClientContext context = WinRmClientContext.newInstance();
//        WinRmTool.Builder builder = WinRmTool.Builder.builder("172.17.189.214", "administrator", "Passw0rd");
//        builder.setAuthenticationScheme(AuthSchemes.NTLM);
//        builder.port(5985);
//        builder.useHttps(false);
//
//        builder.context(context);
//        WinRmTool tool = builder.build();
//        tool.setOperationTimeout(5000L);
//        System.out.println("========");
//        String command = "dir";
//        //String command = "ps -rl";
//        WinRmToolResponse resp = tool.executeCommand(command);
//        System.out.println(resp.getStatusCode());
//        String out = resp.getStdOut();
//        System.out.println(out);
//        context.shutdown();
//    }
//}
