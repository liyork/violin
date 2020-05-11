package com.wolf.algorithm;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Description:
 *
 * @author 李超
 * @date 2020/01/07
 */
public class ArrayOpt {

    @Test
    public void findMax() {
        int[] arr = {7, 2, 8, 9, 3, 5, 6};

        int max = arr[0];
        int length = arr.length;
        for (int i = 1; i < length; i++) {
            if (max < arr[i]) {
                max = arr[i];
            }
        }

        System.out.println("max:" + max);
    }

    @Test
    public void findSecondMax() {
        int[] arr = {7, 2, 8, 9, 3, 5, 6};

        int max = arr[0];
        int secMax = Integer.MIN_VALUE;
        int length = arr.length;
        for (int i = 1; i < length; i++) {
            if (max < arr[i]) {
                secMax = max;
                max = arr[i];
            } else if (secMax < arr[i]) {
                secMax = arr[i];
            }
        }

        System.out.println("secMax:" + secMax);
    }

    // 数组中有正有负, 求最大连续子序列和最大值
    // 方法一：找出所有子连续数组，i从0~n-1，j从i~n-1，然后计算i和j之间的和，O(n^3)，许多子数组(thisSum)都重复计算
    @Test
    public void findMaxSeq1() {
        int[] arr = {1, -2, 4, 8, -4, 7, -1, -5};

        int thisSum;
        int maxSum = 0;
        int i, j, k;
        int length = arr.length;
        int start = 0, end = 0;

        for (i = 0; i < length; i++) {// i从0~n-1
            for (j = i; j < length; j++) {// j从i~n-1
                thisSum = 0;
                k = i;
                while (k <= j) {// k从i~j
                    thisSum += arr[k];
                    k++;
                }
                if (thisSum > maxSum) {
                    maxSum = thisSum;
                    start = i;
                    end = k - 1;
                }
            }
        }
        System.out.println("sum:" + maxSum + "," + arr[start] + "--" + arr[end]);
    }

    // 方法二：去掉一的重复计算子序列问题，例如Sum[i,j]=Sum[i,j-1]+arr[j]，不用再计算Sum[i,j-1]了。O(n^2)
    @Test
    public void findMaxSeq2() {
        int[] arr = {1, -2, 4, 8, -4, 7, -1, -5};

        int maxSum = 0;
        int i, j;
        int length = arr.length;
        int start = 0, end = 0;

        for (i = 0; i < length; i++) {// i从0~n-1
            int thisSum = 0;
            for (j = i; j < length; j++) {// j从i~n-1
                thisSum += arr[j];// 每次都+j，省去了从i开始加到n的过程
                if (thisSum > maxSum) {
                    maxSum = thisSum;
                    start = i;
                    end = j;
                }
            }
        }
        System.out.println("sum:" + maxSum + "," + arr[start] + "--" + arr[end]);
    }

    // 方法三：动态规划
    // 数组最后一个元素arr[n-1]与最大子数组关系为：
    // 1.最大子数组包含arr[n-1]，即以arr[n-1]结尾
    // 2.arr[n-1]单独构成最大子数组
    // 3.最大子数组不包含arr[n-1]，那么求最大子数组可以转换为求arr[1,...,n-2]的最大子数组
    // 结论：假设已经计算出(arr[0],...,arr[i-1])最大的一段数组和为All[i-1]，同时也计算出(arr[0],...,arr[i-1])中包含arr[i-1]的
    // 最大的一段数组和为End[i-1]，则得出关系：All[i-1]=max{arr[i-1],End[i-1],All[i-2]}。也即每次向右加值，max为sum(i-1),i-1,sum(i-2)中最大
    // O(n)，由于额外申请两个数组空间，空间复杂度为O(n)
    @Test
    public void findMaxSeq3() {
        int[] arr = {1, -2, 4, 8, -4, 7, -1, -5};

        int n = arr.length;
        int End[] = new int[n];// 不断加总新值，得到End[i-1]和arr[i-1]的最大值，并记录
        int All[] = new int[n];// All[i-1]和End[i]较大值
        End[n - 1] = arr[n - 1];
        All[n - 1] = arr[n - 1];
        End[0] = All[0] = arr[0];
        for (int i = 1; i < n; i++) {
            // 之前已经加总+i后与i比较，使用最大，用于下面判断，也为了记住此前加总最大值。若已经加总较大则能继续，否则就从i重新开始向前。
            End[i] = max(End[i - 1] + arr[i], arr[i]);
            All[i] = max(End[i], All[i - 1]);// 记录最大序列和值，即比较：之前和、加上i后的值、i值三者最大值
        }

        System.out.println(All[n - 1]);
    }

