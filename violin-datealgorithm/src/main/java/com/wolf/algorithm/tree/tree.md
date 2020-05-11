 * Description: 二叉树
 * 结合有序数组与链表的优点，二叉树中查找数据与在数组中查找数据一样快，添加删除数据也与链表中一样高效。
 * 基本概念：
 * 是n(n>=0)个有限元素的集合，该集合或者空、或者由一个根(root)的元素及两个不相交的、被分别称为左子树和右子树的二叉树组成。
 * 当集合为空时，该二叉树被称为空二叉树。二叉树中，一个元素也称为一个节点。
 * 1. 节点的度。节点所拥有子树的个数。
 * 2. 叶节点。度为0的节点。
 * 3. 分枝节点。度不为0的节点。一棵树的节点除叶节点外，其余都是分枝节点。
 * 4. 树中一个节点的子树的根节点称为这个节点的孩子。这个节点称为它孩子节点的双亲。具有同一个双亲的孩子节点互称为兄弟。
 * 5. 路径、路径长度。一棵树的一串节点n1,n2,...,nk有如下关系：节点ni是n(i+1)的父节点(1<=i<k)，
 * 就把n1,n2,...,nk称为一条由n1至nk的路径。这条路径的长度是k-1。
 * 6. 祖先、子孙。在树中，从上到下，如果有一条路径从节点M到节点N，那么M就称为N的祖先，N称为M的子孙。
 * 7. 节点的层数。规定树的根节点的层数为1，其余节点的层数等于它的双亲节点的层数+1.
 * 8. 树的深度。树中所有节点的最大层数。
 * 9. 树的度。树中各节点度的最大值。
 * <p>
 * 基本性质：
 * 1. 非空二叉树的第i层上最多有2^(i-1)个节点(i>=1)
 * 2. 一课深度为k的二叉树中，最多具有(2^k)-1个节点，最少有k+1个节点。
 * 3. 非空二叉树，度为0的节点(叶子)总是比度为2的节点多一个。
 * 证明：n0表示度为0的节点总数，n1表示度为1的节点总数，n2表示度为2的节点总数，n表示整个完全二叉树的节点总数，则n=n0+n1+n2，
 * 根据二叉树和数的性质(所有节点的度数之和+1=节点总数)得到，n=n1+2*n2+1，依据两等式可得n0=n2+1。
 * 4. 具有n个节点的完全二叉树的深度为[log2n]+1
 * 证明：根据性质2，深度为k的二叉树最多只有(2^k)-1个节点，且完全二叉树的定义是与同深度的满二叉树前面编号相同，即它的总节点数(n)
 * 位于k层和k-1层满二叉树容量之间，即(2^(k-1))-1<n<=(2^k)-1或(2^(k-1))<=n<2^k，三边同时取对数得到：k-1<=log2n<k，因为
 * k是整数，所以k=[log2n]+1
 * 5. 具有n个节点的完全二叉树，按照从上至下和从左到右顺序对二叉树的节点从1开始顺序编号，对于任意的序号为i的节点有：
 * a.如果i>1，那么i的节点的双亲节点序号为i/2，如果i=1，那么是根节点，无双亲。
 * b.如果2i<=n，那么序号为i的节点的左孩子是2i，如果2i>n那么序号为i的节点无左孩子。
 * c.如果2i+1<=n，那么序号为i的节点的右孩子是2i+1，如果2i+1>n那么序号为i的节点无右孩子。
 * 若是对二叉树的根节点从0编号，那么i节点的双亲为(i-1)/2，左孩子2i+1,右孩子2i+2
 * <p>
 * 例题1：一棵完全二叉树有1001个节点，其中叶子节点的个数是多少?
 * 分析：二叉树公式(性质2)：n=n0+n1+n2=>n0+n1+(n0-1)=>2*n0+n1-1。而完全二叉树中，n1只能取0或1(最底层变化后导致n1只能是0或1)，
 * 若n1=1，那么2*n0+1-1=1001=>n05=500.5小数，不符合题意。若n1=0那么2*n0+0-1=>500。
 * <p>
 * 例题2：如果根的层次为1，具有61个节点的完全二叉树的高度为多少?
 * 分析：依据性质4，(log2 61)+1=6
 * <p>
 * 例题3：在具有100个节点的树中，其边的数目为多少?
 * 分析：一棵树中，除了根节点，每一个节点都有一条入边，因此100-1=99。
 
 
