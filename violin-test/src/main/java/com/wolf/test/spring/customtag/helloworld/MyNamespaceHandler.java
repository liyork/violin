package com.wolf.test.spring.customtag.helloworld;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Description:
 * <br/> Created on 2018/6/28 17:14
 *
 * @author 李超
 * @since 1.0.0
 */
public class MyNamespaceHandler extends NamespaceHandlerSupport {

    public void init() {
        registerBeanDefinitionParser("dateformat", new SimpleDateFormatBeanDefinitionParser());
    }

}
