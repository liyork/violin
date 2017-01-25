package com.wolf.behavioral.chain;

/**
 * <p> Description:需求：一个请求，每步一个模型，能处理就返回否则就让其他模块处理。(此种需求可以代替if else)
 * 也可加入权限控制，即当前模块如果没有权限就直接给下个模块，否则先处理下，看看结果是否符合预期再决定是否给下个模块。
 * <p/>
 * Date: 2016/7/11
 * Time: 9:12
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Client {

	public static void main(String[] args) {

		Application application = new Application(Topic.APPLICATION_TOPIC, null);

		Dialog dialog = new Dialog(Topic.PRINT_TOPIC, application);

		Button button = new Button(Topic.PAPER_ORIENTATION_TOPIC, dialog);

		button.handler(Topic.PAPER_ORIENTATION_TOPIC);

		button.handler(Topic.APPLICATION_TOPIC);
	}
}
