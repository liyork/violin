package com.wolf.structural.adapter.pluggable.delegate;

import com.wolf.structural.adapter.pluggable.Node;
import com.wolf.structural.adapter.pluggable.NodeImpl;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> Description:实现必要接口
 * <p/>
 * Date: 2016/7/4
 * Time: 14:36
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class DirectoryBrowser implements TreeAccessorDelegate {

	static Map<String,String> path = new HashMap<String, String>();

	static {
		path.put("a", "D:\\intellijWrkSpace\\violin\\src\\main\\java\\com\\car\\designpattern\\structural\\adapter\\pluggable\\a");
		path.put("b", "D:\\intellijWrkSpace\\violin\\src\\main\\java\\com\\car\\designpattern\\structural\\adapter\\pluggable\\a\\b");
		path.put("c", "D:\\intellijWrkSpace\\violin\\src\\main\\java\\com\\car\\designpattern\\structural\\adapter\\pluggable\\a\\c");
	}

	@Override
	public List<Node> getChildren(Node node) {
		String tempPath = path.get(node.getName());
		List<Node> result = new ArrayList<Node>();
		if (null == tempPath) {
			return new ArrayList<Node>();
		}
		File file = new File(tempPath);
		String[] list = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return new File(dir, name).isDirectory();
			}
		});
		for (String subFileName : list) {
			result.add(createNode(subFileName));
		}
		return result;
	}

	@Override
	public Node createNode(String nodeName) {
		return new NodeImpl(nodeName);
	}

}
