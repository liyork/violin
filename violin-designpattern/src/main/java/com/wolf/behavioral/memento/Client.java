package com.wolf.behavioral.memento;


import com.wolf.behavioral.memento.onepackage.ConstraintResolver;
import com.wolf.behavioral.memento.onepackage.ResolveCommand;
import com.wolf.behavioral.memento.onepackage.ResolverMemento;

/**
 * <b>功能</b>
 * Without violating encapsulation, capture and externalize an object's internal state so that
 * the object can be restored to this state later.
 * <p/>
 * applicability
 * a snapshot of (some portion of) an object's state must be saved so that it can be
 * restored to that state later, and a direct interface to obtaining the state would
 * expose implementation details and break the object's encapsulation.
 *
 * Unless encapsulating and restoring Originator state is cheap, the patter might not be appropriate.
 *
 * Storing incremental changes.
 * @author 李超
 * @Date 2016/7/15
 */
public class Client {

	public static void main(String[] args) {
		//报错
		//new ResolverMemento()

		ResolveCommand resolveCommand = new ResolveCommand(new ConstraintResolver(1.1, 2.2));
		resolveCommand.execute();

		ResolverMemento currentMemento = resolveCommand.getCurrentMemento();
		resolveCommand.execute();
		resolveCommand.execute();

		resolveCommand.setCurrentMemento(currentMemento);
		resolveCommand.unExecute();
	}
}
