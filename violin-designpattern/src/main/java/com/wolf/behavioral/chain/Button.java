package com.wolf.behavioral.chain;

/**
 * <p> Description:由于java只支持单继承所以使用组合方式引入receiveTopic，通过它统一管理，
 * 否则需要在这里定义topic、successor、hasHelp及相关相同重复操作
 * <p/>
 * Date: 2016/7/11
 * Time: 9:02
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Button extends AbstractWidget implements HelpHandler {

	public Button(Topic receiveTopic, HelpHandler successor) {
		this.abstractHelpHandler = new AbstractHelpHandler(receiveTopic, successor);
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
			System.out.println("handle in Button...");
		}else{
			abstractHelpHandler.handler(topic);
		}
	}
}
