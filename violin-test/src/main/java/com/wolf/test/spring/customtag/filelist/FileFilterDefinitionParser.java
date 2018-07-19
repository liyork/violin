package com.wolf.test.spring.customtag.filelist;

import org.apache.commons.io.filefilter.NameFileFilter;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Description:
 * <br/> Created on 2018/6/28 17:52
 *
 * @author 李超
 * @since 1.0.0
 */
public class FileFilterDefinitionParser extends AbstractSingleBeanDefinitionParser {

    /**
     * The bean that is created for this tag element
     *
     * @param element The tag element
     * @return A FileFilterFactoryBean
     */
    @Override
    protected Class<?> getBeanClass(Element element) {
        return FileFilterFactoryBean.class;
    }

    /**
     * Called when the fileFilter tag is to be parsed
     *
     * @param element The tag element
     * @param ctx     The context in which the parsing is occuring
     * @param builder The bean definitions build to use
     */
    @Override
    protected void doParse(Element element, ParserContext ctx, BeanDefinitionBuilder builder) {

        // Set the scope
        builder.setScope(element.getAttribute("scope"));

        try {
            // All of the filters will eventually end up in this list
            // We use a 'ManagedList' and not a regular list because anything
            // placed in a ManagedList object will support all of Springs
            // functionalities and scopes for us, we dont' have to code anything
            // in terms of reference lookups, EL, etc
            ManagedList<Object> filters = new ManagedList<Object>();

            // For each child node of the fileFilter tag, parse it and place it
            // in the filtes list
            NodeList nl = element.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                DefinitionParserUtil.parseLimitedList(filters, nl.item(i), ctx, builder.getBeanDefinition(), element.getAttribute("scope"));
            }

            // Add the filtes to the list of properties (this is applied
            // to the factory beans setFilters below)
            builder.addPropertyValue("filters", filters);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
