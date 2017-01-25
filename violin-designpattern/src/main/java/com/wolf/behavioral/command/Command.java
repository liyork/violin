package com.wolf.behavioral.command;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/20
 * Time: 9:30
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public interface Command {

	public void execute();

	public void undo();
}
