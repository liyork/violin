package com.wolf.test.spring.customtag.filelist;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.ManagedList;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.xml.DomUtils;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.FileFilter;
import java.util.*;

/**
 * Description:
 * <br/> Created on 2018/6/28 17:51
 *
 * @author 李超
 * @since 1.0.0
 */
public class FileListDefinitionParser extends AbstractSingleBeanDefinitionParser {

    /**
     * The bean that is created for this tag element
     *
     * @param element The tag element
     * @return A FileListFactoryBean
     */
    @Override
    protected Class<?> getBeanClass(Element element) {
        return FileListFactoryBean.class;
    }

    /**
     * Called when the fileList tag is to be parsed
     *
     * @param element The tag element
     * @param ctx     The context in which the parsing is occuring
     * @param builder The bean definitions build to use
     */
    @Override
    protected void doParse(Element element, ParserContext ctx, BeanDefinitionBuilder builder) {
        // Set the directory property
        builder.addPropertyValue("directory", element.getAttribute("directory"));

        // Set the scope
        builder.setScope(element.getAttribute("scope"));

        // We want any parsing to occur as a child of this tag so we need to make
        // a new one that has this as it's owner/parent
        ParserContext nestedCtx = new ParserContext(ctx.getReaderContext(), ctx.getDelegate(), builder.getBeanDefinition());

        // Support for filters
        Element exclusionElem = DomUtils.getChildElementByTagName(element, "fileFilter");
        if (exclusionElem != null) {
            // Just make a new Parser for each one and let the parser do the work
            FileFilterDefinitionParser ff = new FileFilterDefinitionParser();
            builder.addPropertyValue("filters", ff.parse(exclusionElem, nestedCtx));
        }

        // Support for nested fileList
        List<Element> fileLists = DomUtils.getChildElementsByTagName(element, "fileList");
        // Any objects that created will be placed in a ManagedList
        // so Spring does the bulk of the resolution work for us
        ManagedList<Object> nestedFiles = new ManagedList<Object>();
        if (fileLists.size() > 0) {
            // Just make a new Parser for each one and let them do the work
            FileListDefinitionParser fldp = new FileListDefinitionParser();
            for (Element fileListElem : fileLists) {
                nestedFiles.add(fldp.parse(fileListElem, nestedCtx));
            }
        }

        // Support for other tags that return File (value will be converted to file)
        try {
            // Go through any other tags we may find.  This does not mean we support
            // any tag, we support only what parseLimitedList will process
            NodeList nl = element.getChildNodes();
            for (int i = 0; i < nl.getLength(); i++) {
                // Parse each child tag we find in the correct scope but we
                // won't support custom tags at this point as it coudl destablize things
                DefinitionParserUtil.parseLimitedList(nestedFiles, nl.item(i), ctx,
                        builder.getBeanDefinition(), element.getAttribute("scope"), false);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Set the nestedFiles in the properties so it is set on the FactoryBean
        builder.addPropertyValue("nestedFiles", nestedFiles);

    }

}