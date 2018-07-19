package com.wolf.test.spring.customtag;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Description:
 * <br/> Created on 2018/6/28 12:11
 *
 * @author 李超
 * @since 1.0.0
 */
public class MyNamespaceHandlerSupport extends NamespaceHandlerSupport {
    public void init() {
        //解析标签：<bf:head-routing 和<bf:stop
        registerBeanDefinitionParser("head-routing", new BusinessFlowBeanDefinitionParser());
        registerBeanDefinitionParser("stop", new StopBeanDefinitionParser());
    }
}