package com.wolf.test.generics;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Description:额外保存参数类型
 * <br/> Created on 05/09/2018 9:26 AM
 *
 * @author 李超
 * @since 1.0.0
 */
public abstract class AbsClass<T> {
    protected final Type _type;

    public AbsClass() {
        Type superClass = getClass().getGenericSuperclass();
        _type = ((ParameterizedType) superClass).getActualTypeArguments()[0];
    }

    public Type getParameterizeType() {
        return _type;
    }
}

class ParaClass extends AbsClass<Long> {
}

class Test {
    public static void main(String[] args) throws NoSuchFieldException {
        ParaClass paraClass = new ParaClass();
        System.out.println(paraClass.getParameterizeType());
    }
}