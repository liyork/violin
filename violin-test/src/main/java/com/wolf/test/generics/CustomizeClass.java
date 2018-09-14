package com.wolf.test.generics;

import com.alibaba.fastjson.JSON;
import com.wolf.test.entity.Room;
import com.wolf.test.generics.entity.Children;
import com.wolf.test.entity.Person;

import java.util.Comparator;

/**
 * <p> Description:泛型类
 * 定义在类上泛型，在方法上的泛型
 * 泛型的本质是参数化类型，也就是说所操作的数据类型被指定为一个参数
 * 提高重用性，当CustomizeClass放入person或是children都可以，不用定义两个类
 * 编译之前检查的
 * note:泛型类，只能用于非静态方法
 * 泛型方法如果单独定义类型则用于静态方法,如果用类的泛型则是实例方法
 * <p>
 * 泛型会在编译期被擦除，所以List<String>和List<Integer>是一样的。但是运行时可以获取相关泛型信息，应该是被保留在某些地方了
 * 能否获得想要的类型可以在command中用 javap -v xx.class  来查看泛型签名来找到线索
 * cd /Users/chaoli/intellijWrkSpace/violin/violin-test/target/classes/com/wolf/test/generics
 * javap -v CustomizeClass.class
 * 可以看到最后一行
 * Signature: #61  // <T:Ljava/lang/Object;>Lcom/wolf/test/generics/BasicGenericClass<Lcom/wolf/test/entity/Person;Lcom/wolf/test/entity/Room;>;Ljava/util/Comparator<Lcom/wolf/test/generics/entity/Children;>;
 * 表明是可以获取泛型类型的。
 * 若是javap -v BasicGenericClass.class
 * Signature: #61   // <T:Ljava/lang/Object;Q:Ljava/lang/Object;>Ljava/lang/Object;
 * <p/>
 * Date: 2015/7/21
 * Time: 8:44
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class CustomizeClass<T> extends BasicGenericClass<Person, Room> implements Comparator<Children> {

    private T entity;

    public void setEntity(T entity) {
        this.entity = entity;
    }

    public void setEntity1(Person entity) {
    }

    public T getEntity() {
        return entity;
    }

    //泛型不能应用都在静态方法或字段上(泛型类中的泛型参数的实例化是在定义对象的时候指定的)
//	public static T getxx(){
//	}

    //类型擦除后变成equals(Object object)与object的equals冲突
//	public void equals(T t){
//	}

    @Override
    public int compare(Children o1, Children o2) {
        return o1.getId() - o2.getId();
    }

    public void testxx() {
        Person person = new Person(1, "aaaa");
        String s = JSON.toJSONString(person);
        super.testTypeRefError(s);
    }

    public void testxx1() {
        Person person = new Person(1, "aaaa");
        String s = JSON.toJSONString(person);
        try {
            super.testTypeRefCorrect(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}






