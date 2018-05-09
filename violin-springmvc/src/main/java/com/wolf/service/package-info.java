package com.wolf.service;

//CycleDependenceInjection与ServiceImpl2相互依赖，先使用CycleDependenceInjection则先加载
//整体流程：
//beanFactory.preInstantiateSingletons()
//        doCreateBean  --CycleDependenceInjection
//        addSingletonFactory
//        populateBean
//        AutowiredAnnotationBeanPostProcessor.postProcessPropertyValues
//        beanFactory.resolveDependency
//        doCreateBean  -- ServiceImpl2
//        addSingletonFactory
//        populateBean
//        AutowiredAnnotationBeanPostProcessor.postProcessPropertyValues
//        beanFactory.resolveDependency
//        field.set(bean, value)   -- ServiceImpl2有CycleDependenceInjection,CycleDependenceInjection中ServiceImpl2=null
//        field.set(bean, value);  -- CycleDependenceInjection设定ServiceImpl2,那么由于是引用，两者都有了