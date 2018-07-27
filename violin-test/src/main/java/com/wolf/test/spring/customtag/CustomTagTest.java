package com.wolf.test.spring.customtag;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Description:测试spring下自定义标签并注册解析器
 * <p>
 * spring启东时加载xml，使用相应的namespacehandler进行加载相关标签并初始化beandefinition，如果对于他不认识的标签会delegate.parseCustomElement
 * ，加载命名空间处理器，这个处理器是从META-INF/spring.handlers中加载来的，调用其init方法，这时我们就进行注册parser，当需要parse时就会使用我们
 * 刚才注册的解析指定bean，然后我们就会注册beandefinition然后应用中就可以getbean获取到了。算是spring给我们开的口子，还算是使用者对代码的研究深入啊。
 *
 * <br/> Created on 2018/6/28 12:16
 *
 * @author 李超
 * @since 1.0.0
 */
public class CustomTagTest {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("customtag.xml");
        Object flow = context.getBean("flow");
        StationRoutingWrap xxabc1 = (StationRoutingWrap) flow;
        System.out.println(xxabc1);

        Object stop = context.getBean("stop");
        StationRoutingWrap xxabc2 = (StationRoutingWrap) stop;
        System.out.println(xxabc2);

        SimpleDateFormat format = (SimpleDateFormat) context.getBean("defaultDateFormat");
        System.out.println("format:" + format.format(new Date()));

        List<File> fileList = (List<File>) context.getBean("xmlList");
        for (File file : fileList) {
            System.out.println(file.getName());
        }
    }
}
