package com.wolf.test.base;

import com.wolf.utils.PathGettingUtils;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;

/**
 * Description: path各种获取方式测试
 * <br/> Created on 2016/12/30 1:01
 *
 * @author 李超()
 * @since 1.0.0
 */
public class PathGettingUtilsTest {

    //运行命令，java -classpath "xxx:yyy:zzz" com.intellij.rt.execution.junit.JUnitStarter -ideVersion5 -junit4 com.wolf.test.base.PathGettingUtilsTest,testGetClassPath
    //classpath中有设定/Users/lichao30/intellij_workspace/violin/violin-test/target/classes
    //总结：
    // 对类加载器调用getResource("")，执行的是PathGettingUtils.class，类加载器根就是PathGettingUtils所在classpath
    // 对类加载器调用getResource("/")，null
    // 对类调用getResource("")，类所在的目录
    // 对类加载器调用getResource("/")，获取当前类所在classpath的根

    //PathGettingUtils中PathGettingUtils.class.getClassLoader().getResource("");
    //file:/Users/lichao30/intellij_workspace/violin/violin-utils/target/classes/ 看来是对哪个类启动就是哪个作为加载器的根
    @Test
    public void testRootPath() {
        URL url1 = PathGettingUtils.class.getClassLoader().getResource("");
        URL url2 = PathGettingUtils.class.getClassLoader().getResource("/");
        URL url3 = PathGettingUtils.class.getResource("");
        URL url4 = PathGettingUtils.class.getResource("/");
        System.out.println(url1);
        System.out.println(url2);
        System.out.println(url3);
        System.out.println(url4);

        String basePath = PathGettingUtilsTest.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        System.out.println(basePath);
    }

    //使用classloader在指定的classpath下查找，不能以/开始，本身就是根开始
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

    //获取结果与上面一样。只不过用的/开始，即classpath的根开始
    @Test
    public void testGetResourceFromClassPathBeginSlash() {
        URL url1 = PathGettingUtils.getResourceFromClassPathStartWithSlash("/com/wolf/utils");
        String path = url1.getPath();
        System.out.println(path);
        URL url2 = PathGettingUtils.getResourceFromClassPathStartWithSlash("/com/wolf/utils/");//仅仅一个/而已，是一样的
        String path1 = url2.getPath();
        System.out.println(path1);
        URL url3 = PathGettingUtils.getResourceFromClassPathStartWithSlash("/com/wolf/utils/JsonUtils.class");
        System.out.println(url3.getPath());
        URL url4 = PathGettingUtils.getResourceFromClassPathStartWithSlash("/ftp.properties");
        System.out.println(url4.getPath());

        //使用class寻找，则需要用/开始，即从根开始查找
//        URL url5 = PathGettingUtils.getResourceFromClassPathStartWithSlash("com/wolf/utils/JsonUtils.class");
//        String path5 = url5.getPath();
//        System.out.println(path5);

        //相对路径查找
        URL url6 = PathGettingUtils.getResourceFromClassPathStartWithSlash("JsonUtils.class");
        System.out.println(url6.getPath());
    }

    @Test
    public void testGetFile() {
        File file1 = new File("");//当前类所在路径
        System.out.println(file1.getAbsoluteFile());
        File file = new File("com/wolf/test");//相对于当前文件+com/wolf/test，开始寻找
        System.out.println(file.getAbsoluteFile());
        File file2 = new File("/Users/xx/yy");//绝对路径直接寻找
        System.out.println(file2.getAbsoluteFile());

        System.out.println(System.getProperty("user.dir"));//当前目录所在文件
        System.out.println(System.getProperty("java.class.path"));//classpath
    }

    //测试getResources方法，以及是否可以读取jar中的文件。
    //对于classpath中设定的目录下则只能识别class或文件夹中文件，不能识别jar，若需要加载jar则需要单独设定classpath的jar路径
    //所以要是工程中打包classpath中有文件，而且jar中也有此文件就排除jar了吧。
    @Test
    public void testGetResourceIfLoadJar() throws IOException {

        //classpath中有/Users/lichao30/intellij_workspace/violin/violin-utils/target所以可以获取。
        Enumeration<URL> resources = getClass().getClassLoader().getResources("ftp.properties");
        //把ftp.properties放入/Users/lichao30/intellij_workspace/violin/violin-test/target/classes/中后可以得到多个
        //把violin-utils-1.0-SNAPSHOT.jar也添加到/Users/lichao30/intellij_workspace/violin/violin-test/target/classes/中后也可以读取
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            System.out.println(url);
        }
    }

    //问题：这里执行就是file:/Users/lichao30/intellij_workspace/violin/violin-test/target/classes/，而在PathGettingUtils中执行main，
    //就是file:/Users/lichao30/intellij_workspace/violin/violin-utils/target/classes/，开始时怀疑是java PathGettingUtils.class
    // 根据此class反推寻找那个类所在classpath的root
    //解决：后来跟踪代码，发现PathGettingUtils.class.getClassLoader()得到的是sun.misc.Launcher$AppClassLoader而AppClassLoader extends URLClassLoader，
    //而AppClassLoader内部有URLClassPath ucp。
    // 当getResource查找类是委托机制，由于父类都没有class所以还是由Launcher.AppClassLoader负责，
    //交给URLClassPath ucp查找，URLClassPath.findResource，里面有loaders包含所有classpath，然后依次寻找，直到找到第一个
    //所以当我们修改classpath中内容，所以返回就不一样了，而开头问题的不同结果了。。
    public static void main(String[] args) {
        URL url1 = PathGettingUtils.class.getClassLoader().getResource("");
        System.out.println(url1);
    }
}
