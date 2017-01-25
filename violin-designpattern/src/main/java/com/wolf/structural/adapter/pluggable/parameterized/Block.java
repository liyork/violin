package com.wolf.structural.adapter.pluggable.parameterized;

import com.wolf.structural.adapter.pluggable.Node;

import java.util.List;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/4
 * Time: 16:26
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public interface Block {
	List<Node> getChildren(Node node);

	Node createNode(String nodeName);
}
