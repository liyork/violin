package com.wolf.algorithm;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Description:
 *
 * @author 李超
 * @date 2020/01/17
 */
public class StringOpt {

    // 问题描述：把一个句子的单词进行反转。如"how are you"=>"you are how"
    // 第一次对整个字符串字符反转，"uoy era woh"，已经实现单词顺序反转，只不过每个单词字符的顺序反了，再对每个单词进行字符串反转。
    @Test
    public void testSwapWords() {
        String s = "how are you";
        char[] chars = s.toCharArray();

        int length = chars.length;
        swapChars(chars, 0, length - 1);

        int i = 0, j = 0;
        for (; j < length; j++) {
            if (chars[j] == ' ') {
                swapChars(chars, i, j - 1);
                i = j + 1;
            }
        }

        swapChars(chars, i, j - 1);

        System.out.println(chars);
    }

    private void swapChars(char[] chars, int i, int j) {
        while (i < j) {
            char tmp = chars[i];
            chars[i] = chars[j];
            chars[j] = tmp;
            i++;
            j--;
        }
    }

    // 判定两字符串是否由相同字符组成，位置可以不同。如"aaaabbc","abcbaaa"
    // 方法一：排序法。排序后比较字符。时间复杂度用快排,O(nlogn)
    @Test
    public void testCompare1() {
        String a = "abc";
        String b = "acb";

        byte[] aBytes = a.getBytes();
        Arrays.sort(aBytes);

        byte[] bBytes = b.getBytes();
        Arrays.sort(bBytes);

        for (int i = 0; i < aBytes.length; i++) {
            if (aBytes[i] != bBytes[i]) {
                System.out.println("not equals.");
            }
        }
    }

    // 方法二：空间换时间。降低时间复杂度，通过额外增加存储空间达到优化算法效果。假设字符串中只有ASCII字符，ASCII字符共有256个(0~255)
    // 申请256数组记录各字符出现个数。时间复杂为O(n)
    @Test
    public void testCompare2() {
        String a = "abc";
        String b = "bac";

        int length = 256;
        int[] arr = new int[length];

        char[] aChars = a.toCharArray();
        int aLen = aChars.length;
        for (int i = 0; i < aLen; i++) {
            arr[i]++;
        }

        char[] bChars = b.toCharArray();
        int bLen = bChars.length;
        for (int i = 0; i < bLen; i++) {
            arr[i]--;
        }

        for (int i = 0; i < length; i++) {
            if (arr[i] != 0) {
                System.out.println("not equals");
                break;
            }
        }
    }

    // 问题描述：删除字符串中重复的字符，如"good"得到"god"
    // 方法一：蛮力法。双重循环，若重复则设定'\0'，最后把'\0'去掉。由于双重循环，因此时间复杂度O(n^2)，n为字符串长度。
    // 方法二：空间换时间。思路：由于常见的字符只有256个，假设此题字符串中不同的字符个数最多为256个，申请一个256的int数组，
    // 记录每个字符出现的次数，遍历时对应的位置若为0则设1，若为1则是重复，设为'\0'，最后去掉所有'\0'。遍历一次数组，时间复杂
    // 度为O(n)，但需额外申请256个字符大小的空间。由于申请的数组用来记录一个字符是否出现，只需要1bit即可，因此作为更好的方案，
    // 可以只申请长度8的int类型的数组，由于每个int类型占32bit，因此一共256bit。
    @Test
    public void testRemoveDuplicate2() {
        String a = "abac";

        char[] chars = a.toCharArray();
        int length = chars.length;
        int[] flags = new int[8 + 1];// 8个32bit的int，8*32bit=256bit。存放1~256的数，256/32=8，所以要8+1个内容防止溢出
        int i;
        for (i = 0; i < length; i++) {
            int index = (int) chars[i] / 32;// 定位index，256个数字被分成4组，每组32个数。
            int shift = (int) chars[i] % 32;// 32位中的位置
            if ((flags[index] & (1 << shift)) != 0) {// 移动取模32位后的位置，即此char在int中的位
                chars[i] = '\0';
            }
            flags[index] |= (1 << shift);// 设定值
        }

        int l = 0;
        for (i = 0; i < length; i++) {
            if (chars[i] != '\0') {
                chars[l++] = chars[i];
            }
        }

        System.out.println(new String(chars, 0, l));
    }

    // 方法三：正则。(?s)(.)(?=.*\\1)
    // 与方法一类似，不过方法一去掉了字符串中后面重复的字符，而这种方法去掉了前面出现的重复字符，因此正则替换后，字符串中字符的出现
    // 顺序将和源字符串中出现的顺序不一致，可以通过对字符串进行反转，然后正则，然后再反转。
    @Test
    public void testRemoveDuplicate3() {
        String a = "abac";

        StringBuffer sb = new StringBuffer(a);
        a = sb.reverse().toString();
        a = a.replaceAll("(?s)(.)(?=.*\\1)", "");
        StringBuffer sb1 = new StringBuffer(a);
        System.out.println(sb1.reverse().toString());
    }

    // 统计字符中单词数。单词数可以由空格出现的次数决定(连续空格作为一次空格，一行开头空格不统计)
    // 遍历，若当前字符为非空格，他前面的字符是空格，则表示"新单词开始"，count++，若当前字符为非空格而前面字符也是非空格，则仍是
    // 原单词的继续。前面一个字符是否空格可以从变量pre看出来，若pre为' '则表示前一个字符是空格。
    @Test
    public void testCountWord() {
        String a = " a1 b11  c d";

        int count = 0;
        char pre = ' ';
        int length = a.length();

        for (int i = 0; i < length; i++) {
            char tmp = a.charAt(i);
            if (pre == ' ' && tmp != ' ') {// 单词开始
                count++;

            }
            pre = tmp;
        }

        System.out.println(count);
    }

