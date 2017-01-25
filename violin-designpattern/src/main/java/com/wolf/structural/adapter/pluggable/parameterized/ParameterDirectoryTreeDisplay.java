package com.wolf.structural.adapter.pluggable.parameterized;

import com.wolf.structural.adapter.pluggable.AbstractTreeDisplay;
import com.wolf.structural.adapter.pluggable.Node;

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
public class ParameterDirectoryTreeDisplay extends AbstractTreeDisplay {

	private Block createNodeBlock;
	private Block getChildrenBlock;

	public void setGetChildrenBlock(Block getChildrenBlock) {
		this.getChildrenBlock = getChildrenBlock;
	}

	@Override
	public List<Node> getChildren(Node node) {
		return getChildrenBlock.getChildren(node);
	}

	@Override
	public Node createNode(String nodeName) {
		return createNodeBlock.createNode(nodeName);
	}

	public Block getCreateNodeBlock() {
		return createNodeBlock;
	}

	public void setCreateNodeBlock(Block createNodeBlock) {
		this.createNodeBlock = createNodeBlock;
	}
}
