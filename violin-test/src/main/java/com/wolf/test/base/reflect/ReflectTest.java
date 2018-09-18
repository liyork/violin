package com.wolf.test.base.reflect;

import com.wolf.test.base.InnerClassAndStaticTest;
import com.wolf.utils.ArrayUtils;
import com.wolf.utils.ReflectUtils;
import org.junit.Test;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.Collection;

/**
 * Description:没有反射就没有框架
 * Person person = new Person()
 * Class clazz = person.getClass()
 * person是类Person的实例对象。类Person是java.lang.Class的实例对象,Class是Class Type。多个person对应的Class是一个
 * Class是Person类的类类型
 * <br/> Created on 2016/10/27 15:08
 *
 * @author 李超()
 * @since 1.0.0
 */
public class ReflectTest {

    public static void main(String[] args) throws Exception {
//        testDynamicCreateBean();
//        test();

        new ReflectTest().testStackTraceElement();
    }

    @Test
    public void testGetClass() throws ClassNotFoundException {
        Class<C> clazz1 = C.class;
        Class<? extends C> clazz2 = new C().getClass();
        Class<?> clazz3 = Class.forName("com.wolf.test.base.reflect.C");
        System.out.println(clazz1 == clazz2);
        System.out.println(clazz2 == clazz3);

        Class<?>[] classes = clazz1.getClasses();
        System.out.println(ArrayUtils.toList(classes));
    }

    @Test
    public void testGetInterface() throws ClassNotFoundException {
        Class<C> clazz1 = C.class;
        Class<?>[] interfaces = clazz1.getInterfaces();//只能获取本类实现的接口
        System.out.println(ArrayUtils.toList(interfaces));

        Class<?>[] interfaces1 = clazz1.getSuperclass().getInterfaces();
        System.out.println(ArrayUtils.toList(interfaces1));
    }

