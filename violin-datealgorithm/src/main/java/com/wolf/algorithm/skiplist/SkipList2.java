package com.wolf.algorithm.skiplist;

/**
 * Description:
 * 多出来很多节点，但是符合定义规则，每个节点next和down
 * Skip lists are a data structure that can be used in place of balanced trees.
 * Skip lists use probabilistic balancing rather than strictly enforced balancing and as a result the algorithms
 * for insertion and deletion in skip lists are much simpler and significantly faster than equivalent algorithms for balanced trees.
 * <br/> Created on 2018/1/29 9:27
 *
 * @author 李超
 * @since 1.0.0
 */
public class SkipList2<T> {

    // 最高层数
    private final int MAX_LEVEL;
    // 总共层数
    private int totalLevel;
    // 表头
    private Node<T> head[];
    // 表尾
    private Node<T> NIL;
    // 生成randomLevel用到的概率值
    private final double P;
    // 论文里给出的最佳概率值
    private static final double OPTIMAL_P = 0.25;

    public SkipList2() {
        // 0.25, 15
        this(OPTIMAL_P);
    }

    public SkipList2(double probability) {
        P = probability;

        MAX_LEVEL = (int) Math.ceil(Math.log(Integer.MAX_VALUE) / Math.log(1 / OPTIMAL_P)) - 1;
        head = new Node[MAX_LEVEL];

        totalLevel = 1;
        head[0] = new Node<T>(Integer.MIN_VALUE, null);//最小值
        NIL = new Node<T>(Integer.MAX_VALUE, null);//最大值
        head[0].next = NIL;//head连接nil
    }

    class Node<T> {
        int key;
        T value;
        Node next;
        Node down;

        public Node(int key, T value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getDown() {
            return down;
        }

        public void setDown(Node down) {
            this.down = down;
        }
    }

    //从上向下搜索
    public T search(int searchKey) {
        int levelIndex = totalLevel - 1;
        Node<T> curNode = head[levelIndex];

        for (int i = levelIndex; i >= 0; i--) {
            //System.out.println("curNode:==>"+ JsonUtils.toJsonString(curNode));
            while (curNode.next.key < searchKey) {
                curNode = curNode.next;
            }

            if (curNode.next.key == searchKey) {//优化，上层如果找到直接返回,如果放在上面while中可能每次都判断性能不好，在这里补一下
                return (T) curNode.next.value;
            }

            if (i != 0) {//非最底层
                if (curNode == head[i]) {//没动且不是最底层(不是最底层防止数据不存在)，head没有down所以需要单独判断
                    curNode = head[i - 1];
                } else {//不是head且不是最底层则向下移动
                    curNode = curNode.down;
                    //System.out.println(curNode);
                }
            } else {
                return null;
            }
        }

//        if (curNode.key == searchKey) {
//            return curNode.value;
//        } else  //优化，由于上面查的是小于searchKey的最后一个元素，所以不可能等于了
        if (curNode.next.key == searchKey) {
            return (T) curNode.next.value;
        } else {
            return null;
        }
    }

    public void insert(int searchKey, T newValue) {
        Node<T>[] update = new Node[MAX_LEVEL];//第几层更新哪个元素
        Node<T> curNode = null;

        for (int level = totalLevel - 1; level >= 0; level--) {//从上致下,每层都插
            curNode = head[level];
            while (curNode.next.key < searchKey) {
                curNode = curNode.next;
            }
            update[level] = curNode;
        }

        Node<T> next = curNode.next;//下一个节点

        if (next.key == searchKey) {//相等则替换值
            next.value = newValue;
        } else {//新增
            int newKeyLevel = randomLevel();

            if (totalLevel < newKeyLevel) {
                for (int newLevel = totalLevel; newLevel < newKeyLevel; newLevel++) {
                    head[newLevel] = new Node<T>(Integer.MIN_VALUE, null);
                    head[newLevel].next = NIL;
                    update[newLevel] = head[newLevel];//新增的层直接使用head引用
                }
                totalLevel = newKeyLevel;
            }

            Node<T> newNode;

            Node<T> lowLevelNewNode = null;
            for (int i = 0; i < newKeyLevel; i++) {
                newNode = new Node<T>(searchKey, newValue);
                if (null != lowLevelNewNode) {
                    newNode.down = lowLevelNewNode;
                }
                newNode.next = update[i].next;
                update[i].next = newNode;
                lowLevelNewNode = newNode;
            }
        }
    }

    public void delete(int searchKey) {
        Node<T>[] update = new Node[MAX_LEVEL];
        Node<T> curNode = null;

        //找到每层的要删除的前一个元素
        for (int level = totalLevel - 1; level >= 0; level--) {
            curNode = head[level];
            while (curNode.next.key < searchKey) {
                curNode = curNode.next;
            }
            update[level] = curNode;
        }

        curNode = curNode.next;

        if (curNode.key == searchKey) {
            for (int i = 0; i < totalLevel; i++) {//从下层开始删除,因为上层索引可能没有这个元素
                if (update[i].next != curNode) {//上层无此节点索引
                    break;
                }
                update[i].next = curNode.next;//指向后一个节点
            }

            while (totalLevel > 0 && head[totalLevel - 1].next == NIL) {//上层第一个节点是空则收缩层
                totalLevel--;
            }
        }
    }

    private int randomLevel() {
        int lvl = 1;
        while (lvl < MAX_LEVEL && Math.random() < P) {
            lvl++;
        }
        return lvl;
    }

    public void print() {
        for (int i = totalLevel - 1; i >= 0; i--) {
            Node<T> curNode = head[i].next;//每层只关心本层的数据
            while (curNode != NIL) {
                System.out.print(curNode.key + "->");
                curNode = curNode.next;
            }
            System.out.println("NIL");
        }
    }

    public static void main(String[] args) {
        try {
            for (int i = 0; i < 20; i++) {
                System.out.println("i==========" + i);
                SkipList2<Integer> sl = new SkipList2<Integer>();
                sl.insert(20, 20);
                sl.insert(5, 5);
                sl.insert(10, 10);
                sl.insert(1, 1);
                sl.insert(100, 100);
                sl.insert(80, 80);
                sl.insert(60, 60);
                sl.insert(30, 30);
                sl.print();
                Integer search1 = sl.search(60);
                System.out.println("search1:" + search1);
                Integer search2 = sl.search(100);
                System.out.println("search2:" + search2);
                Integer search3 = sl.search(50);
                System.out.println(search3 == null ? "search3 not found" : "error");
                System.out.println("---");
                sl.delete(20);
                sl.delete(100);
                sl.print();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
