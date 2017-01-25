package com.wolf.structural.adapter.pluggable.delegate;

import com.wolf.structural.adapter.pluggable.Node;
import com.wolf.structural.adapter.pluggable.AbstractTreeDisplay;

import java.util.List;

/**
 * <p> Description:默认树展示方式，实现对原系统存在的TreeDisplay的利用,通过代理出去“narrow interface”
 * 外部通过实现TreeAccessorDelegate即可
 * <p/>
 * Date: 2016/7/4
 * Time: 14:36
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class DefaultDirectoryTreeDisplay extends AbstractTreeDisplay {

	private TreeAccessorDelegate delegate;

	public DefaultDirectoryTreeDisplay(TreeAccessorDelegate delegate){
		this.delegate = delegate;
	}
	@Override
	public List<Node> getChildren(Node node) {
		return delegate.getChildren(node);
	}

	@Override
	public Node createNode(String nodeName) {
		return delegate.createNode(nodeName);
	}

	public void setDelegate(TreeAccessorDelegate delegate) {
		this.delegate = delegate;
	}
}
