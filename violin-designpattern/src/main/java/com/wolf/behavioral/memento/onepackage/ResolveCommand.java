package com.wolf.behavioral.memento.onepackage;

/**
 * <b>功能</b>命令模式简化
 *
 * @author 李超
 * @Date 2016/7/15
 */
public class ResolveCommand {

	private ConstraintResolver constraintResolver;
	private ResolverMemento currentMemento ;

	public ResolveCommand(ConstraintResolver constraintResolver) {
		this.constraintResolver = constraintResolver;
	}

	public void execute(){
		//这个也可以提供给外部，等需要undo时再提供进来，即使他们拿到也没有用
		currentMemento = constraintResolver.createCurrentMemento();
		constraintResolver.increase();
		constraintResolver.solve();
	}

	public void unExecute(){
		constraintResolver.restoreMemento(this.currentMemento);
		constraintResolver.solve();
	}

	public ResolverMemento getCurrentMemento() {
		return currentMemento;
	}

	public void setCurrentMemento(ResolverMemento currentMemento) {
		this.currentMemento = currentMemento;
	}
}
