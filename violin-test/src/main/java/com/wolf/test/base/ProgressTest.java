package com.wolf.test.base;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.CountDownLatch;

/**
 * Description:
 * <br/> Created on 2018/2/2 10:45
 *
 * @author 李超
 * @since 1.0.0
 */
public class ProgressTest {

    public static void main(String[] args) throws IOException, InterruptedException {

        String cp = System.getProperty("java.class.path");
        String toolsjar = getToolsjarPath(cp);

        String javaHomePath = System.getProperty("java.home").replace(File.separator + "jre", "");

        List<String> args1 = new ArrayList<>(Arrays.asList(
                javaHomePath + "/bin/java",
                "-cp",
                cp
        ));

//        args1.add("UUIDTest");//写错时，控制台打印的信息是乱码，需要下面使用gbk，可能由于win的ProcessBuilder使用gbk导致
        args1.add("com.wolf.test.jvm.InternalParam");

        ProcessBuilder pb = new ProcessBuilder(args1);
        pb.environment().remove("JAVA_TOOL_OPTIONS");

        Process p = pb.start();

//        final BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(p.getInputStream(), "gbk"));
        final BufferedReader stdoutReader = new BufferedReader(new InputStreamReader(p.getInputStream(), StandardCharsets.UTF_8));

        final CountDownLatch countDownLatch = new CountDownLatch(1);

        Thread outT = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String l;
                    while ((l = stdoutReader.readLine()) != null) {
                        if (l.startsWith("ready:")) {
                            System.out.println(l.split("\\:")[1]);
                            countDownLatch.countDown();
                        }
                        System.out.println("[traced app] " + l);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "STDOUT Reader");
        outT.setDaemon(true);

//        final BufferedReader stderrReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), "gbk"));
        final BufferedReader stderrReader = new BufferedReader(new InputStreamReader(p.getErrorStream(), StandardCharsets.UTF_8));

        Thread errT = new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String l = null;
                    while ((l = stderrReader.readLine()) != null) {
                        countDownLatch.countDown();
                        System.err.println("[traced app] " + l);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, "STDERR Reader");
        errT.setDaemon(true);

        outT.start();
        errT.start();

        countDownLatch.await();
    }

    private static String getToolsjarPath(String cp) {
        String toolsjar = null;
        StringTokenizer st = new StringTokenizer(cp, File.pathSeparator);
        while (st.hasMoreTokens()) {
            String elem = st.nextToken();
            if (elem.contains("tools.jar")) {
                toolsjar = elem;
            }
        }
        if (toolsjar == null) {
            URL rturl = String.class.getResource("/java/lang/String.class");
            toolsjar = rturl.toString().replace("jar:file:", "").replace("jre/lib/rt.jar", "lib/tools.jar");
            toolsjar = toolsjar.substring(0, toolsjar.indexOf('!'));
            System.err.println(toolsjar);
        }
        return toolsjar;
    }
}
