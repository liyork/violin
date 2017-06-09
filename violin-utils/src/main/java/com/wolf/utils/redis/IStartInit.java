/**
 * Description: IStartInit.java
 * All Rights Reserved.
 *
 */
package com.wolf.utils.redis;

import javax.servlet.ServletContextEvent;

/**
 * Description: 启动初始化接口，此接口的实现类在启动时加载
 */
public interface IStartInit {

    /**
     * 执行初始化方法
     */
    void execute(ServletContextEvent context);


}
