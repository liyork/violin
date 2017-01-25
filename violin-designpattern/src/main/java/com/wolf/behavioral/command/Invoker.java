package com.wolf.behavioral.command;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/6/20
 * Time: 9:34
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Invoker {

	Command command;

	List<Command> history = new ArrayList<Command>();

	public void storeCommand(Command command){
		this.command = command;
	}

	public void invoke(){
		history.add(this.command);
		this.command.execute();
	}

	public void undoOneStep(){
		Iterator<Command> iterator = history.iterator();
		if (iterator.hasNext()) {
			iterator.next().undo();
			iterator.remove();
		}
	}
}
