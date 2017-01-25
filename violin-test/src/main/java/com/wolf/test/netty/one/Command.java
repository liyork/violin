package com.wolf.test.netty.one;

import java.io.Serializable;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/11
 * Time: 16:01
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Command implements Serializable {

	private static final long serialVersionUID = 7590999461767050471L;

	private String actionName;

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}
}
