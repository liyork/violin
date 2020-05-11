package com.wolf.algorithm.tree;

import org.junit.Test;

/**
 * Description: 二叉树
 *
 * @author 李超
 * @date 2020/01/18
 */
public class BinaryTreeOpt {

    @Test
    public void testInOrder() {
        BinaryTree bt = newTree();
        bt.inOrder();
    }

    @Test
    public void testPreOrder() {
        BinaryTree bt = newTree();
        bt.preOrder();
    }

    @Test
    public void testPostOrder() {
        BinaryTree bt = newTree();
        bt.postOrder();
    }

    @Test
    public void testInsert() {
        BinaryTree bt = newTree();
        bt.inOrder();
    }

    private BinaryTree newTree() {
        BinaryTree bt = new BinaryTree();
        bt.buildTree(5, 3, 8, 1, 4, 7);
        return bt;
    }

    @Test
    public void testLayerTraverse() {
        BinaryTree bt = newTree();
        bt.layerTraverse();
    }

    @Test
    public void testPostOrderByPreOrderAndInOrder() {
        BinaryTree bt = new BinaryTree();
//        bt.postOrderByPreOrderAndInOrder(new int[]{5, 3, 2, 1, 4}, new int[]{1, 2, 3, 4, 5});
        bt.postOrderByPreOrderAndInOrder(new int[]{5, 3, 1, 4, 8, 7}, new int[]{1, 3, 4, 5, 7, 8});
    }

    @Test
    public void testFindAllNodeDistance() {
        BinaryTree bt = new BinaryTree();
        bt.buildTree(5, 2, 8, 1, 4, 3, 7);
        int allNodeDistance = bt.findAllNodeDistance();
        System.out.println(allNodeDistance);
    }

}
