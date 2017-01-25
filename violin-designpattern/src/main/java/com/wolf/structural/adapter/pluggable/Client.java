package com.wolf.structural.adapter.pluggable;

import com.wolf.structural.adapter.pluggable.delegate.DefaultDirectoryTreeDisplay;
import com.wolf.structural.adapter.pluggable.delegate.DirectoryBrowser;
import com.wolf.structural.adapter.pluggable.delegate.OrganizationBrowser;
import com.wolf.structural.adapter.pluggable.delegate.TreeAccessorDelegate;
import com.wolf.structural.adapter.pluggable.parameterized.CreateNodeBlock;
import com.wolf.structural.adapter.pluggable.parameterized.GetChildrenBlock;
import com.wolf.structural.adapter.pluggable.parameterized.ParameterDirectoryTreeDisplay;
import com.wolf.structural.adapter.pluggable.subclass.DirectoryTreeDisplay;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/4
 * Time: 14:47
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Client {

	public static void main(String[] args) {
//		adapterBySubClass();
//		adapterByDelegate();
		adapterByParameterized();
	}

	private static void adapterBySubClass() {
		DirectoryTreeDisplay treeDisplay = new DirectoryTreeDisplay();
		treeDisplay.buildTree(treeDisplay.createNode("a"));
		treeDisplay.display();
	}

	private static void adapterByDelegate() {
		TreeAccessorDelegate directoryDelegate = new DirectoryBrowser();
		DefaultDirectoryTreeDisplay treeDisplay = new DefaultDirectoryTreeDisplay(directoryDelegate);
		treeDisplay.buildTree(treeDisplay.createNode("a"));
		treeDisplay.display();

		TreeAccessorDelegate orgDelegate = new OrganizationBrowser();
		treeDisplay.setDelegate(orgDelegate);
		treeDisplay.buildTree(treeDisplay.createNode("a"));
		treeDisplay.display();
	}

	private static void adapterByParameterized() {
		ParameterDirectoryTreeDisplay treeDisplay = new ParameterDirectoryTreeDisplay();
		treeDisplay.setCreateNodeBlock(new CreateNodeBlock());
		treeDisplay.setGetChildrenBlock(new GetChildrenBlock(treeDisplay));
		treeDisplay.buildTree(treeDisplay.createNode("a"));
		treeDisplay.display();
	}
}
