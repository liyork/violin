package com.wolf.test.jvm;

/**
 * Description:
 * Created on 2021/6/25 4:39 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class MemoryTest {
    static class A {
        C c;
        byte[] data = new byte[1024 * 1024 * 2];
    }

    static class B {
        C c;
        byte[] data = new byte[1024 * 1024 * 3];
    }

    static class C {
        D d;
        E e;
        byte[] data = new byte[1024 * 1024 * 5];
    }

    static class D {
        F f;
        byte[] data = new byte[1024 * 1024 * 7];
    }

    static class E {
        G g;
        byte[] data = new byte[1024 * 1024 * 11];
    }

    static class F {
        D d;
        H h;
        byte[] data = new byte[1024 * 1024 * 13];
    }

    static class G {
        H h;
        byte[] data = new byte[1024 * 1024 * 17];
    }

    static class H {
        byte[] data = new byte[1024 * 1024 * 19];
    }

    A makeRef(A a, B b) {
        C c = new C();
        D d = new D();
        E e = new E();
        F f = new F();
        G g = new G();
        H h = new H();
        a.c = c;
        b.c = c;
        c.e = e;
        c.d = d;
        d.f = f;
        e.g = g;
        f.d = d;
        f.h = h;
        g.h = h;
        return a;
    }

    static A a = new A();
    static B b = new B();

    public static void main(String[] args) throws Exception {
        new MemoryTest().makeRef(a, b);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
