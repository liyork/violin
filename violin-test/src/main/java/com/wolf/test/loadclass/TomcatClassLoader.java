package com.wolf.test.loadclass;

/**
 * Description:
 * <br/> Created on 19/06/2018 10:22 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public class TomcatClassLoader {

    //层次关系
    //bootstrap(tomcat自定义，加载java_home/jre/lib/*)
    //system(加载CATALINA_HOME/bin/*)
    //common(加载CATALINA_HOME/lib/*)
    //每个应用各自的classloader(加载各自的WEB-INF/classes和WEB-INF/lib)

    //加载顺序
    //bootstrap、system、WEB-INF/classes、WEB-INF/lib、common
    //利用这个特性可以将想修改的源码放入src中，肯定优先于WEB-INF/lib和common中的class被加载。
}