    // 方法四：优化的动态规划
    // 方法三中只用到数组End[i-1]和All[i-1]位置存放，可以换成变量。
    @Test
    public void findMaxSeq4() {
        int[] arr = {1, -2, 4, 8, -4, 7, -1, -5};

        int n = arr.length;
        int nAll = arr[0];// 有n个元素的最大子数组和
        int nEnd = arr[0];// 有n个元素的最大数组和(包含最后一个元素)
        for (int i = 1; i < n; i++) {
            nEnd = max(nEnd + arr[i], arr[i]);
            nAll = max(nEnd, nAll);
        }

        System.out.println(nAll);
    }

    // 给予方法四计算子数组位置
    // 通过对nEnd = max(nEnd + arr[i], arr[i]);分析，
    // 当nEnd<0时，nEnd=arr[i](因为若arr[i]为负相加更小则用arr[i],若arr[i]为正则相加大也用arr[i])
    // 即推出：如果某一个值使得nEnd<0则从arr[i]重新开始。
    @Test
    public void findMaxSeq5() {
        int[] arr = {1, -2, 4, 8, -4, 7, -1, -5};
        int begin = 0;
        int end = 0;

        int n = arr.length;
        int sum = Integer.MIN_VALUE;
        int aggregate = 0;
        for (int i = 0; i < n; i++) {
            if (aggregate < 0) {
                aggregate = arr[i];
                begin = i;
            } else {
                aggregate += arr[i];
            }

            if (aggregate > sum) {
                sum = aggregate;
                end = i;
            }
        }

        System.out.println(sum + ",begin:" + begin + ",end:" + end);
    }

    public static int max(int m, int n) {
        return m > n ? m : n;
    }

    // 查询出现次数最多的数
    @Test
    public void findMostFrequently() {
        int[] arr = {1, 4, 2, 2, 8, 4, 7, 4, 4};
        Map<Integer, Integer> countMap = new HashMap<>();
        int length = arr.length;
        for (int i = 0; i < length; i++) {
            Integer count = countMap.get(arr[i]);
            if (null == count) {
                count = 0;
            }
            countMap.put(arr[i], ++count);
        }

        int tmpCount = -1;
        int tmpKey = 0;
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            Integer key = entry.getKey();
            Integer value = entry.getValue();
            if (tmpCount < value) {
                tmpCount = value;
                tmpKey = key;
            }
        }