1. Tree概念

A tree is a (possibly non-linear) data structure made up of nodes or vertices 
and edges without having any cycle. The tree with no nodes is called the null 
or empty tree. A tree that is not empty consists of a root node and potentially 
many levels of additional nodes that form a hierarchy.
树是由结点或顶点和边组成的(可能是非线性的)且不存在着任何环的一种数据结构。没有结点的树称为空(null或empty)树。
一棵非空的树包括一个根结点，还(很可能)有多个附加结点，所有结点构成一个多级分层结构。

2. 结构

Degree	The number of sub trees of a node.	
度	结点所拥有的子树个数称为结点的度(Degree)。

Level	The level of a node is defined by ０ + (the number of connections between the node and the root).	
层次	结点的层次(Level)从根(Root)开始定义起，根为第0层，根的孩子为第1层。以此类推，若某结点在第i层，那么其子树的根就在第i+1层。

Height of node	The height of a node is the number of edges on the longest path between that node and a leaf.	
结点的高度	结点的高度是该结点和某个叶子之间存在的最长路径上的边的个数。 

Height of tree	The height of a tree is the height of its root node.	
树的高度	树的高度是其根结点的高度。 

Depth of node
The depth of a node is the number of edges from the tree's root node to the node.	
结点的深度	结点的深度是从树的根结点到该结点的边的个数。 （注：树的深度指的是树中结点的最大层次。）

3. 二叉树

每个结点至多拥有两棵子树(即二叉树中不存在度大于2的结点)，并且，二叉树的子树有左右之分，其次序不能任意颠倒。

二叉树的性质
（1）若二叉树的层次从0开始，则在二叉树的第i层至多有2^i个结点(i>=0)。
（2）高度为k的二叉树最多有2^(k+1) - 1个结点(k>=-1)。 (空树的高度为-1，高度为1表示第0层)
（3）对任何一棵二叉树，如果其叶子结点(度为0)数为m, 度为2的结点数为n, 则m = n + 1。


4. 完美二叉树(Perfect Binary Tree)或满二叉树
A Perfect Binary Tree(PBT) is a tree with all leaf nodes at the same depth. 
All internal nodes have degree 2.
一个深度为k(>=-1)且有2^(k+1) - 1个结点的二叉树称为完美二叉树。
Every node except the leaf nodes have two children and every level (last level too) is completely filled. 
除了叶子结点之外的每一个结点都有两个孩子，每一层(当然包含最后一层)都被完全填充。


5. 完全二叉树(Complete Binary Tree)
除了最后一层之外的其他层都被完全填充，并且所有节点都保持向左对齐。
A Complete Binary Tree （CBT) is a binary tree in which every level, 
except possibly the last, is completely filled, and all nodes 
are as far left as possible.
完全二叉树从根结点到倒数第二层完全填充，叶子结点都靠左对齐。
Every level except the last level is completely filled and all the nodes are left justified. 除了最后一层之外的其他每一层都被完全填充，并且所有结点都保持向左对齐。
满二叉树：除了叶子节点之外的每一个节点都有两个孩子，除最后一层外每一层都被完全填充。

6. 完满二叉树(Full Binary Tree)
A Full Binary Tree (FBT) is a tree in which every node other than the leaves has two children.
所有非叶子结点的度都是2。叶子节点可以在任意层
Every node except the leaf nodes have two children. 除了叶子结点之外的每一个结点都有两个孩子结点。
完满二叉树：除了叶子节点之外的每一个节点都有两个孩子。(叶子节点可位于任意层)

7. 关系

完美(Perfect)二叉树一定是完全(Complete)二叉树，但完全(Complete)二叉树不一定是完美(Perfect)二叉树。
完美(Perfect)二叉树一定是完满(Full)二叉树，但完满(Full)二叉树不一定是完美(Perfect)二叉树。
完全(Complete)二叉树可能是完满(Full)二叉树，完满(Full)二叉树也可能是完全(Complete)二叉树。
既是完全(Complete)二叉树又是完满(Full)二叉树也不一定就是完美(Perfect)二叉树。
