package com.wolf.structural.adapter.pluggable;

import java.util.List;

/**
 * <p> Description:架构级或者已有系统级代码，用于显示层级结构
 * 任何外界代码如想使用需通过以下三种方式实现：
 * 1.定义AbstractTreeDisplay的子类，重写特定方法，实现自己逻辑
 * 2.
 * <p/>
 * Date: 2016/7/4
 * Time: 14:12
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public interface TreeDisplay {

	public List<Node> getChildren(Node node);

	public Node createNode(String nodeName);

	public void buildTree(Node node);

	public void display();
}
