package com.wolf.structural.adapter.pluggable.parameterized;

import com.wolf.structural.adapter.pluggable.Node;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class GetChildrenBlock implements Block {

	private ParameterDirectoryTreeDisplay treeDisplay;

	static Map<String,String> path = new HashMap<String, String>();

	static {
		path.put("a", "D:\\intellijWrkSpace\\violin\\src\\main\\java\\com\\car\\designpattern\\structural\\adapter\\pluggable\\a");
		path.put("b", "D:\\intellijWrkSpace\\violin\\src\\main\\java\\com\\car\\designpattern\\structural\\adapter\\pluggable\\a\\b");
		path.put("c", "D:\\intellijWrkSpace\\violin\\src\\main\\java\\com\\car\\designpattern\\structural\\adapter\\pluggable\\a\\c");
	}

	public GetChildrenBlock(ParameterDirectoryTreeDisplay treeDisplay){
		this.treeDisplay = treeDisplay;
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
		return treeDisplay.getCreateNodeBlock().createNode(nodeName);
	}

}
