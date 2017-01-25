package com.wolf.behavioral.chain;

/**
 * <p> Description:
 * <p/>
 * Date: 2016/7/11
 * Time: 8:53
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class AbstractHelpHandler implements HelpHandler{

	private Topic topic;

	private HelpHandler successor;

	public AbstractHelpHandler(Topic receiveTopic, HelpHandler successor) {
		this.topic = receiveTopic;
		this.successor = successor;
	}

	@Override
	public boolean hasHelp(Topic topic) {
		return this.topic == topic;
	}

	@Override
	public void setHandler(HelpHandler handler, Topic topic) {
		this.successor = handler;
	}

	@Override
	public void handler(Topic topic) {
		if (null != successor) {
			successor.handler(topic);
		}
	}

	public Topic getTopic() {
		return topic;
	}
}
