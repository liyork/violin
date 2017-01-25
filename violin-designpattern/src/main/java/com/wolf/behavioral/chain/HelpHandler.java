package com.wolf.behavioral.chain;

/**
 * <p> Description:责任链接口定义
 * The idea of this pattern is to decouple senders and receivers by giving multiple objects a chance to handle a request. The request gets passed along a chain of objects until one of them handles it.
 * the request has an implicit receiver
 * <p/>
 * Applicability
 * more than one object may handle a request, and the handler isn't known a priori
 * the set of objects that can handle a request should be specified dynamically.
 * <p/>
 * Added flexibility in assigning responsibilities to objects.
 * <p/>
 * Date: 2016/7/11
 * Time: 8:51
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public interface HelpHandler {

	public boolean hasHelp(Topic topic);

	public void setHandler(HelpHandler handler, Topic topic);

	//这里也可以封装request对象传入
	public void handler(Topic topic);
}
