package com.wolf.algorithm.tree;

import java.util.Arrays;
import java.util.LinkedList;

/**
 * Description: 二叉排序树又称二叉查找树。
 * 它或者是一棵空树，或者是具有下列性质的二叉树：
 * 1. 若左子树不为空，则左子树上所有节点的值均小于它的根节点的值。
 * 2. 若右子数不为空，则右子树上所有节点的值均大于它的根节点的值。
 * 3. 左右子数分别为二叉排序树。
 *
 * @author 李超
 * @date 2020/01/18
 */
public class BinaryTree {

    private Node root;

    public void buildTree(int... data) {
        for (int i = 0; i < data.length; i++) {
            insert(data[i]);
        }
    }

    public void insert(int data) {
        Node node = new Node(data);
        if (root == null) {
            root = node;
            return;
        }

        Node cur = root;
        while (true) {
            if (cur.getData() > data) {
                Node left = cur.left;
                if (left == null) {
                    cur.left = node;
                    break;
                }
                cur = cur.left;
            } else {
                Node right = cur.right;
                if (right == null) {
                    cur.right = node;
                    break;
                }
                cur = cur.right;
            }
        }
    }

    // 前、中、后续遍历，是针对的根节点而言，是先打印、中间打印、后打印根节点。

    // 中序遍历：第一步，中序遍历根节点的左子树，第二步，访问根节点，第三步，中序遍历根节点的右子树
    public void inOrder() {
        if (root == null) {
            return;
        }
        inOrder(root);
    }

    private void inOrder(Node node) {
        Node left = node.getLeft();
        if (left != null) {
            inOrder(left);
        }

        System.out.println(node.getData());

        Node right = node.getRight();
        if (right != null) {
            inOrder(right);
        }
    }

    // 先序遍历：第一步，访问根节点，第二步，先序遍历根节点的左子树，第三步，先序遍历根节点的右子树
    public void preOrder() {
        if (root == null) {
            return;
        }
        preOrder(root);
    }

    private void preOrder(Node node) {
        System.out.println(node.getData());

        Node left = node.getLeft();
        if (left != null) {
            preOrder(left);
        }

        Node right = node.getRight();
        if (right != null) {
            preOrder(right);
        }
    }

    // 后序遍历：第一步，后序遍历根节点的左子树，第二步，后序遍历根节点的右子树，第三步，访问根节点
    public void postOrder() {
        if (root == null) {
            return;
        }
        postOrder(root);
    }

    private void postOrder(Node node) {
        Node left = node.getLeft();
        if (left != null) {
            postOrder(left);
        }

        Node right = node.getRight();
        if (right != null) {
            postOrder(right);
        }

        System.out.println(node.getData());
    }

    // 层序遍历。从二叉树的第一层(根节点)开始，从上至下逐层遍历，在同一层中，则按从左到右的顺序对节点访问。
    // 思路：使用队列。先将根节点放入队列，每次从队列取出一个节点打印值，若这节点有子节点，则放入队列尾，若有又右节点再放入队列尾，一直重复直到队列空。
    public void layerTraverse() {
        if (root == null) {
            return;
        }
        LinkedList<Node> list = new LinkedList<>();
        list.add(root);

        while (!list.isEmpty()) {
            Node poll = list.pop();
            System.out.println(poll.getData());

            Node left = poll.getLeft();
            if (left != null) {
                list.add(left);
            }

            Node right = poll.getRight();
            if (right != null) {
                list.add(right);
            }
        }
    }

    // 根据先序和中序遍历，求得后序遍历
    // 由于先序遍历的规则为根、左、右，因此得到第一元素为根，再看中序遍历为左、根、右，再根据先序的根即可找到左右子树。之后递归求解左子树和右子树。
    // 过程：
    // 1. 确定树的根节点。根是当前树中所有元素在先序遍历中最先出现的。
    // 2. 求解树的子树。找到根在中序遍历的位置，位置左边是左子树，右边是右子树。
    // 3. 对二叉树的左、右子树分别进行步骤1、2，直到没有左右子树为止。
    // 注意：插入顺序对生成的树是有影响的，所以先插入根，再插入左和右
    public void postOrderByPreOrderAndInOrder(int[] preOrder, int[] inOrder) {
        buildTree(preOrder, inOrder);
        postOrder();
    }

