package com.wolf.service;

/**
 * Description:循环依赖注入
 * <br/> Created on 2017/10/17 18:12
 * debug:beanName.equals("serviceImpl2") || beanName.equals("cycleDependenceInjection")
 *
 * 先实例化CycleDependenceInjection，再填充属性，看到依赖ServiceImpl2则加载ServiceImpl2，看到ServiceImpl2依赖CycleDependenceInjection，
 * (由于之前已加载过CycleDependenceInjection只不过serviceImpl2=null)，设置进去CycleDependenceInjection，
 * 完成ServiceImpl2的bean的创建，递归出来，就可以给CycleDependenceInjection设定依赖ServiceImpl2就全了。
 *
 * 使用earlySingletonObjects处理serviceImpl2依赖CycleDependenceInjection时，先设一个值，稍后补充
 * @author 李超
 * @since 1.0.0
 */
//@Component   //todo 为了控制顺序，先放入xml配置中。
public class CycleDependenceInjection {

//    @Autowired
    private ServiceImpl2 serviceImpl2;

    int b;

    public CycleDependenceInjection(){
        System.out.println("xxx CycleDependenceInjection==>"+serviceImpl2);//xxx==>null
        b = 1;
    }

    public void test(){
        System.out.println("test CycleDependenceInjection..."+serviceImpl2);//这里就有了
        serviceImpl2.test("xx1231x");
    }

    public ServiceImpl2 getServiceImpl2() {
        return serviceImpl2;
    }

    public void setServiceImpl2(ServiceImpl2 serviceImpl2) {
        this.serviceImpl2 = serviceImpl2;
    }
}
