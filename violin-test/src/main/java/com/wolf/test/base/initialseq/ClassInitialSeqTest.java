package com.wolf.test.base.initialseq;

import org.junit.Test;

/**
 * Description:
 * <br/> Created on 2017/6/17 0:32
 *
 * @author 李超
 * @since 1.0.0
 */
public class ClassInitialSeqTest {

    public static void main(String[] args) throws Exception {

        //测试2

    }

    /**
     * 先加载Children1，遇到extends马上加载parent，执行static，然后执行Children1
     */
    @Test
    public void testLoadClassStaticPart() {
        Children1.test1();
    }

    @Test
    public void testLoadClassStaticPart2() throws Exception {
        new Children1();
    }

    /**
     * 构造器中super和this平级，都先得调用父类或者重载构造器之后才能初始化成员变量。
     *
     * @throws Exception
     */
    @Test
    public void testThisInConstructor() throws Exception {
        Children s = new Children();
        System.out.println(s.getV2());
    }

    @Test
    public void testWholeSql() {
        new ClassInitial("init");
    }
}
