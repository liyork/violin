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
public class SetNameCommand extends AbstractCommand implements Command {

	public SetNameCommand(Receiver receiver) {
		this.receiver = receiver;
	}

	@Override
	public void execute() {
		System.out.println("set name command...execute");
		receiver.setName("xxxx");
	}

	@Override
	public void undo() {
		System.out.println("set name command...undo");
		receiver.setName(null);
	}
}
