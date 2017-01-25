package com.wolf.behavioral.command;

/**
 * <p> Description:命令模式解耦调用端和接收端，参数化请求。
 * 　　将来自客户端的请求传入一个对象，从而使你可用不同的请求对客户进行参数化。用于“行为请求者”与“行为实现者”解耦，
 * 可实现二者之间的松耦合，以便适应变化。分离变化与不变的因素。
 * 在面向对象的程序设计中，一个对象调用另一个对象，一般情况下的调用过程是：创建目标对象实例；设置调用参数；调用目标对象的方法。
 * 但在有些情况下有必要使用一个专门的类对这种调用过程加以封装，我们把这种专门的类称作command类。
 * <p/>
 * Command模式可应用于
 * a）整个调用过程比较繁杂，或者存在多处这种调用。这时，使用Command类对该调用加以封装，便于功能的再利用。
 * b）调用前后需要对调用参数进行某些处理。
 * c）调用前后需要进行某些额外处理，比如日志，缓存，记录历史操作等。
 * <p/>
 * Command模式有如下效果：
 * a）将调用操作的对象和知道如何实现该操作的对象解耦。
 * b）Command是头等对象。他们可以像其他对象一样被操作和扩展。
 * c）你可将多个命令装配成一个符合命令。
 * d）增加新的Command很容易，因为这无需改变现有的类。
 * <p/>
 * 4.应用场景
 * 在下面的情况下应当考虑使用命令模式：
 * 1）使用命令模式作为"CallBack"在面向对象系统中的替代。"CallBack"讲的便是先将一个函数登记上，然后在以后调用此函数。
 * 2）需要在不同的时间指定请求、将请求排队。一个命令对象和原先的请求发出者可以有不同的生命期。
 * 换言之，原先的请求发出者可能已经不在了，而命令对象本身仍然是活动的。这时命令的接收者可以是在本地，
 * 也可以在网络的另外一个地址。命令对象可以在串形化之后传送到另外一台机器上去。
 * 3）系统需要支持命令的撤消(undo)。命令对象可以把状态存储起来，等到客户端需要撤销命令所产生的效果时，
 * 可以调用undo()方法，把命令所产生的效果撤销掉。命令对象还可以提供redo()方法，以供客户端在需要时，再重新实施命令效果。
 * 4）如果一个系统要将系统中所有的数据更新到日志里，以便在系统崩溃时，可以根据日志里读回所有的数据更新命令，
 * 重新调用Execute()方法一条一条执行这些命令，从而恢复系统在崩溃前所做的数据更新。
 * Date: 2016/6/20
 * Time: 9:34
 *
 * @author 李超
 * @version 1.0
 * @since 1.0
 */
public class Client {

	public static void main(String[] args) {
		Receiver receiver = new Receiver();
		System.out.println(receiver.getId()+":"+receiver.getName());
		Command command = new SetIdCommand(receiver);
		Invoker invoker = new Invoker();
		invoker.storeCommand(command);
		invoker.invoke();

		Command nameCommand = new SetNameCommand(receiver);
		invoker.storeCommand(nameCommand);
		invoker.invoke();

		System.out.println(receiver.getId()+":"+receiver.getName());
		invoker.undoOneStep();
		System.out.println(receiver.getId() + ":" + receiver.getName());
		invoker.undoOneStep();
		System.out.println(receiver.getId() + ":" + receiver.getName());
	}
}
