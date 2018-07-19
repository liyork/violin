package com.wolf.test.spring.customtag;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Description:spring加载xml时遇到非核心的标签则代理给扩展的parser解析
 * <br/> Created on 2018/6/28 12:13
 *
 * @author 李超
 * @since 1.0.0
 */
public class BusinessFlowBeanDefinitionParser implements BeanDefinitionParser {
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        RootBeanDefinition nodeWrapDefinition = new RootBeanDefinition();
        //该BeanDefinition对应的是什么类
        nodeWrapDefinition.setBeanClass(StationRoutingWrap.class);

        String key = element.getAttribute("key");
        //设定属性
        nodeWrapDefinition.getPropertyValues().addPropertyValue("name", key);

        parserContext.getRegistry().registerBeanDefinition("flow", nodeWrapDefinition);

        return nodeWrapDefinition;
    }
}
