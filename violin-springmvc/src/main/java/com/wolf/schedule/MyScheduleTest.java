package com.wolf.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Description:
 * 需要配置xml中
 * xsi:schemaLocation里的
 * http://www.springframework.org/schema/task
 * http://www.springframework.org/schema/task/spring-task-3.0.xsd
 * 和
 * xmlns:task="http://www.springframework.org/schema/task"
 * <br/> Created on 2017/6/16 14:23
 *
 * @author 李超
 * @since 1.0.0
 */
@Component
public class MyScheduleTest {

    //    @Scheduled(fixedRate = 5000)
    @Scheduled(cron = "0/5 * * * * ? ")
    public void doSomething() {
        System.out.println("xxxx");
    }
}
