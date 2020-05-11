package com.wolf.algorithm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Description: Trie树的结构都是采用26叉树进行组织的，每个节点对应一个字母，查找时，一个字母一个字母匹配，
 * 算法时间复杂度为单词的长度n，效率高。
 *
 * @author 李超
 * @date 2020/01/20
 */
public class TrieNodeOpt {

    private void insertNode(TrieNode root, String wd) {
        if (wd.length() == 0) {
            return;
        }

        if (root == null) {
            root = new TrieNode();
        }

        char swd[] = wd.toCharArray();
        Arrays.sort(swd);
        TrieNode tmp = root;

        for (char curChar : swd) {
            int index = getIndex(curChar);
            if (tmp.next[index] == null) {
                TrieNode trieNode = new TrieNode();
                tmp.next[index] = trieNode;
            }
            tmp = tmp.next[index];
        }

        tmp.brothers.add(wd);
    }

    private int getIndex(char c) {
        return c - 'a';
    }

    private boolean searchNode(TrieNode root, String wd) {
        char chars[] = wd.toCharArray();
        Arrays.sort(chars);
        int i = 0;

        while (i < wd.length()) {
            if (root.next[getIndex(chars[i])] != null) {
                root = root.next[getIndex(chars[i])];
                i++;
            } else {
                break;
            }
        }
        if (i == wd.length()) {// 所有元素相等才叫兄弟
            for (String brother : root.brothers) {
                System.out.print(brother + " ");
            }
            System.out.println();
            return true;
        }
        return false;
    }

    // 问题描述：给一个单词，如果通过交换单词中字母的顺序可以得到另外一个单词，那么称为兄弟单词，如army和mary互为兄弟单词。
    // 求输入的单词,根据给定的字典找出输入单词有哪些兄弟单词。
    // 方案：使用trie树
    @Test
    public void findBrother() {
        TrieNode root = new TrieNode();
        insertNode(root, "hehao");
        insertNode(root, "ehaoh");
        insertNode(root, "haohe");
        insertNode(root, "aoheh");
        insertNode(root, "facri");
        insertNode(root, "et");

        searchNode(root, "oheha");
    }

    class TrieNode {
        private List<String> brothers = new ArrayList<>();// 所有字符相同
        private TrieNode next[] = new TrieNode[26];// 26个索引，0~25代表a~z

        public List<String> getBrothers() {
            return brothers;
        }

        public void setBrothers(List<String> brothers) {
            this.brothers = brothers;
        }

        public TrieNode[] getNext() {
            return next;
        }

        public void setNext(TrieNode[] next) {
            this.next = next;
        }
    }
}