    // 问题描述：针对1、2、2、3、4、5数字，打印不同的排列，要求4不能在第三位，3和5不能相连。
    // 打印数组的排列组合方式最简单的方法是递归，但数字有重复且有要求位置。
    // 换思维：把此问题转换为图的遍历，对6个点两两相连可以组成一个无向连通图，6个数字全排列等价于从图中各个节点出发深度遍历此图所有可能
    // 路径所组成的数字集合。
    // 步骤：用6个数字作为节点，构造一个无向连通图。3和5不连通。分别从6节点出发对图做深度优先遍历。每次便利完所有节点，把遍历的路径组合
    // 记录下来，若数字第三位不是4则加入结果集。
    @Test
    public void testGetAllCombinations() {
        int[] numbers = {1, 2, 2, 3, 4, 5};
//        int[] numbers = {1, 2, 3};

        int[][] graph = buildGraph(numbers);
        Set<String> arrange = getAllCombinations(numbers, graph);
        System.out.println(arrange);
    }

    // 无向连通图，使用数组实现，填充下标，有额外冗余，如0元素
    private int[][] buildGraph(int[] numbers) {
        int length = numbers.length;
        int[][] graph = new int[length][length];

        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length; j++) {
                if (i == j) {
                    graph[i][j] = 0;
                } else {
                    graph[i][j] = 1;
                }
            }
        }
        // 不可达
        graph[3][5] = 0;
        graph[5][3] = 0;

        return graph;
    }

    private Set<String> getAllCombinations(int[] numbers, int[][] graph) {

        int length = numbers.length;
        int[] visited = new int[length];
        Set<String> result = new HashSet<>();
        StringBuilder combination = new StringBuilder();

        for (int i = 0; i < length; i++) {// 从所有节点深度遍历
            depthTravel(i, numbers, graph, visited, combination, result);
        }

        return result;
    }

    // 从start开始深度遍历，如：得到1开始的所有可能组合如123,132
    private void depthTravel(int start, int[] numbers, int[][] graph, int[] visited, StringBuilder combination, Set<String> result) {
        visited[start] = 1;
        combination.append(numbers[start]);

        int length = numbers.length;
        if (combination.length() == length) {// 产生结果
            if (combination.indexOf("4") != 2) {
                result.add(combination.toString());
            }
        } else {
            for (int i = 0; i < length; i++) {
                if (graph[start][i] == 1 && visited[i] == 0) {
                    depthTravel(i, numbers, graph, visited, combination, result);
                }
            }
        }

        // 清理(前一次可以继续)
        combination.deleteCharAt(combination.length() - 1);
        visited[start] = 0;
    }

    // 问题描述：字符串中所有字符不重复，输出所有组合。n个字符，根据排列组合性质，一共输出(2^n)-1种组合
    // 递归法，遍历字符串，每个字符只能取或不取。若取则放入结果中，到达指定长度，输出字符串。
    @Test
    public void testCombine1() {
        String s = "abc";
        char[] c = s.toCharArray();
        StringBuilder stringBuilder = new StringBuilder();
        int length = c.length;
        for (int i = 1; i <= length; i++) {// 1~length长度的组合
            combineRecursive(c, 0, i, stringBuilder);
        }
    }

    // begin：起始元素
    // end：组合长度
    // 从begin元素开始，包含begin值，剩余交给下个元素开始的组合。直到若begin到数组末尾或者len满足则返回。
    // 再以begin+1元素开始，重复上述动作。
    private void combineRecursive(char[] c, int begin, int len, StringBuilder sb) {
        if (len == 0) {// 长度满足输出
            System.out.print(sb + "  ");
            return;
        }

        if (begin == c.length) {// 起始已经到头，但是长度还不够，本次作废
            return;
        }

        sb.append(c[begin]);
        combineRecursive(c, begin + 1, len - 1, sb);// 包含begin元素，长度-1，从下个元素继续
        sb.deleteCharAt(sb.length() - 1);// 删除当前元素，便于下面继续
        combineRecursive(c, begin + 1, len, sb);// 下个位置开始重新组合
    }

    // 采用递归，当n不大时，效率没问题，但比较大时，效率很差，因为栈调用次数约为2^n，尾递归优化后也有2^(n-1)。
    // 提高效率，结合本题特性，构造一个长度为n的0/1字符串(或二进制数)表示输出结果中是否包含某个字符，如"001"表示结果不含a、b，含c，
    // 输出结果为c，而"101"=>ac。原题就是要求输出"001"到"111"这2^n-1个组合对应的字符串。
    @Test
    public void testCombine2() {
        String s = "abc";
        char[] c = s.toCharArray();
        combine2(c);
    }

    private void combine2(char[] c) {
        if (c == null) {
            return;
        }

        int len = c.length;
        boolean[] used = new boolean[len];// index处元素是否使用
        char[] resultArr = new char[len];
        int resultIndex = len;

        while (true) {
            int index = 0;
            while (used[index]) {// 从index开始，只要index已使用则变更为未使用，同时resultIndex恢复+1
                used[index] = false;
                ++resultIndex;
                if (++index == len) {
                    return;
                }
            }
            used[index] = true;// 设定使用
            resultArr[--resultIndex] = c[index];// 正向index值，反向设定resultArr
//            System.out.println(Arrays.toString(resultArr));
//            System.out.println(Arrays.toString(used));
            System.out.println(new String(resultArr).substring(resultIndex) + " ");
        }
    }

}
