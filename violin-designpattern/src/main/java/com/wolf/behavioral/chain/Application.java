package com.wolf.behavioral.chain;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/11
 * Time: 9:09
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Application extends AbstractHelpHandler {

	public Application(Topic topic, HelpHandler successor) {
		super(topic, successor);
	}

	@Override
	public void handler(Topic topic) {
		System.out.println("show a list of help topics in Application");
	}
}
