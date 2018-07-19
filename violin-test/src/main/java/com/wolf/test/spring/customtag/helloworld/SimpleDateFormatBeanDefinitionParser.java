package com.wolf.test.spring.customtag.helloworld;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import java.text.SimpleDateFormat;

/**
 * Description:构造一个SimpleDateFormat对象
 * create a single bean definition
 * AbstractSingleBeanDefinitionParser 我们解析后他自动注册到工厂
 * 内部使用id作为name
 * <br/> Created on 2018/6/28 17:15
 *
 * @author 李超
 * @since 1.0.0
 */
public class SimpleDateFormatBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    protected Class<SimpleDateFormat> getBeanClass(Element element) {
        return SimpleDateFormat.class;
    }

    @SuppressWarnings("deprecation")
    protected void doParse(Element element, BeanDefinitionBuilder bean) {
        // this will never be null since the schema explicitly requires that a value be supplied
        String pattern = element.getAttribute("pattern");
        bean.addConstructorArg(pattern);

        // this however is an optional property
        String lenient = element.getAttribute("lenient");
        if (StringUtils.hasText(lenient)) {
            bean.addPropertyValue("lenient", Boolean.valueOf(lenient));
        }
    }
}
