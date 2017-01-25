package com.wolf.behavioral.chain;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/11
 * Time: 9:02
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Dialog extends AbstractWidget implements HelpHandler {

	public Dialog(Topic topic, HelpHandler successor) {
		this.abstractHelpHandler = new AbstractHelpHandler(topic, successor);
	}

	@Override
	public boolean hasHelp(Topic topic) {
		return abstractHelpHandler.hasHelp(topic);
	}

	@Override
	public void setHandler(HelpHandler handler, Topic topic) {
		abstractHelpHandler.setHandler(handler,topic);
	}

	@Override
	public void handler(Topic topic) {
		if (hasHelp(topic)) {
			System.out.println("handle in Dialog...");
		}else{
			abstractHelpHandler.handler(topic);
		}
	}
}
