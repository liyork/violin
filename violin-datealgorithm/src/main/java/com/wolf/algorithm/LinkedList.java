package com.wolf.algorithm;

import org.junit.Test;

/**
 * Description: 链表相关结构+算法
 * 可以用任意一组存储单元来存储单链表中的数据元素(存储单元可以不连续)，还需存储指示其直接后继元素的指针。
 * 这两部分信息组成的数据元素称为结点。n个结点链在一起成为链表。
 * <br/> Created on 2018/5/30 15:28
 *
 * @author 李超
 * @since 1.0.0
 */
public class LinkedList {

    private Node head;

    private void add(Node node) {
        if (null == head) {
            head = node;
            return;
        }

        Node nextNode = head;
        while (nextNode.getNext() != null) {
            nextNode = nextNode.getNext();
        }

        nextNode.setNext(node);
    }

    // i=[1,length]
    private boolean delete(int numTh) {
        if (numTh < 1 || numTh > length()) {
            return false;
        }

        if (numTh == 1) {
            head = head.getNext();
            return true;
        }

        int i = 2;
        Node preNode = head;
        Node curNode = preNode.getNext();
        while (curNode != null) {
            if (i == numTh) {
                preNode.setNext(curNode.getNext());
                return true;
            }
            //向后移动
            preNode = curNode;
            curNode = curNode.getNext();
            i++;
        }
        return false;
    }

    // 删除指定节点，不能使用head
    private boolean deleteNode(Node node) {
        if (node == null) {
            return false;
        }

        Node next = node.getNext();
        // 若是尾节点，无法删除，因为删除后无法使其前驱点的next指向null
        if (next == null) {
            return false;
        }

        node.setItem(next.getItem());
        node.setNext(next.getNext());

        return true;
    }

    private int length() {
        int count = 0;
        Node tmp = head;
        while (tmp != null) {
            count++;
            tmp = tmp.getNext();
        }
        return count;
    }

    private void printList() {
        Node curNode = head;
        while (curNode != null) {
            System.out.println(curNode);
            curNode = curNode.getNext();
        }
    }

    // 从头开始反转链表指向，记得先记录下next然后反转。
    private void reverse() {
        if (null == head) {
            return;
        }

        Node preNode = null;
        Node curNode = head;
        Node nextNode;
        while (curNode != null) {
            nextNode = curNode.getNext();//记录下一个
            curNode.setNext(preNode);//翻转
            preNode = curNode;//移动前一个所在位置
            curNode = nextNode;//移动当前所在位置
        }

        head = preNode;
    }

    // 快慢指针，快的每次都比慢的多走一步，只要有环，早晚相碰。
    private boolean hasCycle() {
        if (null == head) {
            return false;
        }

        // 只有一个元素
        if (head.getNext() == null) {
            return false;
        }

        Node first = head;
        Node second = head;

        while (second != null) {
            first = first.getNext();//走一步

            Node tmp = second.getNext();
            if (null == tmp) {
                return false;
            }

            second = tmp.getNext();//走两步

            if (first.equals(second)) {
                return true;
            }
        }

        return false;
    }

    // 找到环入口点
    // 当快慢指针相遇时，慢指针没有遍历完链表，而快指针已经在环内循环n圈(n>=1的正数)了。假设慢指针走了s步，则快指针走了2s步，
    // 快指针步数还等于s加上在环上多转的n圈(从入口点算的n圈)，设环长为r。
    // 2s = s + nr => s = nr
    // 再拆分。
    // 设整个链表长L，起点到环入口点距离为a，环入口与相遇点为x。
    // s = a + x 带入 s = nr => a + x = nr => a + x = (n-1)r + r => a + x = (n-1)r + (L - a) =>
    // a = (n-1)r + (L - a - x) 。 (L - a - x)为相遇点到环入口点的距离。
    // 上述表达为：从链表头到环入口  等于  (n-1)循环内环+相遇点到环入口。
    // "(n-1)循环内环"是重复，可以忽略，于是可以在链表头和相遇点设定指针，每次各走1步，两指针必定相遇，且相遇第一点为环入口
    private Node findCycleEntryPoint() {
        if (null == head) {
            return null;
        }

        // 只有一个元素
        if (head.getNext() == null) {
            return null;
        }

        Node first = head;
        Node second = head;

        while (second != null) {
            first = first.getNext();//走一步

            Node tmp = second.getNext();
            if (null == tmp) {
                return null;
            }

            second = tmp.getNext();//走两步

            if (first.equals(second)) {
                break;
            }
        }

        second = head;
        while (first != second) {
            first = first.getNext();
            second = second.getNext();
        }

        return first;
    }

