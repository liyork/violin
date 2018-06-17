package com.wolf.test.spring.init;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;

/**
 * Description:测试spring加载bean的各种方法顺序
 * Constructor > setxx(populateBean) > setApplicationContext,@PostConstruct(postProcessBeforeInitialization) > InitializingBean(afterPropertiesSet) > init-method(invokeCustomInitMethod) > postProcessAfterInitialization
 * BeanPostProcessor的postProcessBeforeInitialization是在Bean生命周期中initialbean(afterPropertiesSet和init-method)之前执被调用的
 * <br/> Created on 2018/5/8 9:37
 *
 * @author 李超
 * @since 1.0.0
 */
public class SpringInitSequenceBean implements InitializingBean,ApplicationContextAware {

//    @Autowired //有此注解则spring不会调用setxx方法，应该是通过反射
    SpringInitSequenceBeanXX springInitSequenceBeanXX;

    public SpringInitSequenceBean() {
        System.out.println("SpringInitSequenceBean: constructor");
    }

    public void setSpringInitSequenceBeanXX(SpringInitSequenceBeanXX springInitSequenceBeanXX) {
        this.springInitSequenceBeanXX = springInitSequenceBeanXX;
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("SpringInitSequenceBean: postConstruct");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("SpringInitSequenceBean: afterPropertiesSet");
        springInitSequenceBeanXX.hashCode();
    }

    public void initMethod() {
        System.out.println("SpringInitSequenceBean: init-method");
    }


//    @Override
//    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
//        System.out.println("调用postProcessBeanFactory()...");
//    }

//    @Override
//    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
//        if (beanName.equals("springInitSequenceBean")) {
//            System.out.println("调用postProcessBeforeInitialization()...");
//        }
//        return bean;
//    }
//
//    @Override
//    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
//        if (beanName.equals("springInitSequenceBean")) {
//            System.out.println("调用postProcessAfterInitialization()...");
//        }
//        return bean;
//    }


    public SpringInitSequenceBeanXX getSpringInitSequenceBeanXX() {
        return springInitSequenceBeanXX;
    }


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        System.out.println("setApplicationContext...");
    }
}