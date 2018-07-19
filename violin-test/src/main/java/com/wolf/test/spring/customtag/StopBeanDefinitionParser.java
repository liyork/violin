package com.wolf.test.spring.customtag;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Description:
 * <br/> Created on 2018/6/28 12:13
 *
 * @author 李超
 * @since 1.0.0
 */
public class StopBeanDefinitionParser implements BeanDefinitionParser {
    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        RootBeanDefinition nodeWrapDefinition = new RootBeanDefinition();
        //该BeanDefinition对应的是什么类
        nodeWrapDefinition.setBeanClass(StationRoutingWrap.class);

        String id = element.getAttribute("id");
        //设定属性
        nodeWrapDefinition.getPropertyValues().addPropertyValue("id", id);

        parserContext.getRegistry().registerBeanDefinition("stop", nodeWrapDefinition);

        return nodeWrapDefinition;
    }
}
