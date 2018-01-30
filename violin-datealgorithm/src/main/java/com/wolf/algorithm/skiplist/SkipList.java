package com.wolf.algorithm.skiplist;

/**
 * Description:
 * Node
 *    -forward[0] --> Node
 *    -forward[1] --> Node
 *    -forward[2] --> Node
 *    -forward[3] --> Node
 *    -forward[4] --> Node
 *                      -forward[0] --> Node
 *                      -forward[1] --> Node
 *                      -forward[2] --> Node
 *                      -forward[3] --> Node
 *                                          -forward[0] --> nil
 *                                          -forward[1] --> nil
 *                                          -forward[2] --> nil
 *                                          -forward[3] --> nil
 *
 *  查询 node.forward[i].value -->  current = node.forward[i].value -->...
 *
 *    这种结构感觉好怪异!节省空间
 *
 *    一个拥有k个指针的结点称为一个k层结点（level k node）。按照上面的逻辑，50%的结点为1层，25%的结点为2层，12.5%的结点为3层...
 *    使一个k层结点的第i个指针指向第i层的下一个结点，而不是它后面的第2^(i-1)个结点，那么结点的插入和删除只需要原地修改操作；
 *    一个结点的层数，是在它被插入的时候随机选取的，并且永不改变。因为这样的数据结构是基于链表的，并且额外的指针会跳过中间结点，所以作者称之为跳表（Skip Lists）。
 * <br/> Created on 2018/1/29 9:27
 *
 * @author 李超
 * @since 1.0.0
 */
public class SkipList<T> {

    // 最高层数
    private final int MAX_LEVEL;
    // 总共
    private int totalLevel;
    // 表头
    private SkipListNode<T> head;
    // 表尾
    private SkipListNode<T> NIL;
    // 生成randomLevel用到的概率值
    private final double P;
    // 论文里给出的最佳概率值
    private static final double OPTIMAL_P = 0.25;

    public SkipList() {
        // 0.25, 15
        this(OPTIMAL_P, (int) Math.ceil(Math.log(Integer.MAX_VALUE) / Math.log(1 / OPTIMAL_P)) - 1);
    }

    public SkipList(double probability, int maxLevel) {
        P = probability;
        MAX_LEVEL = maxLevel;

        totalLevel = 1;
        head = new SkipListNode<T>(Integer.MIN_VALUE, null, maxLevel);//最小值
        NIL = new SkipListNode<T>(Integer.MAX_VALUE, null, maxLevel);//最大值
        for (int i = MAX_LEVEL - 1; i >= 0; i--) {//head各层为nil
            head.forward[i] = NIL;//head连接nil
        }
    }

    // 既是节点又包含关联节点，每个节点的forward[i]组成一个层
    class SkipListNode<T> {
        int key;
        T value;
        SkipListNode[] forward;

        public SkipListNode(int key, T value, int level) {
            this.key = key;
            this.value = value;
            this.forward = new SkipListNode[level];
        }
    }

    public T search(int searchKey) {
        SkipListNode<T> curNode = head;

        for (int i = totalLevel - 1; i >= 0; i--) {
            while (curNode.forward[i].key < searchKey) {//每层都是i，下个forward集合也是i
                //一开始出不来数据，中午想了下，想着二分查找，这种随机建立索引不行，应该自己建立logx=n的索引，还想着怎么建，然后再一想，
                // 不对，这个就是先上层找到最后最小的然后下移动，没错。
                curNode = curNode.forward[i];
            }
        }

        if (curNode.key == searchKey) {
            return curNode.value;
        } else if (curNode.forward[0].key == searchKey) {
            return (T) curNode.forward[0].value;
        } else {
            return null;
        }
    }

    public void insert(int searchKey, T newValue) {
        SkipListNode<T>[] update = new SkipListNode[MAX_LEVEL];//第几层更新那个元素
        SkipListNode<T> curNode = head;

        for (int i = totalLevel - 1; i >= 0; i--) {//从上致下,下层继续从上层curNode开始
            while (curNode.forward[i].key < searchKey) {
                curNode = curNode.forward[i];
            }
            // curNode.key < searchKey <= curNode.forward[i].key
            update[i] = curNode;
        }

        SkipListNode<T> forwardNode = curNode.forward[0];//下一个节点

        if (forwardNode.key == searchKey) {//相等则替换值
            forwardNode.value = newValue;
        } else {
            int newKeyLevel = randomLevel();

            if (totalLevel < newKeyLevel) {
                for (int newLevel = totalLevel; newLevel < newKeyLevel; newLevel++) {//新增的层直接使用head引用
                    update[newLevel] = head;
                }
                totalLevel = newKeyLevel;
            }

            SkipListNode<T> newNode = new SkipListNode<T>(searchKey, newValue, newKeyLevel);

            for (int i = 0; i < newKeyLevel; i++) {
                newNode.forward[i] = update[i].forward[i];//更新new节点的下一个元素=上一个元素的后续
                update[i].forward[i] = newNode;//第几层后续为当前节点
            }
        }
    }

    public void delete(int searchKey) {
        SkipListNode<T>[] update = new SkipListNode[MAX_LEVEL];
        SkipListNode<T> curNode = head;

        for (int level = totalLevel - 1; level >= 0; level--) {
            while (curNode.forward[level].key < searchKey) {
                curNode = curNode.forward[level];
            }
            // curNode.key < searchKey <= curNode.forward[level].key
            update[level] = curNode;
        }

        curNode = curNode.forward[0];

        if (curNode.key == searchKey) {
            for (int i = 0; i < totalLevel; i++) {//从下层开始删除
                if (update[i].forward[i] != curNode) {//上层无此节点索引
                    break;
                }
                update[i].forward[i] = curNode.forward[i];//指向后一个节点
            }

            while (totalLevel > 0 && head.forward[totalLevel - 1] == NIL) {//上层第一个节点是空则收缩层
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
            SkipListNode<T> curNode = head.forward[i];//每层只关心本层的数据
            while (curNode != NIL) {
                System.out.print(curNode.key + "->");
                curNode = curNode.forward[i];
            }
            System.out.println("NIL");
        }
    }

    public static void main(String[] args) {
        SkipList<Integer> sl = new SkipList<Integer>();
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
        System.out.println("---");
        sl.delete(20);
        sl.delete(100);
        sl.print();
    }
}
