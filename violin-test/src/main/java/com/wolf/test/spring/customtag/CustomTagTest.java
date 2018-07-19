package com.wolf.test.spring.customtag;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Description:
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
