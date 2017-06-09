package com.wolf.test.base.classloadseq;

import com.alibaba.fastjson.JSON;

/**
 * Description:
 * <br/> Created on 2017/5/1 6:49
 *
 * @author 李超
 * @since 1.0.0
 */
public class ConstructorSeqRef {

    public ConstructorSeqRef(ConstructorSeqTest constructorSeqTest) {
        System.out.println("in ConstructorSeqRef.."+JSON.toJSONString(constructorSeqTest));
    }
}
