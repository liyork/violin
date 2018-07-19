package com.wolf.test.spring.customtag.filelist;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * Description:
 * 流程：
 * 构造beandefinition“：FileListDefinitionParser解析<corecommons:fileList 然后调用FileFilterDefinitionParser解析<corecommons:fileFilter，设定值
 * 初始化FileListFactoryBean，进行过滤名称
 * <br/> Created on 2018/6/28 17:51
 *
 * @author 李超
 * @since 1.0.0
 */
public class CoreNamespaceHandler extends NamespaceHandlerSupport {

    @Override
    public void init() {
        this.registerBeanDefinitionParser("fileList", new FileListDefinitionParser());
        //只会解析beans下的一层标签，子标签不会解析，下面暂时不会被spring用到，而是在FileListDefinitionParser中手动触发
        this.registerBeanDefinitionParser("fileFilter", new FileFilterDefinitionParser());
    }
}