        System.out.println("key:" + tmpKey);
    }

    // 求两数组合是20的元素
    // 方法一：两重循环，比对所有两数相加可能。时间复杂度O(n^2)
    // 方法二：排序法。先排序，然后从前begin和从后end相加，若和小于sum则begin向右移动(右边大)，若和大于sum则end向左移动。
    // 用堆排序或快排时间复杂度为O(nlogn)，前后比较时间复杂度为O(n)，最后为O(nlogn)
    @Test
    public void findSumCombination() {
        int[] arr = {1, 5, 17, 2, 6, 3, 15};
        Arrays.sort(arr);// 1,2,3,5,6,15,17
        int sum = 20;

        int begin = 0;
        int end = arr.length - 1;
        while (begin < end) {
            if (arr[begin] + arr[end] < sum) {
                begin++;
            } else if (arr[begin] + arr[end] > sum) {
                end--;
            } else {
                System.out.println(arr[begin] + "," + arr[end]);
                begin++;
                end--;
            }
        }
    }

    // 循环右移k位
    // 1, 5, 17, 2, 6, 3, 15 --右移动2位--> 3, 15, 1, 5, 17, 2, 6，发现两段序列的顺序不变，3，5和1, 5, 17, 2, 6
    // 可以把这两段看做两个整体，右移k位就是把数组的两部分交换。
    // 实现：分别逆序两部分，然后逆序所有
    @Test
    public void shiftKBit() {
        int[] arr = {1, 5, 17, 2, 6, 3, 15};
        int k = 2;

        int length = arr.length;

        k = k % length;// 防止k比length大，右移k位和右移k%n位结果一样
        reverse(arr, length - k, length - 1);
        reverse(arr, 0, length - k - 1);
        reverse(arr, 0, length - 1);

        System.out.println(Arrays.toString(arr));
    }

    public static void reverse(int[] arr, int begin, int end) {
        for (; begin < end; begin++, end--) {
            CommonUtils.swap(arr, begin, end);
        }
    }

    // 查找第k个最小的数
    // 方法一：排序法。对数组排序，然后取第k-1位置(数组从0开始下标)。时间复杂度O(nlogn)
    // 方法二：剪枝法。采用快排思想，只不过要进行左排和右排之前先判断中枢+1和k的位置然后返回或选择一边，不断缩小规模。效率高，因为只考虑一边。
    @Test
    public void testFindKSmall() {
        int[] arr = {4, 5, 17, 2, 6, 3, 15};
        int kSmall = findKSmall(arr, 0, arr.length - 1, 6);
        System.out.println(Arrays.toString(arr));
        System.out.println(kSmall);
    }

    private int findKSmall(int[] arr, int beginIndex, int endIndex, int kSmall) {

        if (beginIndex >= endIndex) {
            return arr[beginIndex];
        }

        int midValue = arr[beginIndex];

        int leftIndex = beginIndex;
        int rightIndex = endIndex;

        while (true) {
            while (rightIndex > leftIndex && arr[rightIndex] > midValue) {
                rightIndex--;
            }

            if (leftIndex >= rightIndex) {
                break;
            }

            arr[leftIndex++] = arr[rightIndex];

            while (leftIndex < rightIndex && arr[leftIndex] < midValue) {
                leftIndex++;
            }

            if (leftIndex >= rightIndex) {
                break;
            }

            arr[rightIndex--] = arr[leftIndex];
        }
        arr[leftIndex] = midValue;

        if (leftIndex + 1 < kSmall) {
            return findKSmall(arr, leftIndex + 1, endIndex, kSmall);
        } else if (leftIndex + 1 > kSmall) {
            return findKSmall(arr, beginIndex, leftIndex - 1, kSmall);
        } else {
            return arr[leftIndex];
        }
    }

    // 条件：只有一个数字除外其他数组都出现两次。时间复杂度是O(n)，空间复杂度是O(1)。求出这个数
    // 若不要求复杂度，可以排序之后遍历比较相邻数得到，O(nlogn)。或者使用map然后得到，O(n)但是空间复杂度是O(n)
    // 由于只有一个数字1次其他都2次，想到异或运算，因为任何一个数字异或自己都为0。遍历数组进行异或，两次的都抵消，剩余就是所求。
    // 适用于其他数都是偶数的情景。
    @Test
    public void testFindOnceInTwo() {
        int[] arr = {3, 5, 4, 15, 3, 4, 5};
        int result = arr[0];
        for (int i = 1; i < arr.length; i++) {
            result ^= arr[i];
        }
        System.out.println(result);
    }

    // 出了一个数之外其余都出现3次。
    // 若数组中所有元素都出现n次，则此数组中所有数对应的二进制数中，各个位上的1出现的个数均可以被n整除。因此统计所有元素位然后取余n，
    // 不为0的则是单个元素的位
    // 适用于其他数奇偶都可，因为1个数和他们不一样。
    @Test
    public void testFindOnceInTree() {
        int[] arr = {3, 3, 4, 4, 3, 5, 4};
        int[] bitCount = new int[32];
        int length = arr.length;
        // 加总所有元素的二进制位出现1的次数
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < 32; j++) {// 移动32次(0~31位)
                bitCount[j] += (arr[i] >> j) & 1;// 数组存放从低位开始
            }
        }

        int result = 0;
        for (int i = 0; i < bitCount.length; i++) {
            if (bitCount[i] % 3 != 0) {// 不能整除的位，一定是要查询的数的位
                result += 1 << i;
            }
        }

        System.out.println(result);
    }

    // 1~(n-1)，n-1个连续数字，放入new arr[n]中(顺序不要求)，找出仅有一个元素重复
    // 要求：每个元素只能访问1次，并且不用辅助存储空间。
    // 采用数学求和法，因为只有一个数字重复1次，又是连续数字，根据累加和原理，对数组所有项求和然后减去1~n的和，即为所求的重复数。
    // 看来潜在因素在元素就是下标上。。
    @Test
    public void testFindDupe1() {

        int[] arr = {1, 2, 3, 3, 5, 4, 7, 8, 9, 6};
        int numSum_1 = 0;// numSum-1
        int arrSum = 0;
        int length = arr.length;
        for (int i = 0; i < length - 1; i++) {
            numSum_1 += (i + 1);// 1~(length - 1),length-1个数的和
            arrSum += arr[i];// arr[0]~arr[n-2]
        }

        // 补上最后元素
        arrSum += arr[length - 1];// +arr[n-1]

        int result = arrSum - numSum_1;
        System.out.println(result);
    }

    // 若无要求每个数组元素只能访问1次，且不允许使用辅助存储空间。
    // 异或法。每两个相异的位执行异或运算之后，结果为1，相同的位执行异或后结果为0。所以数组a[n]中n个元素异或结果与1~n-1异或的结果再异或得到结果。
    // 设重复数为A，其余n-1个数异或结果为B，n个数异或结果为A^A^B，1~n-1异或结果为A^B，
    // 由于异或满足交换律和结合律，且X^X=0,0^X=X，则有(A^A^B)^(A^B)=>A^B^B=>A
    @Test
    public void testFindDupe2() {

        int[] arr = {1, 2, 3, 3, 5, 4, 7, 8, 9, 6};
        int length = arr.length;
        int result = 0;
        for (int i = 0; i < length; i++) {
            result ^= arr[i];
        }

        for (int i = 1; i < length; i++) {
            result ^= i;
        }

        System.out.println(result);
    }

    // 空间换时间法。
    // 申请长度为n的整形数组，然后遍历arr数组，取每个元素a[i]值，将其对应到新数组a[i]位置上，若已存在则得到结果。
    // 空间复杂度比较大。也可以使用位图来降低空间复杂度，即不用整形数字来表示元素是否出现过，而用1bit表示，因此需要申请数组的长度为n/32取上整。
    @Test
    public void testFindDupe3() {
        int[] arr = {1, 2, 3, 5, 4, 7, 8, 9, 6, 3};
        int length = arr.length;
        int[] count = new int[length];// 1~n-1个数，最大为n-1，创建n的数组，0位置不放
        int dupIndex = 0;
        for (int i = 0; i < length; i++) {
            int elementIndex = arr[i];
            int tmp = count[elementIndex];
            if (tmp == 0) {
                count[elementIndex] = 1;
            } else {
                dupIndex = i;
                break;
            }
        }
        System.out.println(arr[dupIndex]);
    }

    // 取值为[1~n-1]，放入含n个元素的数组中，至少存在一个重复数，即可能存在多个重复数。O(n)时间内找出其中任意一个重复数。
    // 位图法。使用大小为n的位图，记录每个元素是否出现过，一旦遇到已出现过则输出。时间复杂度O(n),空间复杂度O(n)
    // 数组排序法。先排序，然后顺序扫描，一旦遇到一个一出现的元素则输出。时间复杂度O(nlogn),空间复杂度O(1)
    // 下面方案：取反法。若遍历到元素i，则把a[i]值取反，如果i在数组中出现两次则a[i]经过两次取反还是原值，若出现1次则a[i]为反之。
    // todo 此方法目前看的问题是：若i元素不被指向则无法区分出i元素，因为他也是正。
    @Test
    public void testFindDupe4NotOne1() {
//        int[] arr = {1, 2, 4, 5, 2, 3, 7, 8, 6};
//        int[] arr = {4, 2, 1, 3, 4};
        int[] arr = {1, 2, 2, 4, 5, 4};
//        int[] arr = {3, 4, 3, 5, 7, 6, 7, 2, 1};

        int length = arr.length;
        for (int i = 0; i < length; i++) {
            int index = arr[i];
            if (index > 0) {
                arr[index] = -arr[index];
            } else {
                arr[-index] = -arr[-index];// 使用反值作为下标继续操作arr
            }
        }

        for (int i = 1; i < length; i++) {
            if (arr[i] > 0) {
                System.out.println(i);
            } else {
                arr[i] = -arr[i];// 恢复
            }
        }
    }

    // 采用类似于单链表是否存在环的问题。单链表可以采用数组实现，此时每个元素值作为next指针指向下一个元素。
    // 本题转化为"已知一个单链表中存在环，找出环的入口点"。思路：将arr[i]的元素看做指向arr[arr[i]]，最终形成一个单链表，
    // 由于数组中存在重复元素，因此一定存在环，且环入口即为所求。
    // 问题的关键在于，数组长度是n，而元素的范围是[1,n-1]，所以arr[0]不会指向自己，进而不会陷入错误的自循环。所以元素范围不能包含0
    @Test
    public void testFindDupe4NotOne2() {

        int[] arr = {1, 2, 2, 4, 5, 4};

        int x, y;
        x = y = 0;
        do {
            x = arr[arr[x]];// x一次走两步
            y = arr[y];// y一次走一步
        } while (x != y);// 找到环中的一个点
        x = 0;
        do {
            x = arr[x];
            y = arr[y];
        } while (x != y);// 找到入口点
        System.out.println(x);
    }

    // 递归实现：求数组的最元素
    // 思路：递归的求解"数组第一个元素"与"数组中其他元素组成的子数组的最大值"的最大值。
    @Test
    public void testFindMaxRecursive() {

        int[] arr = {1, 2, 9, 7, 5, 4};
        int maxValue = findMaxRecursive(arr, 0);
        System.out.println(maxValue);
    }

    // 求begin开始的最大元素
    private int findMaxRecursive(int[] arr, int begin) {
        if (begin == arr.length - 1) {// 最后一个元素
            return arr[begin];
        }

        int maxValue = findMaxRecursive(arr, begin + 1);
        return Math.max(arr[begin], maxValue);
    }

    // 求:数对只差的最大值。描述：数组中的一个数字减去它右边子数组中的一个数字可以得到一个差值，求所有可能的差值中的最大值。
    // 方法一：遍历法。两层循环，外层i在[0,n-2]，内层[i+1,n-1]，不断求差值。时间复杂度O(n^2)
    // 二分法(如下)。减少计算的次数。思路：把数组分为两个子数组，那么最大的差值只能是：a.被减数和减数都在左子数组中。b.被减数和减数都在
    // 右子数组中。c.被减数是座子数组的最大值，减数是右子数组中的最小值。求三者最大值。
    // 只对数组经过一次遍历，时间复杂度为O(n)。由于采用了递归实现，要进行压栈和弹栈，有额外开销，导致性能下降。由于通过引用方式获取上一步
    // 的最大和最小值使用了AtomicInteger,也有开销。
    @Test
    public void testFindMaxDiff1() {
        int[] arr = {1, 2, 9, 7, 5, 4};
        AtomicInteger min = new AtomicInteger(0);
        AtomicInteger max = new AtomicInteger(0);
        int maxDiff = getMaxDiff1(arr, 0, arr.length - 1, min, max);
        System.out.println(maxDiff);
    }

    private int getMaxDiff1(int[] a, int begin, int end, AtomicInteger max, AtomicInteger min) {
        if (begin == end) {
            min.set(a[begin]);
            max.set(a[begin]);
            return Integer.MIN_VALUE;
        }
        int middle = (end + begin) / 2;
        AtomicInteger lMax = new AtomicInteger();
        AtomicInteger lMin = new AtomicInteger();
        int leftMax = getMaxDiff1(a, begin, middle, lMax, lMin);

        AtomicInteger rMax = new AtomicInteger();
        AtomicInteger rMin = new AtomicInteger();
        int rightMax = getMaxDiff1(a, middle + 1, end, rMax, rMin);

        int lrDiff = (leftMax > rightMax) ? leftMax : rightMax;

        int midMax = lMax.get() - rMin.get();// 题目要求左边和右边数组差值

        if (lMax.get() > rMax.get()) {
            max.set(lMax.get());
        } else {
            max.set(rMax.get());
        }

        if (lMin.get() < rMin.get()) {
            min.set(lMin.get());
        } else {
            min.set(rMin.get());
        }

        return (lrDiff > midMax) ? lrDiff : midMax;
    }

    // 动态规划。通过对题目分析，发现是动态规划问题。思路：戈丁数组a，申请额外的数组diff和max，
    // diff[i]是以数组中第i个数字为减数的所有数对之差的最大值(即：前i(包含)个数而组成的子数组中最大的差值)，max[i]为前i(包含)个数的最大值。
    // 假设已经求得diff[i]，diff[i+1]的值有两种可能:a.等于diff[i](即max[i]-a[i+1]<diff[i])。b.等于max[i]-a[i+1]。
    // 通过上面分析，得到动态规划方法表达式：diff[i+1]=max(diff[i],max[i]-a[i+1]) 和 max[i+1]=max(max[i],a[i+1])
    // 数组最大的差值为diff[n-1]，n为数组的长度。
    // 对数组遍历一次，时间复杂度为O(n)，没有采用递归，因此性能提升。由于引入了两个额外数组，空间复杂度O(n)
    @Test
    public void testFindMaxDiff2() {

        int[] arr = {1, 2, 9, 7, 5, 4};

        int len = arr.length;
        int[] diff = new int[len];
        int[] max = new int[len];
        max[0] = arr[0];

        for (int i = 1; i < len; i++) {
            diff[i] = max(diff[i - 1], max[i - 1] - arr[i]);
            max[i] = max(max[i - 1], arr[i]);
        }

        System.out.println(diff[len - 1]);
    }

    // 从动态规划方法的计算公式中看出，在求解diff[i+1]时，只用到了diff[i]与max[i],而与数组diff和max中的其他数字无关，因此可以
    // 通过两个变量来记录值，降低空间复杂度。
    // 还可以通过求最大子数组之和求解。思路：申请一个长度为n-1的数组diff，满足diff[i]=a[i]-a[i+1],那么a[i]-a[j](0<i<j<n)就等价于
    // diff[i]+diff[i+1]+...+dif[j]。因此，求所有a[i]-a[j]组合的最大值就可以转换为求所有diff[i]+diff[i+1]+...+diff[j]组合的最大值。
    @Test
    public void testFindMaxDiff3() {
        int[] arr = {1, 2, 9, 7, 5, 4};
        int len = arr.length;
        int diff = 0;
        int max = arr[0];

        for (int i = 1; i < len; i++) {
            diff = max(diff, max - arr[i]);
            max = max(max, arr[i]);
        }

        System.out.println(diff);
    }

    // 问题：升序数组，可能又正、负、0，求数组中元素的绝对值最小的数。
    // 思路：分三种情况:a.第一元素为非负数，那么第一个元素即为结果。b.如果数组最后一个元素为非正数，那么最后一个元素为为结果。
    // c.数组中既有正数又有负数，首先找到正负分界点，如果分界点为0，那么0为结果，否则比较分界点左右数的绝对值确定最小数。
    // 找到分界点：a.可以遍历，找到非负值。时间复杂度为O(n)。
    // 二分查找分界点。
    // 思路：a.先找mid,若为0则找到。b.arr[mid]>0，若arr[mid-1]为0则返回，若小于0则为分界点。否则继续在数组左半部分查找。
    // c.arr[mid]<0，若arr[mid+1]为0则返回，若大于0则找到分界点，否则继续在数组有半部分查找。
    @Test
    public void testFindAbsMin() {
//        int[] arr = {-10, -5, -2, 7, 15, 50};
//        int[] arr = {-1, 7, 9, 12, 15, 50};
//        int[] arr = {2, 4, 6, 8, 27};
        int[] arr = {-13, -9, -7, -3};

        int first = arr[0];
        if (first >= 0) {// 都是正数
            System.out.println(first);
            return;
        }

        int end = arr[arr.length - 1];
        if (end <= 0) {// 都是负数
            System.out.println(end);
            return;
        }

        int absMin = findAbsMin(arr, 0, arr.length - 1);
        System.out.println(absMin);
    }

    private int findAbsMin(int[] arr, int begin, int end) {

        int mid = 0;
        while (true) {
            mid = (end + begin) / 2;
            int midValue = arr[mid];
            if (midValue == 0) {
                return 0;
            } else if (midValue > 0) {
                int midLeftValue = arr[mid - 1];
                if (midLeftValue == 0) {
                    return 0;
                } else if (midLeftValue < 0) {
                    int abs = Math.abs(midLeftValue);
                    return midValue < abs ? midValue : abs;
                } else {
                    end = mid - 1;
                }
            } else {
                int midRightValue = arr[mid + 1];
                if (midRightValue == 0) {
                    return 0;
                } else if (midRightValue > 0) {
                    int abs = Math.abs(midValue);
                    return abs > midRightValue ? midRightValue : abs;
                } else {
                    begin = mid + 1;
                }
            }
        }
    }

    // 问题描述：数组中含有重复元素，求指定两数在数组中出现位置的最小距离。
    // 思路：遇到n1时与上次遍历到n2时下标求差值然后和min_dist比较，遇到n2时和上次遍历到n1时下标求差值然后和min_dist比较。
    // 遍历一次数组，时间复杂度O(n)
    @Test
    public void testFindMinDis() {
//        int[] arr = {1, 2, 9, 4, 6, 7, 1, 9, 7, 5, 4};
        int[] arr = {1, 2, 9, 4, 9, 1, 7, 5, 4};

        int a = 1;
        int b = 9;

        int aIndex = -1;
        int bIndex = -1;
        int minDis = Integer.MAX_VALUE;
        for (int i = 0; i < arr.length; i++) {
            int tmp = arr[i];
            if (tmp == a) {
                aIndex = i;
                if (bIndex != -1) {
                    int b1 = i - bIndex;
                    if (b1 < minDis) {
                        minDis = b1;
                    }
                }
            } else if (tmp == b) {
                bIndex = i;
                if (aIndex != -1) {
                    int b1 = i - aIndex;
                    if (b1 < minDis) {
                        minDis = b1;
                    }
                }
            }
        }

        System.out.println(minDis);
    }

    // 问题描述：数组中相邻元素只差绝对值为1，查找指定数字所在位置。
    // 跳跃搜索法。通过分析数组特点，发现规律：用第一个元素与查询之比较，得到差值，由于数组中相邻元素差值为1，因此指定数出现位置必是
    // 第1+差值位置(下标为索引)。因为如果数组是递增的，那么经过差值次+1到达指定值。若不是递增的，那指定值必在差值+1后面。
    // 重复以上不断用差值跳跃，得到结果。
    // 减少对数组元素访问个数。
    @Test
    public void testFindFirstBit() {
        int[] arr = {3, 4, 5, 6, 5, 6, 7, 8, 9, 8};
        int a = 9;

        int i = 0;
        int length = arr.length;
        while (i < length) {
            if (arr[i] == a) {
                System.out.println(i);
                break;
            } else {
                i += Math.abs(arr[i] - a);
            }
        }
    }

    // 问题描述:arr[0,mid-1]和arr[mid,n-1]都是生序，对两者合并后有序。要求空间复杂度O(1)。元素可用<比较。
    // 假设都升序。思路：由于O(1)空间复杂度，因此不能用归并方法。最容易的就是插入排序，时间复杂度为O(n^2),空间复杂度为O(1)，但是没有利用条件。
    // 下面方法思路：遍历0~mid-1，与a[mid]比较，若a[mid]<a[i]，则交换值，接着找到交换后a[mid]在a[mid,n-1]中的具体位置(在a[mid,n-1]中
    // 进行插入排序)。插入排序实现:遍历a[mid~n-2]，若a[i+1]<a[i]则交换位置，然后i++。
    // 以上只能解决后半截有序，前半截随意，因为是用<判断交换并插入的。
    // 若其他情况，则可以将后半截先逆序，然后执行上面操作
    @Test
    public void testMergeSortedArray() {
        int[] arr = {5, 6, 7, 8, 1, 2, 3, 4};
//        int[] arr = {8, 7, 6, 5, 1, 2, 3, 4};
//        int[] arr = {8, 7, 6, 5, 4, 3, 2, 1};// 有问题
//        int[] arr = {4, 3, 2, 1,5, 6, 7, 8};// 有问题
//        int[] arr = {4, 3, 2, 1,8, 7, 6, 5};// 有问题
        int mid = 4;

        for (int i = 0; i <= mid - 1; i++) {
            if (arr[mid] < arr[i]) {
                CommonUtils.swap(arr, i, mid);
                findFitPosition(arr, mid);
            }
        }

        System.out.println(Arrays.toString(arr));
    }

    private void findFitPosition(int[] arr, int mid) {
        for (int i = mid; i < arr.length - 1; i++) {
            if (arr[i + 1] < arr[i]) {
                CommonUtils.swap(arr, i, i + 1);
            } else {
                break;
            }
        }
    }

    // 问题描述：两个整形数组升序a,b求交集。
    // 对于两个数组长度相当时：
    // 1. 二路归并法。以i，j遍历数组，若a[i]=b[j]则为交集，i++,j++，若a[i]>b[j]则j++，反只则i++，直到有一个到尾。
    // 2. 顺序遍历法。顺序遍历，使用hash表计数，为2则为交集。
    // 对于两个数组长度悬殊时：
    // 1. 遍历短的数组，对元素在长数组中二分查找。具体，两指针指向两数组末尾，从短数组取元素在长数组中二分查找，找到则为交集，并且将长数组
    // 的指针指向前一个位置(因为找到元素的之后位置都是大元素了)，若没找到，同样有一个位置，移动长数组指针向前。再从短数组取元素，直到两个
    // 数组有一个到头位置。
    // 2. 采用与1类似，但是每次查找在前一次查找的基础上进行，这样可以大大缩小查找表的长度。
    // 3. 采用与2类似，但是便利长度小的数组的方式不同，即从数组头部和尾部同时开始遍历，可以缩小查找表的长度。
    @Test
    public void testSame() {
        int[] a = {1, 3, 5, 8};
        int[] b = {3, 6, 8, 9};

        int i = 0;
        int j = 0;
        int aLen = a.length;
        int bLen = b.length;

        while (i < aLen && j < bLen) {
            int aValue = a[i];
            int bValue = b[j];

            if (aValue == bValue) {
                System.out.println(aValue);
                i++;
                j++;
            } else if (aValue > bValue) {
                j++;
            } else {
                i++;
            }
        }
    }

    // 问题描述：5个元素组成数组中除0，元素不重复，判断数组是否连续相邻。
    // 条件：1. 5个数值允许乱序。2. 0可以通赔任意数值。3. 0可以多次出现。 4. 全0算连续，只有一个非0算连续。
    // 思路：若没有0，要组成连续数列，最大和最小差距必须是4，存在0时，只要最大和最小差距小于4即可。所以找出最大和最小，时间复杂度O(n)，
    // 若最大-最小<=4，那么5个数值连续相邻。
    @Test
    public void testIfContinuous() {
//        int[] arr = {1, 3, 5, 8, 6};
        int[] arr = {2, 1, 3, 4, 5};

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int length = arr.length;
        for (int i = 0; i < length; i++) {
            if (arr[i] < min) {
                min = arr[i];
            } else if (arr[i] > max) {
                max = arr[i];
            }
        }

        System.out.println(min + " " + max);

        if (max - min <= length - 1) {
            System.out.println("连续");
        } else {
            System.out.println("不连续");
        }
    }

    int reverseCount;

    // 问题描述：数组a，若a[i]>a[j](i<j)则a[i]和a[j]被成为一个反序。
    // 分治归并法。在归并排序的基础上额外使用一个计数器记录。即当进行合并时，若左边的大准备放入排序好数组时,加上对应的逆序个数。
    // 与归并排序有着相同时间复杂度O(nlogn)
    @Test
    public void testReverseCount() {

        int[] arr = {5, 8, 3, 6};
        mergeSort(arr, 0, arr.length - 1);
        System.out.println(reverseCount);
    }

    // 5, 8, 3, 6  [0,1,3]
    // [5, 8] [3, 6]  [0,1,0] [2,3,2]
    // [5]
    // [8]
    // [5, 8]
    // [3]
    // [6]
    // [3, 6]
    // 3, 5, 6, 8
    private void mergeSort(int[] arr, int begin, int end) {
        if (begin < end) {
            int mid = (end + begin) / 2;
            mergeSort(arr, begin, mid);
            mergeSort(arr, mid + 1, end);
            merge(arr, begin, mid, end);
        }
    }

    private void merge(int[] arr, int begin, int mid, int end) {
        int i, j, k, n1, n2;
        n1 = mid - begin + 1;
        n2 = end - mid;
        int[] L = new int[n1];
        int[] R = new int[n2];
        for (i = 0, k = begin; i < n1; i++, k++) {
            L[i] = arr[k];
        }
        for (i = 0, k = mid + 1; i < n2; i++, k++) {
            R[i] = arr[k];
        }
        for (k = begin, i = 0, j = 0; i < n1 & j < n2; k++) {
            if (L[i] < R[j]) {
                arr[k] = L[i++];
            } else {
                reverseCount += mid - i + 1;// 因为都是升序的，i都比j位置大了，那么i到mid的数都比j大，所以数量是加上(mid-i)+1
                arr[k] = R[j++];
            }
        }

        if (i < n1) {
            for (; i < n1; i++, k++) {
                arr[k] = L[i];
            }
        }
        if (j < n2) {
            for (; j < n2; j++, k++) {
                arr[k] = R[j];
            }
        }
    }

    // 问题描述：3个升序数组。分别找一个元素，使得组成的三元组距离最小：max(|a[i]-b[j]|,|a[i]-c[k]|,|[b[j]-c[k]|)。
    // 方法一：蛮力法。分别遍历三个数组元素，求出他们的距离比对最小值。i,j,k分别从0开始依次遍历。时间复杂度O(a*b*c)
    // 最小距离法。假设当前遍历到元素ai,bj,ck，且ai<bj<ck，此时他们的最小距离肯定为Di=ci-ai(依据上面公式)。分如下3种讨论：
    // 1. 若接下来求ai,bi,c(i+1)的距离，由于c(i+1)>ci，新距离D(i+1)=c(i+1)-ai，显然D(i+1)>=Di，因此D(i+1)不可能为最小距离。
    // 2. 接下来求ai,b(i+1),ci的距离，由于b(i+1)>bi，若b(i+1)<=ci(升了一级还比c小，那与ai的差值，还是不如ci与ai的差值)，此时
    // 他们距离仍为D(i+1)=ci-ai；若b(i+1)>ci，那么此时距离为D(i+1)=b(i+1)-ai，显然D(i+1)>=Di，因此D(i+1)不可能为最小距离。
    // 3. 接下来求a(i+1),bi,ci的距离，若a(i+1)<|ci-ai|+ci==两边同时由ci减去==>
    // ci-a(i+1)<ci-|ci-ai|-ci==>ci-a(i+1)<|ci-ai|==>新距离D(i+1)<Di,因此D(i+1)有可能是最小距离。
    // 综上，在求最小距离时只需要考虑第三种情况。思路：从三个数组第一个元素开始，先求出minDist，接着找出这3个数中最小数下标++，接着
    // 求三元素距离，若小于minDist则替换，直到一个数组结束。
    // 最多只需对三个数组分别遍历一遍，时间复杂度(a+b+c)
    @Test
    public void testMinDistance() {
        int[] a = {1, 2, 3, 4};
        int[] b = {5, 6, 7, 8};
        int[] c = {9, 10, 11, 12};

        minDistance(a, b, c);
    }

    private void minDistance(int[] a, int[] b, int[] c) {
        int aLen = a.length;
        int bLen = b.length;
        int cLen = c.length;

        int aIndex = 0;
        int bIndex = 0;
        int cIndex = 0;

        int min = Integer.MAX_VALUE;
        int i = 0, j = 0, k = 0;
        while (i < aLen && j < bLen && k < cLen) {
            int aVal = a[i];
            int bVal = b[j];
            int cVal = c[k];
            int max = max(max(Math.abs(aVal - bVal), Math.abs(aVal - cVal)), Math.abs(cVal - bVal));
            if (max < min) {
                aIndex = i;
                bIndex = j;
                cIndex = k;
                min = max;
            }

            if (aVal < bVal) {
                if (aVal < cVal) {
                    i++;
                } else {
                    k++;
                }
            } else {
                if (bVal < cVal) {
                    j++;
                } else {
                    k++;
                }
            }
        }

        System.out.println(min + " " + aIndex + " " + bIndex + " " + cIndex);
    }
}
