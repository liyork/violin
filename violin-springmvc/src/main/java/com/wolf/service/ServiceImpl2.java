package com.wolf.service;

/**
 * Description:
 * <br/> Created on 2017/4/6 10:03
 *
 * @author 李超
 * @since 1.0.0
 */
//@Component
public class ServiceImpl2  {

//    @Autowired
    private CycleDependenceInjection cycleDependenceInjection;

    public ServiceImpl2() {
        System.out.println("xxx ServiceImpl2==>"+cycleDependenceInjection);//xxx==>null
    }

    public String test(String xxx) {
        return "1111test ServiceImpl2...." + xxx;
    }

    public CycleDependenceInjection getCycleDependenceInjection() {
        return cycleDependenceInjection;
    }

    public void setCycleDependenceInjection(CycleDependenceInjection cycleDependenceInjection) {
        this.cycleDependenceInjection = cycleDependenceInjection;
    }
}
