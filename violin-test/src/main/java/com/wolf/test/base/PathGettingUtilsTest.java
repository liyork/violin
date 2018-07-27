package com.wolf.test.base;

import com.wolf.utils.PathGettingUtils;
import com.wolf.utils.log.CustomLoggerFactory;
import org.junit.Test;

import java.io.File;
import java.net.URL;

/**
 * Description:
 * <br/> Created on 2016/12/30 1:01
 *
 * @author 李超()
 * @since 1.0.0
 */
public class PathGettingUtilsTest {

    //  /D:/intellijWrkSpace/violin/violin-utils/target/classes/
    // /Users/chaoli/IdeaProjects/violin/violin-test/target/classes/
    // 1.由于有类加载器了，所以是运行时路径。2.类加载器获取""就是项目根路径。
    @Test
    public void testGetClassPath() {
        URL url1 = PathGettingUtils.getClassPathRoot1(PathGettingUtilsTest.class);
        URL url2 = PathGettingUtils.getClassPathRoot2();
        System.out.println(url2.getPath());
        System.out.println(url1.getPath().equals(url2.getPath()));
    }

    @Test
    public void testGetPackagePath() {
        System.out.println(PathGettingUtils.getPackagePath(PathGettingUtilsTest.class));
    }

    @Test
    public void testGetResourceFromClassPath() {
        URL url1 = PathGettingUtils.getResourceFromClassPath(PathGettingUtilsTest.class, "com/wolf/utils");
        System.out.println(url1.getPath());
        URL url2 = PathGettingUtils.getResourceFromClassPath(PathGettingUtilsTest.class, "com/wolf/utils/");
        System.out.println(url2.getPath());
        URL url3 = PathGettingUtils.getResourceFromClassPath(PathGettingUtilsTest.class, "com/wolf/utils/JsonUtils.class");
        System.out.println(url3.getPath());
        URL url4 = PathGettingUtils.getResourceFromClassPath(PathGettingUtilsTest.class, "ftp.properties");
        System.out.println(url4.getPath());
    }

    @Test
    public void testGetResourceFromClassPathBeginSlash() {
        URL url1 = PathGettingUtils.getResourceFromClassPathStartWithSlash("/com/wolf/utils");
        String path = url1.getPath();
        System.out.println(path);
        URL url2 = PathGettingUtils.getResourceFromClassPathStartWithSlash("/com/wolf/utils/");
        String path1 = url2.getPath();
        System.out.println(path1);
        URL url3 = PathGettingUtils.getResourceFromClassPathStartWithSlash("/com/wolf/utils/JsonUtils.class");
        System.out.println(url3.getPath());
        URL url4 = PathGettingUtils.getResourceFromClassPathStartWithSlash("/ftp.properties");
        System.out.println(url4.getPath());

        File file = new File(path);
        for (File f : file.listFiles()) {
            System.out.println(f.getAbsoluteFile());
        }

        File file2 = new File(path1);
        for (File f : file2.listFiles()) {
            System.out.println(f.getAbsoluteFile());
        }
    }

    @Test
    public void testTryGetPath() {
        File file = new File("com/wolf/test");//相对于当前文件所在最上层包violin路径，开始寻找
        System.out.println(file.getAbsoluteFile());
        File file2 = new File("/Users/xx/yy");//绝对路径直接寻找
        System.out.println(file2.getAbsoluteFile());

        System.out.println(System.getProperty("user.dir"));//当前目录所在文件上层的工作目录
        System.out.println( System.getProperty("java.class.path"));//classpath
    }

    @Test
    public void testGetPathDiff() {
        String basePath = PathGettingUtilsTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        System.out.println(basePath);
        System.out.println(PathGettingUtilsTest.class.getClassLoader().getResource("").getPath());
        System.out.println(PathGettingUtilsTest.class.getResource("/").getPath());

        System.out.println(PathGettingUtilsTest.class.getResource("").getPath());
    }



}
