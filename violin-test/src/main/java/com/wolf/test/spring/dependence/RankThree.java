package com.wolf.test.spring.dependence;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Description:
 * <br/> Created on 2018/6/7 18:46
 *
 * @author 李超
 * @since 1.0.0
 */
@Component
@Order(3)
public class RankThree implements Ranks{

    private String rank = "RankThree";

    public String toString(){
        return this.rank;
    }
}