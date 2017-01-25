package com.wolf.structural.adapter.pluggable.parameterized;

import com.wolf.structural.adapter.pluggable.Node;
import com.wolf.structural.adapter.pluggable.NodeImpl;

import java.util.List;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/4
 * Time: 16:27
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class CreateNodeBlock implements Block {
	@Override
	public List<Node> getChildren(Node node) {
		return null;
	}

	@Override
	public Node createNode(String nodeName) {
		return new NodeImpl(nodeName);
	}
}
