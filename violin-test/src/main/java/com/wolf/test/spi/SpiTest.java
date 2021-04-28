package com.wolf.test.spi;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Description:
 * SPI:Service Provider Interface，是Java提供的，一套用来被第三方实现或者扩展，的接口，可以用来启用框架扩展和替换组件。
 * 作用就是为这些被扩展的API寻找服务实现。
 * Created on 2021/4/20 10:53 AM
 *
 * @author 李超
 * @version 0.0.1
 */
public class SpiTest {
    public static void main(String[] args) {
        ServiceLoader<TestIface> load = ServiceLoader.load(TestIface.class);
        List<TestIface> out = new ArrayList<>();
        for (TestIface testIface : load) {
            System.out.println(testIface);
            out.add(testIface);
        }

        // 不是一个对象
        ServiceLoader<TestIface> load1 = ServiceLoader.load(TestIface.class);
        for (TestIface testIface : load1) {
            System.out.println(testIface);
            System.out.println(out.contains(testIface));
        }

        // 再打印一遍，有缓存providers
        for (TestIface testIface : load) {
            System.out.println(testIface);
            out.add(testIface);
        }
    }
}
