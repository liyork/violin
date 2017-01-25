package com.wolf.structural.adapter.pluggable.delegate;

import com.wolf.structural.adapter.pluggable.Node;

import java.util.List;

/**
 * <p> Description:必要接口
 * <p/>
 * Date: 2016/7/4
 * Time: 15:26
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public interface TreeAccessorDelegate {

	public List<Node> getChildren(Node node);

	public Node createNode(String nodeName);
}
