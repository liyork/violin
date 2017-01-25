package com.wolf.behavioral.template;

import java.util.ArrayList;
import java.util.List;

/**
 * <p> Description:
 * Define the skeleton of an algorithm in an operation, deferring some steps to subclasses.
 * Template Method lets subclasses redefine certain steps of an algorithm without changing the algorithm's structure.
 * fixes their ordering
 * refactoring to generalize
 * Template methods are a fundamental technique for code reuse.
 * <p/>
 * Date: 2016/7/18
 * Time: 8:18
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public abstract class AbstractApplication implements Application{

	List<Document> list = new ArrayList<Document>();

	@Override
	public void openDocument(String name) {
		if (!canOpenDocument(name)) {
			// cannot handle this document
			return;
		}

		Document doc = createDocument();
		list.add(doc);
		showAboutToOpenDocument(doc);
		doc.open();
		doc.read();
	}

	//may be overridden(hook)
	protected void showAboutToOpenDocument(Document doc) {
		System.out.println("show about to open doc");
	}

	//must be overridden
	protected abstract boolean canOpenDocument(String name);

	protected abstract Document createDocument();
}
