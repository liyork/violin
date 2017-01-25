package com.wolf.behavioral.mediator;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/14
 */
public class ListBox extends AbstractWidget {

	public ListBox(DialogDirector dialogDirector) {
		super(dialogDirector);
	}

	public String getSelection() {
		return "getSelection";
	}

	public void setSelection(String selection) {
		System.out.println("setSelection"+selection);
	}
}