    //todo-my 后续可以改成start、end省去总是创建数组，已经从string改成array，就先后期再改了。。
    private void buildTree(int[] preOrder, int[] inOrder) {
        if (inOrder.length == 0) {// 一边为空则返回
            return;
        }

        if (inOrder.length == 1) {// 唯一值,直接插入
            insert(inOrder[0]);
            return;
        }

        int root = preOrder[0];// 先序是根、左、右，先取得根
        insert(root);
        int rootIndexInOrder = findInArray(inOrder, root);// 根元素在中序的位置
        // 左边
        int[] leftInOrder = subArray(inOrder, 0, rootIndexInOrder);
        int[] leftPreOrder = subInAll(leftInOrder, preOrder);// 依据中序元素找到对应的先序元素
        buildTree(leftPreOrder, leftInOrder);
        // 右边
        int[] rightInOrder = subArray(inOrder, rootIndexInOrder + 1, inOrder.length);
        int[] rightPreOrder = subInAll(rightInOrder, preOrder);
        buildTree(rightPreOrder, rightInOrder);
    }

    private int findInArray(int[] inOrder, int root) {
        for (int i = 0; i < inOrder.length; i++) {
            if (inOrder[i] == root) {
                return i;
            }
        }
        throw new RuntimeException("not found element:" + root + " in array:" + Arrays.toString(inOrder));
    }

    private int[] subArray(int[] inOrder, int left, int right) {
        int[] result = new int[right - left];

        for (int resultIndex = 0, i = left; i < right; i++) {
            result[resultIndex++] = inOrder[i];
        }
        return result;
    }

    // 依据subString中元素截取allString
    private int[] subInAll(int[] sub, int[] all) {
        if (sub.length == 0) {
            return sub;
        }
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < sub.length; i++) {
            int tmp = sub[i];
            int index = findInArray(all, tmp);
            if (min > index) {
                min = index;
            }

            if (index > max) {
                max = index;
            }
        }
        return subArray(all, min, max + 1);
    }

    int max = 0;

    // 问题描述：节点的距离是指这两个节点之间边的个数。求一棵二叉树中距离最远的两个节点之间的距离。
    // 一般而言，对二叉树的操作通过递归方法实现比较容易。求最大距离的思路：求左子树距根节点的最大距离leftMaxDistance，求右子树距根节点的
    // 最大距离rightMaxDistance，那么二叉树中节点的最大距离maxDistance=leftMaxDistance+rightMaxDistance
    public int findAllNodeDistance() {
        findAllNodeDistance1(root);
//         findAllNodeDistance2(root);

        return max;
    }

    // 向上汇总法。底层从0开始，向上汇总时，无孩子或单孩子计算maxDis时除了左+右还要+1，俩孩子计算maxDis则左+右
    private int findAllNodeDistance1(Node node) {
        if (node == null) {
            return 0;
        }

        int leftDistance = findAllNodeDistance1(node.getLeft());
        int rightDistance = findAllNodeDistance1(node.getRight());

        int maxDis = leftDistance + rightDistance;

        if (node.getLeft() == null || node.getRight() == null) {
            maxDis = maxDis + 1;
        }

        if (maxDis > max) {
            max = maxDis;
        }

        return maxDis;
    }

    // 左右较大法。
    // 节点的leftMaxDistance值来自左节点的leftMaxDistance和rightMaxDistance的最大值然后+1
    // 节点的rightMaxDistance值来自右节点的leftMaxDistance和rightMaxDistance的最大值然后+1
    // max为leftMaxDistance+rightMaxDistance。
    // max还是为当前节点的左子树+右子树，只不过节点左子树的值来自左孩子的左右子树最大值
    private void findAllNodeDistance2(Node node) {
        if (node == null) {
            return;
        }

        if (node.left != null) {
            findAllNodeDistance2(node.left);
        }

        if (node.right != null) {
            findAllNodeDistance2(node.right);
        }

        // 比较左节点的左右最大值
        if (node.left != null) {
            node.leftMaxDistance = max(node.left.leftMaxDistance, node.left.rightMaxDistance) + 1;
        }

        if (node.right != null) {
            node.rightMaxDistance = max(node.right.leftMaxDistance, node.right.rightMaxDistance) + 1;
        }

        if (node.leftMaxDistance + node.rightMaxDistance > max) {
            max = node.leftMaxDistance + node.rightMaxDistance;
        }
    }

    private int max(int leftMaxDistance, int rightMaxDistance) {
        return leftMaxDistance > rightMaxDistance ? leftMaxDistance : rightMaxDistance;
    }

    class Node {
        private int data;
        private Node left;
        private Node right;
        private int leftMaxDistance;
        private int rightMaxDistance;

        public Node(int data) {
            this.data = data;
        }

        public int getData() {
            return data;
        }

        public void setData(int data) {
            this.data = data;
        }

        public Node getLeft() {
            return left;
        }

        public void setLeft(Node left) {
            this.left = left;
        }

        public Node getRight() {
            return right;
        }

        public void setRight(Node right) {
            this.right = right;
        }
    }
}
