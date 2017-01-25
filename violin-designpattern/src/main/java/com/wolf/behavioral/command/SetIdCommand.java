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
public class SetIdCommand extends AbstractCommand implements Command {

	public SetIdCommand(Receiver receiver) {
		this.receiver = receiver;
	}

	@Override
	public void execute() {

		System.out.println("set id command...execute");
		receiver.setId(1);
	}

	@Override
	public void undo() {
		System.out.println("set id command...undo");
		receiver.setId(0);
	}
}
