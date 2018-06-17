package com.wolf.test.spring.dependence;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Description:
 * <br/> Created on 2018/6/7 18:47
 *
 * @author 李超
 * @since 1.0.0
 */
@Component
@Order(2)
public class RankTwo implements Ranks{

    private String rank = "RankTwo";

    public String toString(){
        return this.rank;
    }
}
