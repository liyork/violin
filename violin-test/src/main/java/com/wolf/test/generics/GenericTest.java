package com.wolf.test.generics;

import com.wolf.test.entity.Person;
import com.wolf.test.entity.Room;
import com.wolf.test.generics.entity.Children;
import com.wolf.test.generics.entity.SubChildren;
import org.junit.Test;
import sun.reflect.generics.reflectiveObjects.TypeVariableImpl;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * <p> Description:泛型测试
 * 泛型参数是动态的???
 * 泛型的本质是参数化类型，通常用于输入参数、存储类型不确定的场景。相比于直接使用 Object 的好处是：
 * 编译期强类型检查、无需进行显式类型转换。
 *
 * Java 中的泛型是在编译器这个层次实现的，在生成的Java字节代码中是不包含泛型中的类型信息的
 *
 * 擦除规则：字节码中类型会被擦除到上限
 * List<String>、List<T> 擦除后的类型为 List。
 * List<String>[]、List<T>[] 擦除后的类型为 List[]。
 * List<? extends E>、List<? super E> 擦除后的类型为 List<E>。
 * List<T extends Serialzable & Cloneable> 擦除后类型为 List<Serializable>。
 * <p>
 * Java 为什么这么处理呢？有以下两个原因：
 * 避免 JVM 的大换血。如果 JVM 将泛型类型延续到运行期，那么到运行期时 JVM 就需要进行大量的重构工作了，提高了运行期的效率。
 * 版本兼容。 在编译期擦除可以更好地支持原生类型（Raw Type）
 *
 * signature 属性
 * Java泛型的擦除并不是对所有使用泛型的地方都会擦除的，部分地方会保留泛型信息。
 *
 * <p/>
 * Date: 2015/7/21
 * Time: 8:44
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class GenericTest {

    @Test
    public void testBasicUse() throws NoSuchMethodException {
        CustomizeClass<Person> customizeClass = new CustomizeClass<Person>();
        ParameterizedType genericInterface = (ParameterizedType) customizeClass.getClass().getGenericInterfaces()[0];
        Type[] actualTypeArguments = genericInterface.getActualTypeArguments();
        System.out.println(actualTypeArguments[0]);
        System.out.println(genericInterface.getOwnerType());
        System.out.println(genericInterface.getRawType());
        ParameterizedType superclass = (ParameterizedType) customizeClass.getClass().getGenericSuperclass();
        Type[] actualTypeArguments1 = superclass.getActualTypeArguments();
        for (Type type : actualTypeArguments1) {
            //都是Class。应该是有哪个属性标识是接口还是类。还有其他类型吗？还有ParameterizedType类型的可能
            System.out.println((Class)type);
        }
        System.out.println(superclass.getOwnerType());
        System.out.println(superclass.getRawType());

        Method setEntity = customizeClass.getClass().getMethod("setEntity", Object.class);
        TypeVariableImpl type = (TypeVariableImpl) setEntity.getGenericParameterTypes()[0];
        System.out.println(type.getName());
        System.out.println(Arrays.toString(type.getBounds()));
        System.out.println(type.getGenericDeclaration());

        Method setEntity1 = customizeClass.getClass().getMethod("setEntity1", Person.class);
        Class type1 = (Class) setEntity1.getGenericParameterTypes()[0];
        System.out.println(type1.getName());
    }

    @Test
    public void testBasicGenericClass() {
        BasicGenericClass<Person, Room> basicGenericClass = new BasicGenericClass<Person,Room>();
        Person person1 = new Person(111, "xx");
        basicGenericClass.setT(person1);
        Person x = basicGenericClass.getX();
        System.out.println(x.getId());
        BasicGenericClass<Children,Room> basicGenericClass1 = new BasicGenericClass<Children,Room>();
        //成功编译过后的class文件中是不包含任何泛型信息的。泛型信息不会进入到运行时阶段。
        System.out.println(basicGenericClass.getClass() == basicGenericClass1.getClass());
    }

    @Test
    public void testUpperAndSuperLimit() {
//		UpperAndSuperLimit upperLimit1 = new UpperAndSuperLimit<Person>();//报错
        UpperAndSuperLimit upperLimit2 = new UpperAndSuperLimit<Children>();
        UpperAndSuperLimit upperLimit3 = new UpperAndSuperLimit<SubChildren>();

//		UpperLimit.down(new ArrayList<SubChildren>());//报错
        ArrayList<Children> list = new ArrayList<>();
        list.add(new Children(1, "x1"));
        list.add(new Children(2, "x2"));
        UpperAndSuperLimit.down(list);
        UpperAndSuperLimit.down(new ArrayList<Person>());

//		UpperLimit.up(new ArrayList<Person>());//报错
        UpperAndSuperLimit.up(list);
        UpperAndSuperLimit.up(new ArrayList<SubChildren>());


        Children children = UpperAndSuperLimit.testGenericMethod1(list);
        System.out.println(children);
        Object o = new UpperAndSuperLimit().testGenericMethod2(list);
        System.out.println(o);

        Object o1 = UpperAndSuperLimit.ifThenElse(false, 1, 2);
        System.out.println(o1);
        Object o2 = UpperAndSuperLimit.ifThenElse(false, "1", 3.14d);
        System.out.println(o2);
        Object o3 = UpperAndSuperLimit.ifThenElse(false, new SubChildren(1, "2"), 3.14d);
        System.out.println(o3);

        CustomizeClass<Person> customizeClass = new CustomizeClass<Person>();
        Person person = new Person(1, "xx");
        customizeClass.setEntity(person);
        Person entity = customizeClass.getEntity();
        CustomizeClass<Children> customizeClass2 = new CustomizeClass<Children>();
        customizeClass2.setEntity(new Children(2, "yy"));
        boolean compare = UpperAndSuperLimit.compare(customizeClass, customizeClass2);
        System.out.println(compare);
    }

    //测试使用泛型导致的参数被两次Object[]
    @Test
    public void testDynamicParamMethod() {
        Object[] arg = new Object[2];
        arg[0] = 1l;
        arg[1] = 0;
        GenericTest.dynamicParamMethod("a1", "a2", arg);
        GenericTest.dynamicParamMethod("a1", "a2", "a3", "a4");

        GenericXImpl generic = new GenericXImpl();
        Object[] key = generic.getKey();
        System.out.println(key);
        GenericTest.dynamicParamMethod("a1", "a4", key);

        generic.test();
        generic.test2();
    }

    private static void dynamicParamMethod(String a, String b, Object... arg) {
        System.out.println("a:" + a + " " + arg[0]);
    }

    abstract class GenericX<K> {
        abstract K getKey();

        public void test() {
            K key = getKey();
            GenericTest.dynamicParamMethod("a1", "a2", key);

            //编译后的结果。。如果方法是可变参数，则new Object[] { }
//            Object key = getKey();
//            JavaBaseTest.testDynamicParamMethod("a1", "a2", new Object[] { key });
        }

        public void test2() {
            K key = getKey();
            GenericTest.dynamicParamMethod("a1", "a2", (Object[]) key);

            //编译后的结果。。如果方法是可变参数，且有强制转换，则不使用new Object[] {}
//            Object key = getKey();
//            GenericTest.dynamicParamMethod("a1", "a2", (Object[])key);
        }
    }

    class GenericXImpl extends GenericX<Object[]> {

        @Override
        Object[] getKey() {
            Object[] arg = new Object[2];
            arg[0] = 1l;
            arg[1] = 0;
            return arg;
        }

    }

    @Test
    public void testTypeRef() throws NoSuchMethodException {
        CustomizeClass<Person> customizeClass = new CustomizeClass<Person>();

//        customizeClass.testxx();
        customizeClass.testxx1();

    }

}

