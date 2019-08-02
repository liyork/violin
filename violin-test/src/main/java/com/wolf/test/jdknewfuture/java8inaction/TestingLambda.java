package com.wolf.test.jdknewfuture.java8inaction;

import org.junit.Test;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Description: 测试lambda
 *
 * @author 李超
 * @date 2019/07/30
 */
public class TestingLambda {

    @Test
    public void testExpectAndActual() {

        Point point = new Point(1, 1);
        Point point1 = point.moveRight(3);
        assertEquals(4, point1.getX());
        assertEquals(1, point1.getY());
    }

    //使用字段测试lambda表达式，Lambda表达式会生成函数接口的一个实例，通过字段测试实例的行为
    @Test
    public void testLambdaByField() {

        Point point1 = new Point(1, 1);
        Point point2 = new Point(1, 2);
        Point point3 = new Point(1, 1);

        int compare = Point.comparator.compare(point1, point2);
        assertNotEquals(1, compare);
        int compare2 = Point.comparator.compare(point1, point3);
        assertEquals(0, compare2);
    }

    @Test
    public void testUseLambdaInMethod() {

        List<Point> points = Arrays.asList(new Point(1, 2), new Point(1, 3));
        List<Point> expect = Arrays.asList(new Point(3, 2), new Point(3, 3));
        List<Point> actual = Point.moveAllRight(points, 2);
        assertEquals(expect, actual);
    }

    @Test
    public void testParamLambdaMethod() {

        List<Point> points1 = Arrays.asList(new Point(1, 2), new Point(1, 3));
        List<Point> filter = Point.filter(points1, (p) -> p.getX() > 1);
        assertEquals(filter, Collections.emptyList());
        List<Point> filter1 = Point.filter(points1, (p) -> p.getX() < 2);
        assertEquals(filter1, points1);
    }

    //栈输出中带有lambda$testStackTrace$2(TestingLambda.java:67)，因为是匿名的lambda
    //方法最好还是分行写，容易排查
    @Test
    public void testStackTrace() {

        List<Point> points = Arrays.asList(new Point(1, 2), null);
        points.stream()
                .map(p -> p.getX())
                .forEach(System.out::println);
//        points.stream().map(Point::getX).forEach(System.out::println);
    }

    //查看流中每步操作的结果
    @Test
    public void testDebugPeek() {

        Arrays.asList(1, 2, 3, 4).stream()
                .peek(i -> System.out.println("from stream " + i))
                .filter(i -> i >= 2)
                .peek(i -> System.out.println("after filter " + i))
                .map(i -> i + 2)
                .peek(i -> System.out.println("after map " + i))
                .limit(2)
                .peek(i -> System.out.println("after limit " + i))
                .forEach(i -> System.out.println("for each " + i));
    }

    static class Point {
        private int x;

        private int y;

        public static Comparator<Point> comparator = Comparator.comparingInt(Point::getX).thenComparing(Point::getY);

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public Point moveRight(int x) {
            return new Point(this.x + x, y);
        }

        public static List<Point> moveAllRight(List<Point> points, int x) {

            return points.stream()
                    .map(p -> new Point(p.getX() + x, p.getY()))
                    .collect(Collectors.toList());
        }

        public static List<Point> filter(List<Point> points, Predicate<Point> predicate) {

            System.out.println("filter start business logic code ..");
            List<Point> collect = points.stream()
                    .filter(predicate::test)
                    .collect(Collectors.toList());
            System.out.println("filter end business logic code ..");

            return collect;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x &&
                    y == point.y;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y);
        }
    }
}
