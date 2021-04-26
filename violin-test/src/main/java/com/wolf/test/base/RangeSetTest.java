package com.wolf.test.base;

import com.google.common.collect.Range;
import com.google.common.collect.RangeSet;
import com.google.common.collect.TreeRangeSet;

import java.util.Iterator;
import java.util.Set;

/**
 * Description:RangeSet类是用来存储一些不为空的也不相交的范围的数据结构。假如需要向RangeSet的对象中加入一个新的范围，那么任何相交的部分都会被合并起来，所有的空范围都会被忽略
 * Created on 2021/4/14 6:19 PM
 *
 * @author 李超
 * @version 0.0.1
 */
public class RangeSetTest {

    public static void main(String[] args) {
        //testBase();

        //testIterator();

        //testComplement();

        //testContains();

        //testInWhichRange();

        testEncloses();
    }

    private static void testEncloses() {
        RangeSet rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closed(1, 10));
        rangeSet.add(Range.closed(11, 15));
        // rangeSet都在参数里则true
        boolean encloses = rangeSet.encloses(Range.closedOpen(18, 20));
        System.out.println(encloses);
        encloses = rangeSet.encloses(Range.closedOpen(0, 5));
        System.out.println(encloses);
    }

    private static void testInWhichRange() {
        // 查询某个元素在rangeSet中哪个范围中
        RangeSet rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closed(1, 10));
        rangeSet.add(Range.closed(11, 15));
        Range range = rangeSet.rangeContaining(12);
        System.out.println(range);
        range = rangeSet.rangeContaining(16);
        System.out.println(range);
    }

    private static void testContains() {
        RangeSet rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closed(1, 10));
        rangeSet.add(Range.closed(11, 15));
        boolean contains = rangeSet.contains(15);
        System.out.println(contains);
        contains = rangeSet.contains(16);
        System.out.println(contains);
    }

    // 得到rangeSet互补的范围(互补集)，其中的元素也是互不相交、且不为空的RangeSet
    private static void testComplement() {
        RangeSet rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closed(1, 10));
        rangeSet.add(Range.closed(11, 15));
        RangeSet complement = rangeSet.complement();
        System.out.println(rangeSet);
        System.out.println(complement);
    }

    private static void testIterator() {
        RangeSet rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closed(1, 10));
        rangeSet.add(Range.closed(11, 15));
        Set<Range> ranges = rangeSet.asRanges();
        Iterator<Range> iterator = ranges.iterator();
        while (iterator.hasNext()) {
            Range next = iterator.next();
            System.out.println(next);
        }
    }

    private static void testBase() {
        RangeSet rangeSet = TreeRangeSet.create();
        rangeSet.add(Range.closed(1, 10));
        System.out.println(rangeSet);

        rangeSet.add(Range.closed(11, 15));
        System.out.println(rangeSet);

        rangeSet.add(Range.open(15, 20));
        System.out.println(rangeSet);

        rangeSet.add(Range.openClosed(0, 0));
        System.out.println(rangeSet);

        rangeSet.remove(Range.open(5, 10));
        System.out.println(rangeSet);
    }
}
