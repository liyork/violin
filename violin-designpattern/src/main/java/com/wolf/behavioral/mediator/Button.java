package com.wolf.behavioral.mediator;

/**
 * <b>功能</b>
 *
 * @author 李超
 * @Date 2016/7/14
 */
public class Button extends AbstractWidget {


	public Button(DialogDirector dialogDirector) {
		super(dialogDirector);
	}

	public void grayTheColor(){
		System.out.println("make color gray");
	}
}
