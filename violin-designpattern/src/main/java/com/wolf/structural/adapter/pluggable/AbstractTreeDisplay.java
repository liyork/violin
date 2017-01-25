package com.wolf.structural.adapter.pluggable;


import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> Description:最小化让子类实现的接口
 *
 * find a "narrow" interface for Adaptee(文件结构、类结构、组织结构)
 * <p/>
 * Date: 2016/7/4
 * Time: 14:15
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractTreeDisplay implements TreeDisplay {

	private Map<Integer,List<Node>> hierarchy = new LinkedHashMap<Integer, List<Node>>();
	private int currentLevel;

	@Override
	public void buildTree(Node node) {
		build(node);
	}

	private void build(Node node) {
		currentLevel++;
		List<Node> children = getChildren(node);
		for (Node child : children) {
			List<Node> nodes = hierarchy.get(currentLevel);
			if (CollectionUtils.isEmpty(nodes)) {
				nodes = new ArrayList<Node>();
				hierarchy.put(currentLevel,nodes);
			}
			nodes.add(child);
			build(child);
		}
	}


	@Override
	public void display() {
		for (Map.Entry<Integer, List<Node>> entry : hierarchy.entrySet()) {
			List<Node> nodes = entry.getValue();
			for (Node node : nodes) {
				System.out.println(entry.getKey()+":"+ node.getName());
			}
		}
	}
}