    private void sort() {
        Node<Integer> curNode = head;
        Node<Integer> nextNode;
        Integer tmp;

        while (curNode != null) {// 可以判断next不为空，因为当前curNode第一次肯定不为空，总是判断next也就总是预判
            nextNode = curNode.getNext();
            while (nextNode != null) {
                if (curNode.getItem().compareTo(nextNode.getItem()) > 0) {
                    tmp = curNode.getItem();
                    curNode.setItem(nextNode.getItem());
                    nextNode.setItem(tmp);
                }
                nextNode = nextNode.getNext();
            }
            curNode = curNode.getNext();
        }
    }

    // 外循环依次遍历，内循环从外循环处开始依次比对外循环当前值，有则删除。
    private void removeDuplicate() {
        Node cur = head;
        Node pre;

        while (cur != null) {
            pre = cur;
            while (pre.getNext() != null) {
                if (cur.getItem().equals(pre.getNext().getItem())) {
                    pre.setNext(pre.getNext().getNext());
                } else {
                    pre = pre.getNext();
                }
            }
            cur = cur.getNext();
        }
    }

    // 倒数第k个元素，前后指针。
    private Node findBackwardK(int k) {
        if (head == null) {
            return null;
        }

        Node result = head;
        Node forward = head;
        while (k-- > 1 && forward != null) {// 先走k-1步
            forward = forward.getNext();
        }
        if (forward == null) {
            return null;
        }

        while (true) {
            forward = forward.getNext();
            if (forward == null) {
                return result;
            } else {
                result = result.getNext();
            }
        }
    }

    // 逆序打印链表，使用递归，先输出下个节点然后再输出本节点，就反过来打印了。(方法调用本身就是入栈操作,所以用递归就省去额外栈空间了)
    private void printReverse(Node head) {
        if (head == null) {
            return;
        }
        printReverse(head.getNext());
        System.out.println(head.getItem());
    }

    // 快慢指针，快指针先走，每次走2步，慢指针后走，每次走1步，
    // 快指针第一步到空表明链表长度为奇数，慢指针即为中间，快指针第二步为空表明链表长度为偶数，慢指针的next为中间。因为开始就指向第一个数了
    private Node findMid() {
        if (null == head) {
            return null;
        }

        Node first = head;
        Node second = head;

        while (true) {
            second = second.getNext();
            if (null == second) {// length=奇数
                return first;
            }

            second = second.getNext();//走两步
            if (null == second) {// length=偶数
                return first.getNext();
            }

            first = first.getNext();// 走一步
        }
    }

    // 链表相交(有同样的1-n个尾部节点)，一定有相同的尾节点。时间复杂度:O(linkedList1.length+linkedList2.length)
    private boolean isIntersect(LinkedList linkedList2) {

        if (head == null || linkedList2 == null || linkedList2.head == null) {
            return false;
        }

        Node node1 = head;
        while (node1.getNext() != null) {
            node1 = node1.getNext();
        }

        Node node2 = linkedList2.head;
        while (node2.getNext() != null) {
            node2 = node2.getNext();
        }

        return node1 == node2;
    }

    // 找到相交链表(有同样的1-n个尾部节点)的第一个相交点
    // 假设len1>len2，将node1从head移动len1-len2步，到len1-len2个节点，然后node1和node2到相交点就是同样距离了。
    // 时间复杂度O(len1+len2)
    private Node findFirstIntersectPoint(LinkedList linkedList2) {

        if (head == null || linkedList2 == null || linkedList2.head == null) {
            return null;
        }

        int length1 = 1;
        Node node1 = head;
        while (node1.getNext() != null) {
            node1 = node1.getNext();
            length1++;
        }

        int length2 = 1;
        Node node2 = linkedList2.head;
        while (node2.getNext() != null) {
            node2 = node2.getNext();
            length2++;
        }

        if (node1 != node2) {
            return null;
        }

        Node longNode = head;
        Node shortNode = linkedList2.head;
        int diffNum = 0;// 相差"元素个数"
        if (length1 > length2) {
            diffNum = length1 - length2;
        } else if (length1 < length2) {
            longNode = linkedList2.head;
            shortNode = head;
            diffNum = length2 - length1;
        }

        while (diffNum > 0) {// 移动"元素个数"步，到"元素个数"节点
            longNode = longNode.getNext();
            diffNum--;
        }

        while (!longNode.getItem().equals(shortNode.getItem())) {
            longNode = longNode.getNext();
            shortNode = shortNode.getNext();
        }

        return longNode;
    }

