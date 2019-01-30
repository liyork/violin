package com.wolf.test.jvm.initialseq;

import com.wolf.test.jvm.initialseq.vo.Children1;
import com.wolf.test.jvm.initialseq.vo.Children2;
import org.junit.Test;

/**
 * Description:
 * <p>
 * <p>
 * <br/> Created on 2017/6/17 0:32
 *
 * @author 李超
 * @since 1.0.0
 */
public class InitialSeqTest {

    /**
     * 先加载Children1，初始化，加载parent，初始化执行static，然后执行Children1
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
     * 子类先调用父类，然后父类调用getV1被子类重写了，而这时子类的value1还是初始值，所以0
     *
     * @throws Exception
     */
    @Test
    public void testThisInConstructor() throws Exception {
        Children2 s = new Children2();
        System.out.println("children2 get v2:"+s.getV2());
        System.out.println("children2 get v1:"+s.getV1());
    }

    @Test
    public void testWholeSql() {

        new StaticFieldNewInstanceInitialSeq("init");
    }
}