    @Test
    public void testGetName() {
        Class<C> cClass = C.class;
        Class<Float> floatClass = float.class;
        Class<Integer> integerClass = int.class;
        Class<Void> voidClass = Void.class;
        Class<? extends int[]> intArrayClass = new int[]{}.getClass();
        Class<? extends C[]> cArrayClass = new C[]{}.getClass();

        System.out.println(cClass.getName());
        System.out.println(cClass.getSimpleName());

        System.out.println(floatClass.getName());
        System.out.println(integerClass.getName());
        System.out.println(voidClass.getName());

        System.out.println(intArrayClass.getName());
        System.out.println(intArrayClass.getSimpleName());
        System.out.println(intArrayClass.getCanonicalName());//同getSimpleName

        System.out.println(cArrayClass.getName());

        Class<InnerClassAndStaticTest.StaticInnerClass> staticInnerClassClass = InnerClassAndStaticTest.StaticInnerClass.class;
        System.out.println(staticInnerClassClass.getName());//内部类用$
        System.out.println(staticInnerClassClass.getSimpleName());
        System.out.println(staticInnerClassClass.getCanonicalName());//内部类用.

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

            }
        };
        System.out.println("Runnable.class.getName:" + runnable.getClass().getName());
        System.out.println("Runnable.class.getSimpleName:" + runnable.getClass().getSimpleName());//返回空
        System.out.println("Runnable.class.getCanonicalName:" + runnable.getClass().getCanonicalName());//null

    }

    @Test
    public void testGetModifier() {
        Class<TestModifier> testModifierClass = TestModifier.class;
        int modifiers = testModifierClass.getModifiers();
        System.out.println(modifiers);
        String s = Modifier.toString(modifiers);
        System.out.println(s);
        System.out.println(Modifier.isAbstract(modifiers));
        System.out.println(Modifier.isPublic(modifiers));

    }

    @Test
    public void testGetFields() throws Exception {
        Field[] fields = C.class.getFields();//查询public字段，包含父类
        for (Field field : fields) {
            System.out.println(field.getName());
        }
        System.out.println();

        Field[] declaredFields = C.class.getDeclaredFields();//查询本类中所有字段，不包含父类
        for (Field field : declaredFields) {
            System.out.println(field.getName());
        }
    }

    @Test
    public void testGetFieldType() throws Exception {
        Class<C> cClass = C.class;
        Field listField = cClass.getDeclaredField("list");
        System.out.println(listField.getName());
        System.out.println(listField.getType());
        System.out.println();

        Type genericType = listField.getGenericType();
        if (genericType instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) genericType;
            Type rawType = parameterizedType.getRawType();
            System.out.println(rawType);
            System.out.println(parameterizedType.getOwnerType());
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            for (Type o : actualTypeArguments) {
                if (o instanceof Class) {
                    Class o1 = (Class) o;
                    System.out.println(o1.getName());
                }
            }
        }
        Class<?> type = listField.getType();
        System.out.println(type);
    }

    @Test
    public void testOperationFieldValue() throws Exception {
        C c = new C();
        c.seq = "xx";

        Class<? extends C> aClass = c.getClass();
        Field seq = aClass.getField("seq");
        Object x = seq.get(c);
        System.out.println(x);

        seq.set(c, "yyy");

        System.out.println(c.seq);

        Field age = aClass.getDeclaredField("age");
        age.setAccessible(true);
        System.out.println(age.get(c));
    }

    @Test
    public void testGetMethods() {
        Class<C> cClass = C.class;

        Method[] methods = cClass.getMethods();//获取本类+父类的public方法
        for (Method method : methods) {
            System.out.println("methods name:" + method.getName());
            Class<?> returnType = method.getReturnType();
            System.out.println("returnType:" + returnType.getName());

            Type genericReturnType = method.getGenericReturnType();
            if (genericReturnType instanceof Class) {
                System.out.println("returnType Class:" + returnType.getName());
            } else {
                ParameterizedType parameterReturnType = (ParameterizedType) genericReturnType;
                System.out.println("returnType ParameterizedType:" + parameterReturnType.getActualTypeArguments());
            }

            System.out.println("getParameters===");
            Parameter[] parameters = method.getParameters();
            for (Parameter parameter : parameters) {
                System.out.print(parameter.getName() + " ");
                System.out.print(parameter.getType() + " ");
                System.out.print(parameter.getParameterizedType() + " ");
                System.out.print(parameter.getModifiers());
                System.out.println();
            }

            System.out.println("getParameterTypes===");
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                System.out.print("parameter:" + parameterType.getName() + ",");
            }
            System.out.println();

            Class<?>[] exceptionTypes = method.getExceptionTypes();
            for (Class o : exceptionTypes) {
                System.out.println(o);
            }
        }

        System.out.println("=========");

        Method[] methods2 = cClass.getDeclaredMethods();//获取本类的所有方法
        for (Method method : methods2) {
            System.out.println("methods name:" + method.getName());
            Class<?> returnType = method.getReturnType();
            System.out.println("returnType:" + returnType.getName());
            Class<?>[] parameterTypes = method.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                System.out.print("parameter:" + parameterType.getName() + ",");
            }
            System.out.println();
        }

    }

    @Test
    public void testOperationMethod() throws Exception {
        C c = new C();
        Class cClass = c.getClass();
        Method testbbb = cClass.getMethod("testbbb", String.class, Integer.class);
        Object invoke = testbbb.invoke(c, "a", 1);
        System.out.println(invoke);

        Method testException = cClass.getMethod("testException", null);
        try {
            testException.invoke(c, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
//            e.printStackTrace();
            Throwable cause = e.getCause();
            System.out.println("cause:" + cause.toString());
            String message = e.getMessage();
            System.out.println("message:" + message);
        }


        Method testStatic = cClass.getMethod("testStatic");
        Object invoke1 = testStatic.invoke(null, null);
        System.out.println(invoke1);


        C<String> c1 = new C<String>();
        //类型擦除，C类中编译后保持E，但是这里若有get则编译后是强转(String)
        Method testGeneric = c1.getClass().getMethod("testGeneric", Object.class);
        Type[] genericParameterTypes = testGeneric.getGenericParameterTypes();
        for (Type o : genericParameterTypes) {
            if (o instanceof TypeVariable) {
                TypeVariable o1 = (TypeVariable) o;
                Type[] bounds = o1.getBounds();
                GenericDeclaration genericDeclaration = o1.getGenericDeclaration();
            }
        }
        Object invoke2 = testGeneric.invoke(c1, 111);
        System.out.println(invoke2);
    }


    @Test
    public void testGetConstructors() {
        Class<C> cClass = C.class;

        Constructor<?>[] constructors = cClass.getConstructors();//只能获取本类公共构造器，由于constructor不能继承所以无法获取父类的构造器
        for (Constructor<?> constructor : constructors) {
            System.out.println("constructor name:" + constructor.getName());
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                System.out.print("parameter:" + parameterType.getName() + ",");
            }
            System.out.println();
        }

        System.out.println("=========");

        Constructor<?>[] constructors2 = cClass.getDeclaredConstructors();//获取本类所有构造器
        for (Constructor<?> constructor : constructors2) {
            System.out.println("constructor name:" + constructor.getName());
            Class<?>[] parameterTypes = constructor.getParameterTypes();
            for (Class<?> parameterType : parameterTypes) {
                System.out.print("parameter:" + parameterType.getName() + ",");
            }
            System.out.println();
        }
    }

    @Test
    public void testOperationConstructor() throws Exception {
        Class<C> cClass = C.class;
        C c = cClass.newInstance();


        Constructor<C> constructor = cClass.getConstructor(Integer.class);
        C c1 = constructor.newInstance(4444);
        System.out.println(c1.getAge());
    }

    @Test
    public void testArray() throws Exception {
        C c = new C();
        Class cClass = c.getClass();
        Field arr = cClass.getField("arr");
        Class<?> type = arr.getType();
        System.out.println(type.getComponentType());
        System.out.println(type.isArray());

        String[] o = (String[]) Array.newInstance(String.class, 3);
        o[0] = "abv";
        Array.set(o, 1, "cccc");

        arr.setAccessible(true);
        arr.set(c, o);

        String[] arr1 = c.arr;
        for (String s : arr1) {
            System.out.println(s);
        }

    }

    @Test
    public void testEnum() throws Exception {
        C c = new C();
        Class cClass = c.getClass();
        Field arr = cClass.getField("state");
        Class<C.State> type = (Class<C.State>) arr.getType();
        System.out.println(type.isEnum());
        System.out.println(ArrayUtils.toList(type.getEnumConstants()));//每个实例对象

        Class<C.State> stateClass = C.State.class;
        Field[] declaredFields = stateClass.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isEnumConstant()) {
                System.out.println(field.getName() + " is EnumConstant");
            } else {
                System.out.println(field.getName() + " is not EnumConstant");
            }
        }
    }

    @Test
    public void testException() {
        try {
            Class<?> xxx = Class.forName("xxx");
        } catch (ClassNotFoundException e) {
            System.out.println("not found class....");
            //e.printStackTrace();
        }

        Class<C> cClass = C.class;
        try {
            Field age = cClass.getField("age");
        } catch (NoSuchFieldException e) {
            System.out.println("field not found:age");
        }

        try {
            Field xxx = cClass.getField("xxx");
        } catch (NoSuchFieldException e) {
            System.out.println("field not found:xxx");
        }

        try {
            Method qqq = cClass.getMethod("qqq");
        } catch (NoSuchMethodException e) {
            System.out.println("method not found:qqq");
        }

        try {
            Method testbbb = cClass.getMethod("testbbb");
        } catch (NoSuchMethodException e) {
            System.out.println("method not found:testbbb");
        }

        try {
            Constructor<C> constructor = cClass.getConstructor(String.class);
        } catch (NoSuchMethodException e) {
            System.out.println("constructor not found:String.class");
        }

        C obj = new C();
        try {
            Field age = cClass.getDeclaredField("age");
            age.set(obj, 111);
        } catch (NoSuchFieldException e) {
            System.out.println("field not found:age");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        try {
            Field finalTest = cClass.getDeclaredField("finalTest");
            finalTest.setAccessible(true);
            finalTest.set(obj, "111");
            System.out.println(obj.finalTest);//debug是111，但是输出到控制台变成了abc？？？
            System.out.println(finalTest.get(obj));
        } catch (NoSuchFieldException e) {
            System.out.println("field not found:finalTest");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    //我们想要的就是我用那个类就加载那个类，也就是常说的运行时刻加载，动态加载类
    private static void testDynamicCreateBean() throws Exception {
        Class<?> aClass = Class.forName("com.wolf.test.base.reflect.C");
        Object o = aClass.newInstance();
        System.out.println(o);
    }

    private static void test() throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        //仅能取到公共方法
        Method test1 = C.class.getMethod("test", null);
        System.out.println(test1);
        Method test2 = B.class.getMethod("test", null);
        System.out.println(test2);
        System.out.println(test1.equals(test2));
        Method test3 = A.class.getMethod("test", null);
        System.out.println(test3);
        System.out.println(test1.equals(test3));

        //所有声明的方法
        Method test4 = B.class.getDeclaredMethod("test1", null);
        System.out.println(test4);
        //不让jvm检查方法修饰符，我们可以直接访问到任何修饰符的方法
        test4.setAccessible(true);
        Object invoke = test4.invoke(new B(), null);
        System.out.println(invoke);
        System.out.println("test4.isAccessible():" + test4.isAccessible());
        test4.setAccessible(false);

        //似乎是不用恢复成false，因为每次get都不一样的method。
        Method test5 = B.class.getDeclaredMethod("test1", null);
        System.out.println("test4==test5:" + (test4 == test5));
        System.out.println("test5.isAccessible():" + test5.isAccessible());


    }

    @Test
    public void testAnnotationInstance() throws NoSuchMethodException {
        Method getCityName = ClassHasAnnotation4Test.class.getMethod("test", null);
        Annotation[] annotations = getCityName.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println(Annotation4Test.class.isInstance(annotation));
            System.out.println(annotation instanceof Annotation4Test);
        }
    }

    //看来类结构中存储自己，父类只有个外键关联
    @Test
    public void testGetFieldUtil() throws NoSuchMethodException {
        Field name = ReflectUtils.getField(C.class, "name");
        System.out.println(name);

        Collection<Field> allFields = ReflectUtils.getAllFields(C.class);
        for (Field allField : allFields) {
            System.out.println(allField);
        }
    }


    //可以看到打印出了从目标类中使用线程开始的所有栈信息。
    public void testStackTraceElement() {

        B b = new B();

        InvocationHandler invocationHandler = new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                return method.invoke(b);
            }
        };
        A o1 = (A) Proxy.newProxyInstance(b.getClass().getClassLoader(), b.getClass().getInterfaces(), invocationHandler);

        o1.printStackTraceElement();
    }
}