    @Test
    public void testPrint() {
        LinkedList linkedList = getLinkedList();
        linkedList.printList();
    }

    @Test
    public void testLength() {
        LinkedList linkedList = getLinkedList();
        int length = linkedList.length();
        System.out.println(length);
    }

    @Test
    public void testDelete() {
        LinkedList linkedList = getLinkedList();
        boolean delete = linkedList.delete(3);
        System.out.println(delete);
        linkedList.printList();
    }

    @Test
    public void testDeleteNode() {
        LinkedList linkedList = new LinkedList();
        linkedList.add(new Node(1));
        Node node = new Node(2);
        linkedList.add(node);
        linkedList.add(new Node(3));
        boolean delete = linkedList.deleteNode(node);
        System.out.println(delete);
        linkedList.printList();
    }

    @Test
    public void testReverse() {
        LinkedList linkedList = getLinkedList();
        linkedList.reverse();
        linkedList.printList();
    }

    @Test
    public void testHasCycle() {
        LinkedList linkedList = getLinkedList();
        Node node = new Node(4);
        linkedList.add(node);
        linkedList.add(new Node(5));
        linkedList.add(new Node(6));
        linkedList.add(node);
        boolean hasCycle = linkedList.hasCycle();
        System.out.println(hasCycle);
    }

    @Test
    public void testFindCycleEntryPoint() {
        LinkedList linkedList = getLinkedList();
        Node node = new Node(4);
        linkedList.add(node);
        linkedList.add(new Node(5));
        linkedList.add(new Node(6));
        linkedList.add(node);
        Node entryPoint = linkedList.findCycleEntryPoint();
        System.out.println(entryPoint);
    }

    @Test
    public void testSort() {
        LinkedList linkedList = new LinkedList();
        linkedList.add(new Node(6));
        linkedList.add(new Node(5));
        linkedList.add(new Node(3));
        linkedList.add(new Node(4));
        linkedList.add(new Node(1));
        linkedList.sort();
        linkedList.printList();
    }

    @Test
    public void testRemoveDuplicate() {
        LinkedList linkedList = getLinkedList();
        linkedList.add(new Node(5));
        linkedList.add(new Node(5));
        linkedList.add(new Node(3));
        linkedList.add(new Node(3));
        linkedList.add(new Node(1));
        linkedList.removeDuplicate();
        linkedList.printList();
    }

    @Test
    public void testFindBackwardK() {
        LinkedList linkedList = getLinkedList();
        linkedList.add(new Node(4));
        linkedList.add(new Node(5));
        linkedList.add(new Node(6));
        Node backwardK = linkedList.findBackwardK(1);
        System.out.println(backwardK);
    }

    @Test
    public void testPrintReverse() {
        LinkedList linkedList = getLinkedList();
        linkedList.printReverse(linkedList.head);
    }

    @Test
    public void testFindMid() {
        LinkedList linkedList = getLinkedList();
        linkedList.add(new Node(4));
        Node mid = linkedList.findMid();
        System.out.println(mid);
    }

    @Test
    public void testIsIntersect() {
        LinkedList linkedList1 = new LinkedList();
        linkedList1.add(new Node(1));
        linkedList1.add(new Node(2));
        Node node = new Node(3);
        linkedList1.add(node);

        LinkedList linkedList2 = new LinkedList();
        linkedList2.add(new Node(4));
        linkedList2.add(new Node(5));
        linkedList2.add(node);

        boolean intersect = linkedList1.isIntersect(linkedList2);
        System.out.println(intersect);
    }

    @Test
    public void testFindFirstIntersectPoint() {
        LinkedList linkedList1 = new LinkedList();
        linkedList1.add(new Node(1));
        linkedList1.add(new Node(2));
        Node node6 = new Node(6);
        Node node7 = new Node(7);
        Node node8 = new Node(8);
        linkedList1.add(node6);
        linkedList1.add(node7);
        linkedList1.add(node8);

        LinkedList linkedList2 = new LinkedList();
        linkedList2.add(new Node(4));
        linkedList2.add(new Node(5));
//        linkedList2.add(new Node(9));
//        linkedList2.add(new Node(10));
//        linkedList2.add(new Node(11));
        linkedList2.add(node6);

        Node firstIntersectPoint = linkedList1.findFirstIntersectPoint(linkedList2);
        System.out.println(firstIntersectPoint);
    }

    private static LinkedList getLinkedList() {
        LinkedList linkedList = new LinkedList();
        linkedList.add(new Node(1));
        linkedList.add(new Node(2));
        linkedList.add(new Node(3));

        return linkedList;
    }
}
