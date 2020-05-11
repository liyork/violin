package com.wolf.service;

//CycleDependenceInjection与ServiceImpl2相互依赖，先使用CycleDependenceInjection则先加载
//整体流程：
//DefaultSingletonBeanRegistry
//  // Cache of singleton objects: bean name --> bean instance
//  singletonObjects
//  // Cache of singleton factories: bean name --> ObjectFactory
//  singletonFactories
//  // Cache of early singleton objects: bean name --> bean instance
//  earlySingletonObjects
//  // Set of registered singletons, containing the bean names in registration order
//  registeredSingletons
//  // Names of beans that are currently in creation
//  singletonsCurrentlyInCreation
//
//
//context.refresh
//  finishBeanFactoryInitialization
//    beanFactory.preInstantiateSingletons
//    for beanNames
//      getBean  --cycleDependenceInjection
//    	  doGetBean
//    	    getSingleton
//            beforeSingletonCreation
//              this.singletonsCurrentlyInCreation.add(beanName)  --标识当前单例正在创建
//      		    singletonFactory.getObject
//        		  createBean
//        		    doCreateBean
//        		      instanceWrapper = createBeanInstance
//          			    instantiateBean
//          				  beanInstance = getInstantiationStrategy().instantiate
//          				    BeanUtils.instantiateClass
//          					  ctor.newInstance
//          					    public CycleDependenceInjection()  --构造函数
//          				  bw = new BeanWrapperImpl(beanInstance)
//          				  initBeanWrapper
//          			  bean = instanceWrapper.getWrappedInstance
//                  earlySingletonExposure = (mbd.isSingleton() && this.allowCircularReferences && isSingletonCurrentlyInCreation(beanName))
//                  若earlySingletonExposure
//                    addSingletonFactory
//                      若!singletonObjects.containsKey(beanName)
//                        singletonFactories.put(beanName, singletonFactory)
//                        earlySingletonObjects.remove(beanName)  --注册早期的bean生产工厂
//                        registeredSingletons.add(beanName)
//                  populateBean  --填充属性，依赖注入
//                    applyPropertyValues
//                      valueResolver = new BeanDefinitionValueResolver
//                      for pvs.getPropertyValues()
//                        valueResolver.resolveValueIfNecessary  --serviceImpl2
//                          resolveReference
//                            beanFactory.getBean(serviceImpl2)  --又执行了createBean等动作
//                              ...
//                              public ServiceImpl2()  --构造函数
//                              populateBean
//                                applyPropertyValues  --同样设置属性，cycleDependenceInjection
//                                  beanFactory.getBean
//                                    doGetBean
//                                      sharedInstance = getSingleton  --Eagerly check singleton cache for manually registered singletons.
//                                        singletonObject = this.singletonObjects.get
//                                        若singletonObject == null && isSingletonCurrentlyInCreation(beanName)
//                                          singletonObject = this.earlySingletonObjects.get(beanName)
//                                          若singletonObject == null && allowEarlyReference
//                                            singletonFactory = this.singletonFactories.get(beanName)  --获取之前提前注册的bean工厂
//                                            若singletonFactory != null
//                                              singletonObject = singletonFactory.getObject()
//                                                getEarlyBeanReference
//                                                  若bean != null && !mbd.isSynthetic() && hasInstantiationAwareBeanPostProcessors()
//                                                    for getBeanPostProcessors
//                                                      org.springframework.context.annotation.ConfigurationClassPostProcessor$EnhancedConfigurationBeanPostProcessor 符合
//                                                      getEarlyBeanReference  直接返回bean
//                                                    最后返回exposedObject，就是参数bean，即构造cycleDependenceInjection时的bean，即匿名内部类中引用的bean
//                                              earlySingletonObjects.put(beanName, singletonObject)
//                                              singletonFactories.remove(beanName)
//                                      若sharedInstance != null && args == null
//                                        bean = getObjectForBeanInstance
//                              以上完成了ServiceImpl2中的CycleDependenceInjection属性注入，但是CycleDependenceInjection的属性serviceImpl2还是空
//                              registerDisposableBeanIfNecessary
//                            this.beanFactory.registerDependentBean(refName, this.beanName);
//                            bw.setPropertyValues  --设定CycleDependenceInjection的属性serviceImpl2