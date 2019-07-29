package com.wolf.test.jdknewfuture.java8inaction;

import org.junit.Test;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 * Description:
 *
 * @author 李超
 * @date 2019/07/25
 */
public class Refactor2Lambda {

    private static final Logger logger = java.util.logging.Logger.getLogger("aaa");

    @Test
    public void testAnnoClass2Lambda() {

        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                System.out.println("hello");
            }
        };

        Runnable runnable2 = () -> System.out.println("hello");
    }

    @Test
    public void testAnnoClassAndLambdaDiff() {

        int a = 1;
        Runnable runnable1 = new Runnable() {
            @Override
            public void run() {
                int a = 2;
                System.out.println("hello1");
                System.out.println("this:" + this);//this->Refactor2Lambda$2@5bb21b69代表匿名类本身
                System.out.println("a:" + a);//屏蔽包含类的变量
            }
        };
        runnable1.run();

        Runnable runnable2 = () -> {
            System.out.println("hello2");
            System.out.println("this:" + this);//this->Refactor2Lambda@fcd6521代表包含类
//            int a = 3;//编译报错
        };
        runnable2.run();
    }

    @Test
    public void testLambda2MethodRef() {

        Map<String, List<Dish>> group = Data.menu.stream()
                .collect(Collectors.groupingBy((d) -> {//lambda不太易读
                    if (d.getCalories() < 300) {
                        return "a";
                    } else if (d.getCalories() < 700) {
                        return "b";
                    } else {
                        return "c";
                    }
                }));

        Data.menu.stream()
                .collect(Collectors.groupingBy(Dish::getCaloriesLevel));//方法比较直观


        Data.menu.stream()
                .sorted((a, b) -> a.getCalories() - b.getCalories());

        //读起来就像问题描述
        Data.menu.stream()
                .sorted(Comparator.comparingInt(Dish::getCalories));

        //使用提供好的工具类
        Data.menu.stream()
                .map(d -> d.getCalories())
                .reduce(0, (a, b) -> a + b);

        Data.menu.stream()
                .collect(Collectors.summingInt(Dish::getCalories));

        Data.menu.stream().mapToInt(Dish::getCalories).sum();
    }

    //Stream API能更清晰地表达数据处理管道的意图
    @Test
    public void testCommandProcess2Stream() {

        //命令式操作：筛选和抽取
        List<String> names = new ArrayList<>();
        for (Dish dish : Data.menu) {
            if (dish.getCalories() < 300) {
                names.add(dish.getName());
            }
        }

        //stream操作,读起来更像是问题陈述，并行化也容易
        List<String> names2 = Data.menu.parallelStream()
                .filter(d -> d.getCalories() < 300)
                .map(Dish::getName)
                .collect(Collectors.toList());
    }

    @Test
    public void testConditionLazyExe() {

        //问题：日志器的状态（它支持哪些日志等级）通过isLoggable方法暴露给了客户端代码。
        //为什么要在每次输出一条日志之前都去查询日志器对象的状态？
        int a = 1;
        if (logger.isLoggable(Level.INFO)) {//控制语句被混杂在业务逻辑代码之中,典型的情况包括进行安全性检查以及日志输出
            logger.info("aaaaa" + "bbbbb");
        }

        //你不再需要在代码中插入条件判断(判断应该属于log类内部职责),内部检查日志对象是否已经设置为恰当的日志等级，不必再暴露内部状态
        logger.log(Level.INFO, "aaaaa" + "bbbbb");

        //延迟消息体构造。这样代码更易读(减少重复判断)，封装更好(状态自治)
        logger.log(Level.INFO, () -> "aaaaa" + "bbbbb");
    }

    //避免冗余、重复
    @Test
    public void testAroundExe() {

        aroundExe(() -> 1);
        aroundExe(() -> 2);
    }

    public <T> T aroundExe(Supplier<T> supplier) {

        try {
            System.out.println("same before..");
        } catch (Exception e) {
            e.printStackTrace();
        }

        T t = supplier.get();

        try {
            System.out.println("same end..");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return t;
    }

    //策略模式：用一组可替换的算法，动态选择。或者运行时选择。是依赖接口设计原则的结果。
    //用lambda替代策略中的子类行为
    @Test
    public void testStrategy2Lambda() {

        new Validator(new IsAllLowerCase(), "sss").isMatch();
        new Validator(new IsNumeric(), "1234").isMatch();

        new Validator(s -> s.matches("[a-z]+"), "sss").isMatch();
        new Validator(s -> s.matches("\\d+"), "1234").isMatch();
    }

    interface ValidationStrategy {
        boolean exe(String s);
    }

    class IsAllLowerCase implements ValidationStrategy {
        @Override
        public boolean exe(String s) {
            return s.matches("[a-z]+");
        }
    }

    class IsNumeric implements ValidationStrategy {
        @Override
        public boolean exe(String s) {
            return s.matches("\\d+");
        }
    }

    class Validator {
        private ValidationStrategy validation;
        private String s;

        public Validator(ValidationStrategy validation, String s) {
            this.validation = validation;
            this.s = s;
        }

        public boolean isMatch() {
            return validation.exe(s);
        }
    }

    //将灵活的部分使用lambda传入，参数化行为。不用再继承公共父类。可能也适用于方法简单？
    @Test
    public void testTemplate2Lambda() {

        new JianSheBaking().checkActivity(1);
        new PufaBaking().checkActivity(2);

        OnlineBakingLambda.checkActivity(1);
    }

    abstract class OnlineBaking {

        public void checkActivity(int id) {
            //getcustominfo
            System.out.println("id:" + id);

            processCustomHappy(id);

            System.out.println("other process:" + id);
        }

        protected abstract void processCustomHappy(int id);
    }

    class JianSheBaking extends OnlineBaking {

        @Override
        protected void processCustomHappy(int id) {
            System.out.println("JianSheBaking processCustomHappy:" + id);
        }
    }

    class PufaBaking extends OnlineBaking {

        @Override
        protected void processCustomHappy(int id) {
            System.out.println("PufaBaking processCustomHappy:" + id);
        }
    }

    static class OnlineBakingLambda {

        public static void checkActivity(Integer id) {
            checkActivity(id, i -> System.out.println("OnlineBakingLambda processCustomHappy:" + i));
        }

        private static void checkActivity(int id, Consumer<Integer> consumer) {

            //getcustominfo
            System.out.println("id:" + id);

            consumer.accept(id);

            System.out.println("other process:" + id);
        }
    }

    //用lambda重写观察者，不过若是lambda中内容很多，不易理解，可能还是需要类来支撑。lambda能行为化参数，但似乎只能替换简单的功能？也可以
    //使用类，然后用方法引用使得代码简洁又不失易读
    @Test
    public void testObserve2Lambda() {

        Feed feed = new Feed();
        feed.registryObserver(new Tom());
        feed.registryObserver(new Petter());
        feed.notifyObserver("xx");

        Feed feed2 = new Feed();
        feed2.registryObserver(s -> System.out.println("Tom notify this:" + s));
        feed2.registryObserver(s -> System.out.println("Petter notify this:" + s));
    }

    interface Observer {

        void notify(String s);
    }

    class Tom implements Observer {

        public void notify(String s) {
            System.out.println("Tom notify this:" + s);
        }
    }

    class Petter implements Observer {

        public void notify(String s) {
            System.out.println("Petter notify this:" + s);
        }
    }

    interface Subject {

        void registryObserver(Observer observer);

        void notifyObserver(String s);
    }

    class Feed implements Subject {

        private List<Observer> observers = new ArrayList<>();

        @Override
        public void registryObserver(Observer observer) {
            observers.add(observer);
        }

        @Override
        public void notifyObserver(String s) {
            observers.forEach(o -> o.notify(s));
        }
    }

    //合并lambda表达式
    @Test
    public void testFilterChain2Lambda() {

        Processor processor1 = new Processor1();
        Processor processor2 = new Processor2();
        processor1.setSuccessor(processor2);

        String xx1 = processor1.invoke("xxx");
        System.out.println("xx1:" + xx1);

        UnaryOperator<String> unaryOperator1 = (s) -> {
//            System.out.println("process 1:" + s);
            return s + ":" + 1;
        };
        UnaryOperator<String> unaryOperator2 = (s) -> {
//            System.out.println("process 2:" + s);
            return s + ":" + 2;
        };
        Function<String, String> function = unaryOperator1.andThen(unaryOperator2);
        String xx2 = function.apply("xxx");
        System.out.println("xx2:" + xx2);
    }

    public abstract class Processor {

        private Processor successor;

        public String invoke(String s) {
            String result = process(s);
            if (null != successor) {
                return successor.invoke(result);
            }
            return result;
        }

        protected abstract String process(String s);

        public Processor getSuccessor() {
            return successor;
        }

        public void setSuccessor(Processor successor) {
            this.successor = successor;
        }
    }

    class Processor1 extends Processor {

        @Override
        protected String process(String s) {
//            System.out.println("process 1:" + s);
            return s + ":" + 1;
        }
    }

    class Processor2 extends Processor {

        @Override
        protected String process(String s) {
//            System.out.println("process 2:" + s);
            return s + ":" + 2;
        }
    }

    @Test
    public void testFunction2Lambda() {

        ProductFactory productFactory = new ProductFactory();
        productFactory.createProduct("apple");

        productFactory.createProductWithLambda("apple");
    }

    static class ProductFactory {
        public Product createProduct(String name) {
            if (name.equals("apple")) {
                return new Apple();
            } else if (name.equals("orange")) {
                return new Apple();
            } else {
                throw new IllegalArgumentException("not found name:" + name);
            }
        }

        static Map<String, Supplier<Product>> constructorRef = new HashMap<String, Supplier<Product>>();

        {
            constructorRef.put("apple", Apple::new);
            constructorRef.put("orange", Orange::new);
        }

        public Product createProductWithLambda(String name) {
            Supplier<Product> productSupplier = constructorRef.get(name);
            if (null == productSupplier) {
                throw new IllegalArgumentException("not found name:" + name);
            }
            return productSupplier.get();
        }

        @FunctionalInterface
        interface Product {
            void printName();
        }

        class Apple implements Product {

            public Apple() {
            }

            public void printName() {
            }
        }

        class Orange implements Product {
            public Orange() {
            }

            public void printName() {
            }
        }
    }
}