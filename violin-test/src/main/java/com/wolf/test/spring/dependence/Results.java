package com.wolf.test.spring.dependence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Description:
 * <br/> Created on 2018/6/7 18:46
 *
 * @author 李超
 * @since 1.0.0
 */
@Component
public class Results {

    @Autowired
    private List<Ranks> ranks ;

    @Override
    public String toString(){
        return ranks.toString();
    }
}
