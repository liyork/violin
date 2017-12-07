package com.wolf.schedule;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

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

    static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式

    //    @Scheduled(fixedRate = 5000)
//    @Scheduled(cron = "0/5 * * * * ? ")
    @Scheduled(cron = "0/10 10/10 8-20 * * ?")//从8点10分0秒开始到20点，每10分钟触发10秒一次的任务
    // (08:10:00、08:20:00---8:50:00,08:20:00、08:20:10--08:50:10)
    public void doSomething() {
        System.out.println(df.format(System.currentTimeMillis())+":qqqqqq");
    }
}
