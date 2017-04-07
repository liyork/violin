/**
 * Description: ApplicationContext.java
 * All Rights Reserved.
 *

 */
package com.wolf.company;

import com.wolf.company.server.StartNettyServer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Description: spring 上下文类
 */
@Component
public final class SpringApplicationContext implements ApplicationContextAware {

//	private static final Logger logger = Logger.getLogger(SpringApplicationContext.class);

    private static ApplicationContext ac = null;

    private static ApplicationContext mvcAc = null;

    private SpringApplicationContext() {
    }

    public static Object getBean(String id) {
        Object bean = null;
        try {
            bean = mvcAc.getBean(id);
        } catch (Exception e) {
            bean = ac.getBean(id);
        }
        return bean;
    }

    public static boolean isSingleton(String id) {
        boolean result;
        try {
            result = mvcAc.isSingleton(id);
        } catch (Exception e) {
            result = ac.isSingleton(id);
        }
        return result;
    }

    /**
     * 根据class对象返回IOC容器中其对象和其子类的对象。
     * 未找到则返回空MAP。
     * KEY为BEAN ID或者NAME，VALUE为BEAN实例
     */
    public static <T> Map<String, T> getBeansByType(Class<T> type) {
        try {
            return BeanFactoryUtils.beansOfTypeIncludingAncestors(mvcAc, type);
        } catch (Exception e) {
            return BeanFactoryUtils.beansOfTypeIncludingAncestors(ac, type);
        }
    }

    /**
     * 根据Bean类型,返回容器中所有该类型Bean的Key值Set集合,未找到则返回空的Set集合
     */
    public static Set<String> getBeanKeyByType(Class<?> type) {
        return SpringApplicationContext.getBeansByType(type).keySet();
    }

    /**
     * 启动时加载spring 上下文
     * Description:
     */
    public static void initApplicationContext(ApplicationContext ac) {

        SpringApplicationContext.ac = ac;

    }

    public static void initMvcApplicationContext(ApplicationContext mvcAc) {
        SpringApplicationContext.mvcAc = mvcAc;
    }

    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType) {
        Map<String, Object> map1 = ac.getBeansWithAnnotation(annotationType);
        Map<String, Object> map2 = mvcAc.getBeansWithAnnotation(annotationType);
        Map<String, Object> result = new HashMap<String, Object>(map1.size() + map2.size());
        result.putAll(map1);
        result.putAll(map2);
        return result;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringApplicationContext.ac = applicationContext;
        new StartNettyServer().execute(null);
    }
}
